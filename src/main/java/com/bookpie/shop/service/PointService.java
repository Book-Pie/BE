package com.bookpie.shop.service;

import com.bookpie.shop.config.ApiConfig;
import com.bookpie.shop.domain.OrderPoint;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.point.PointDto;
import com.bookpie.shop.repository.OrderPointRepository;
import com.bookpie.shop.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
public class PointService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderPointRepository orderPointRepository;
    @Autowired
    private ApiConfig apiConfig;

    // 포인트 충전
    public JSONObject charge(PointDto dto) {
        // 응답할 JSON객체 생성
        JSONObject response = new JSONObject();
        try {
            // 1. iamport로부터 access 토큰 받아오기
            String uri = "https://api.iamport.kr/users/getToken";
            JSONObject jsonObject = new JSONObject();
            log.info("아임포트 키 : " + apiConfig.getIamportKey());
            log.info("아임포트 시크릿 : " + apiConfig.getIamportSecret());
            jsonObject.put("imp_key", apiConfig.getIamportKey());
            jsonObject.put("imp_secret", apiConfig.getIamportSecret());
            String type = "POST";
            // 1-1. callApi 함수를 호출하여 token값 받아오기
            JSONObject token = callApi(jsonObject, type, uri);
            log.info("access 토큰 : " + token);

            // 2. imp_uid로 아임포트 서버에서 결제 정보 조회
            //JSONObject jsonObject1 = new JSONObject();
            uri = "https://api.iamport.kr/payments/" + dto.getImp_uid();
            type = "GET";
            // 2-1. 가져온 token값에서 access_token값만을 추출
            JSONObject realToken = (JSONObject) token.get("response");

            log.info("찐 access_token : " + realToken.get("access_token"));
            jsonObject.put("Authorization", realToken.get("access_token"));
            // 2-2. callApi 함수를 호출하여 결제정보 받아오기
            JSONObject info = callApi(jsonObject, type, uri);
            log.info("결제 정보 : " + info);
            JSONObject amount = (JSONObject) info.get("response");
            log.info("금액 정보 : " +amount.get("amount"));

            // 3. 포인트 충전
            // 3-1. 아임포트로부터 받아온 결제정보에서의 충전금액과 사용자가 입력한 충전금액이 일치하는지 화인
            if (amount.get("amount").toString().equals(dto.getAmount()+"")) {
                // 결제가 완료됐으면
                if (amount.get("status").toString().equals("paid")) {
                    log.info("user_id : " + dto.getUser_id());
                    log.info("status : " + amount.get("status").toString());
                    response.put("message", "일반 결제 성공");

                    // 3-2. DB에 정보 금액 저장
                    Optional<User> checkUser = userRepository.findById(dto.getUser_id());
                    User user = checkUser.orElse(null);

                    user.getPoint().chargePoint(dto.getAmount());

                    userRepository.save(user);

                    // 3-3. DB에 포인트 결제 내역 저장
                    OrderPoint orderPoint = OrderPoint.chargePoint(dto, user);
                    orderPointRepository.save(orderPoint);
                    user.getOrderPoint().add(orderPoint);
                }
            } else {
                response.put("message", "결제 실패");
                throw new IllegalArgumentException("위조된 결제시도");
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            // 주문 취소
            cancel(dto);
        }

        log.info("data : " + response.toString());
        return response;
    }

    // api 호출 메서드
    private static JSONObject callApi(JSONObject param, String type, String uri) {
        HttpURLConnection conn = null;
        JSONObject response = null;

        try {
            URL url = new URL(uri);
            conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod(type);
            conn.setRequestProperty("Content-Type", "application/json");
            if (param.containsKey("Authorization")) {
                conn.setRequestProperty("Authorization", param.get("Authorization").toString());
            }
            conn.setDoOutput(true);

            // body값 넘기기 (get 방식에서는 필요 없음)
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            bw.write(param.toJSONString());
            log.info("제이슨 타입 값 : " + param.toJSONString());
            bw.flush();
            bw.close();

            //보내고 결과값 받기
            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();

                // 받아온 값을 JSON형태로 파싱
                JSONParser parser = new JSONParser();
                response = (JSONObject) parser.parse(sb.toString());
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return response;
    }

    // 포인트 결제 내역 조회
    public List<PointDto> getPointList(Long user_id) {
        Optional<User> checkUser = userRepository.findById(user_id);
        User user = checkUser.orElse(null);

        List<OrderPoint> pointList = user.getOrderPoint();

        return pointList.stream().map(orderPoint -> PointDto.createDto(orderPoint))
                .collect(Collectors.toList());
    }

    // 포인트 환불
    public String cancel(PointDto dto) {
        // 유저 유효성 검사
        Optional<User> checkUser = userRepository.findById(dto.getUser_id());
        User user = checkUser.orElse(null);
        // 금액이 맞지 않을 경우 환불 불가
        if (user.getPoint().getTotalPoint() < dto.getCancel_amount()) {
            throw new IllegalArgumentException("금액이 부족하여 환불하실 수 없습니다.");
        }
        // 아임포트 서버로 보낼 JSONObject 생성
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("imp_key", apiConfig.getIamportKey());
        jsonObject.put("imp_secret", apiConfig.getIamportSecret());
        String uri = "https://api.iamport.kr/users/getToken";    // 토큰 요청 uri
        String type = "POST";   // http 요청 방식

        // 결제 환불 시 로직
        if (dto.getPoint_id() != null) {
            // 1. 결제번호, 유저정보, 환불가능한 금액 유효성 검사
            OrderPoint orderPoint = orderPointRepository.findById(dto.getPoint_id())
                    .orElseThrow(() -> new IllegalArgumentException("해당 주문번호는 존재하지 않습니다."));
            // 1-1. orderPoint 엔티티를 환불 Dto로 변환
            PointDto cancelDto = PointDto.cancelDto(orderPoint);
            // 1-2. 이미 환불한 주문이면 다시 환불 불가
            if (orderPoint.getCancel_amount() != 0) {
                throw new IllegalArgumentException("이미 환불된 주문입니다.");
            }

            // 2. 액세스 토큰 발급
            // 2-1. callApi 함수를 호출하여 token값 받아오기
            JSONObject token = callApi(jsonObject, type, uri);
            JSONObject realToken = (JSONObject) token.get("response");
            log.info("access 토큰 : " + realToken.get("access_token"));

            // 3. 액세스 토큰으로 아임포트 서버로부터 결제정보 받아오기
            uri = "https://api.iamport.kr/payments/" + cancelDto.getImp_uid();
            type = "GET";
            jsonObject.put("Authorization", realToken.get("access_token"));
            // 3-1. callApi 함수를 호출하여 결제정보 받아오기
            JSONObject info = callApi(jsonObject, type, uri);
            log.info("결제 정보 : " + info);
            JSONObject amount = (JSONObject) info.get("response");
            log.info("금액 정보 : " +amount.get("amount"));

            // 4. 아임포트 서버로 결제환불 요청
            // 4-1. 받아온 주문 정보에서의 금액과 유저가 요청한 금액이 맞는지 확인
            if (amount.get("amount").toString().equals(cancelDto.getCancel_amount()+"")) {
                uri = "https://api.iamport.kr/payments/cancel";
                type = "POST";
                jsonObject.put("imp_uid", cancelDto.getImp_uid());
                jsonObject.put("amount", cancelDto.getCancel_amount());
                jsonObject.put("checksum", cancelDto.getCancel_amount());

                JSONObject responseObj = callApi(jsonObject, type, uri);
                JSONObject response = (JSONObject) responseObj.get("response");
                log.info("환불 응답 : " + responseObj.toString());
                // 환불 성공
                if (response.get("status").toString().equals("cancelled")) {
                    OrderPoint op = OrderPoint.chargePoint(cancelDto, user);
                    orderPointRepository.delete(orderPoint);
                    orderPointRepository.save(op);
                    user.getOrderPoint().add(op);
                } else {
                    throw new IllegalArgumentException("환불 실패");
                }
            } else {
                throw new IllegalArgumentException("금액이 맞지 않습니다.");
            }
        } else {   // 포인트 충전 중 에러발생으로 인한 결제 취소 로직
            log.info("취소 로직으로 들어오는가");
            // 액세스 토큰 발급
            JSONObject token = callApi(jsonObject, type, uri);
            JSONObject realToken = (JSONObject) token.get("response");
            log.info("access 토큰 : " + realToken.get("access_token"));
            jsonObject.put("Authorization", realToken.get("access_token"));

            // 아임포트 서버로 취소 요청
            uri = "https://api.iamport.kr/payments/cancel";
            type = "POST";
            jsonObject.put("imp_uid", dto.getImp_uid());
            jsonObject.put("amount", dto.getAmount());
            jsonObject.put("checksum", dto.getAmount());

            JSONObject responseObj = callApi(jsonObject, type, uri);
            JSONObject response = (JSONObject) responseObj.get("response");
            log.info("취소 응답 : " + responseObj.toString());
            // 취소 실패
            if (!response.get("status").toString().equals("cancelled")) {
                throw new IllegalArgumentException("취소 실패");
            }
            return "취소 성공";
        }



        return "환불 성공";
    }
}
