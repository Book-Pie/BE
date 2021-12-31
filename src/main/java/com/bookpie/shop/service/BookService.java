package com.bookpie.shop.service;

import com.bookpie.shop.domain.BookCategory;
import com.bookpie.shop.domain.dto.book.BookCategoryDto;
import com.bookpie.shop.repository.BookCategoryRepository;
import com.bookpie.shop.repository.BookRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@Slf4j
@Transactional
public class BookService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookCategoryRepository bookCategoryRepository;

//    public BookDto getOne(String keyword) {
//
//    }

    public String searchPlace(String keyword) {
        try {
            keyword = URLEncoder.encode(keyword, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("encoding fail", e);
        }

        String apiURL = "https://openapi.naver.com/v1/search/book_adv.xml?=d_titl=나미야 잡화점&display=10&start=1";

        Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("X-Naver-Client-Id", "DTIcIOhVAuEwKPgeIab2");
        requestHeaders.put("X-Naver-Client-Secret", "5GjHzCVxDU");
        String responseBody = get(apiURL, requestHeaders);

        return responseBody;
    }

    private String get(String apiUrl, Map<String, String> requestHeaders) {
        HttpURLConnection con = connect(apiUrl);
        try {
            con.setRequestMethod("GET");
            for (Map.Entry<String, String> header : requestHeaders.entrySet()) {
                con.setRequestProperty(header.getKey(), header.getValue());
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {  // 정상 호출
                return readBody(con.getInputStream());
            } else {
                return readBody(con.getErrorStream());
            }
        } catch (IOException e) {
            throw new RuntimeException("API 요청과 응답 실패", e);
        } finally {
            con.disconnect();
        }
    }

    private HttpURLConnection connect(String apiUrl) {
        try {
            URL url = new URL(apiUrl);
            return (HttpURLConnection) url.openConnection();
        } catch (MalformedURLException e) {
            throw new RuntimeException("API URL이 잘못되었습니다. : " + apiUrl, e);
        } catch (IOException e) {
            throw new RuntimeException("연결이 실패했습니다. : " + apiUrl, e);
        }
    }

    private String readBody(InputStream body) {
        InputStreamReader streamReader = new InputStreamReader(body, StandardCharsets.UTF_8);

        try (
                BufferedReader lineReader = new BufferedReader(streamReader)
        ) {
            StringBuilder responseBody = new StringBuilder();

            String line;
            while ((line = lineReader.readLine()) != null) {
                responseBody.append(line);
            }

            return responseBody.toString();
        } catch (IOException e) {
            throw new RuntimeException("API 응답을 읽는데 실패했습니다.", e);
        }
    }

    // 카테고리 추가
    public String addCategory(BookCategoryDto dto) {

        if (dto.getParent_id() == null) {  // 부모 카테고리 추가
            BookCategory category = BookCategory.createParentCategory(dto);
            bookCategoryRepository.save(category);
        } else {  // 자식 카테고리 추가
            // 부모 카테고리
            BookCategory parentCategory = bookCategoryRepository.findById(dto.getParent_id())
                    .orElseThrow(() -> new IllegalArgumentException("해당 부모 카테고리는 존재하지 않습니다."));

            // 자식 카테고리
            BookCategory subCategory = BookCategory.createSubCategory(dto, parentCategory);
            bookCategoryRepository.save(subCategory);

            // 연관 관계 매핑 (자식태그 -> 부모태그)
            List<BookCategory> subList = parentCategory.getSubCategory();
            subList.add(subCategory);
        }

        return dto.getCategoryName() + " 카테고리 추가";
    }

    // 카테고리 삭제
    public String deleteCategory(Long category_id) {
        BookCategory bookCategory = bookCategoryRepository.findById(category_id)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리는 존재하지 않습니다."));

        bookCategoryRepository.delete(bookCategory);
        return bookCategory.getCategoryName() + " 카테고리가 삭제되었습니다.";
    }

    // 카테고리 조회
//    public List<BookCategoryDto> getAll() {
//        List<BookCategory> bookCategoryList = bookCategoryRepository.findAllCategory();
//
//        List<BookCategoryDto> dtos = new ArrayList<>();
//
//        for (BookCategory bookCategory : bookCategoryList) {
//            BookCategoryDto dto = BookCategoryDto.createDto(bookCategory);
//            log.info("여기 들어오나요");
//            // subCategory를 가진 bookCategory가 있는지 확인
//            if (bookCategory.getSubCategory().size() != 0) {
//                // 있으면 subCategory들도 Dto로 변환하여 dto리스트에 담는다.
//                for (BookCategory bookCategory1 : bookCategory.getSubCategory()) {
//                    dto.getSubCategory().add(BookCategoryDto.createDto(bookCategory1));
//                }
//            }
//            dtos.add(dto);
//        }
//        return dtos;
//    }

    public List<BookCategoryDto> getAll() {
        return bookCategoryRepository.findAllCategory()
                .stream().map(category -> BookCategoryDto.createDto(category))
                .collect(Collectors.toList());
    }
}

