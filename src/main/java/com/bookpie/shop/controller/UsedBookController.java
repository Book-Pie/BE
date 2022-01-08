package com.bookpie.shop.controller;

import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.FindUsedBookDto;
import com.bookpie.shop.domain.dto.UsedBookCreateDto;
import com.bookpie.shop.domain.enums.Category;
import com.bookpie.shop.domain.enums.SaleState;
import com.bookpie.shop.repository.UsedBookRepository;
import com.bookpie.shop.service.UsedBookLikeService;
import com.bookpie.shop.service.UsedBookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.bookpie.shop.utils.ApiUtil.success;

@RestController
@RequestMapping("/api/usedbook")
@RequiredArgsConstructor
@Slf4j
public class UsedBookController {

    private final UsedBookService usedBookService;
    private final UsedBookLikeService usedBookLikeService;
    private final UsedBookRepository repository;
    //중고도서 등록
    @PostMapping("")
    public ResponseEntity upload(@RequestPart("images")List<MultipartFile> images,
                                 @Valid @RequestParam("usedBook")String request) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        UsedBookCreateDto usedBookCreateDto = objectMapper.readValue(request, new TypeReference<UsedBookCreateDto>() {});
        log.debug(usedBookCreateDto.toString());
        if(!StringUtils.hasText(usedBookCreateDto.getTitle())) throw new IllegalArgumentException("제목을 입력해주세요.");
        if(!StringUtils.hasText(usedBookCreateDto.getContent())) throw new IllegalArgumentException("내용을 입력해주세요.");
        if(usedBookCreateDto.getPrice() == 0) throw new IllegalArgumentException("가격을 입력해주세요");
        return new ResponseEntity(success(usedBookService.uploadUsedBook(getCurrentUserId(),usedBookCreateDto,images)), HttpStatus.OK);
    }
    //중고도서 상세정보 조회
    @GetMapping("/{id}")
    public ResponseEntity getUsedBookDetail(@PathVariable("id")Long id){
        return new ResponseEntity(success(usedBookService.getUsedBook(id)),HttpStatus.OK);
    }

    //중고도서 검색
    @GetMapping("")
    public ResponseEntity getUsedBookList(@RequestParam(value = "title",required = false,defaultValue = "") String title,
                                    @RequestParam(value = "page",required=false,defaultValue = "1") int page,
                                     @RequestParam(value = "limit",required = false,defaultValue = "20") int limit,
                                    @RequestParam(value = "sort",required = false,defaultValue = "date")String sort,
                                     @RequestParam(value = "first",required = false,defaultValue = "null")String first,
                                     @RequestParam(value = "second",required = false,defaultValue = "null")String second,
                                          @RequestParam(value = "pageCount",required = false,defaultValue = "0")Long pageCount){
        FindUsedBookDto findUsedBookDto = new FindUsedBookDto();
        findUsedBookDto.setTitle(title);
        findUsedBookDto.setLimit(limit);
        findUsedBookDto.setOffset((page*limit)-limit);
        findUsedBookDto.setFstCategory(Category.nameOf(first));
        findUsedBookDto.setSndCategory(Category.nameOf(second));
        findUsedBookDto.setSort(sort);
        findUsedBookDto.setPageCount(pageCount);
        log.debug(findUsedBookDto.toString());
        return new ResponseEntity(success(usedBookService.getUsedBookList(findUsedBookDto)),HttpStatus.OK);
    }

    //중고도서 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity deleteUsedBook(@PathVariable("id") Long id){
        return new ResponseEntity(success(usedBookService.delete(id)),HttpStatus.OK);
    }

    //회원이 올린 중고도서 검색
    @GetMapping("/user/{id}")
    public ResponseEntity getMyUpload(@PathVariable("id") Long id){
        return new ResponseEntity(success(usedBookService.getUserUpload(id)),HttpStatus.OK);
    }


    //좋아요
    @PostMapping("/like/{bookId}")
    public ResponseEntity updateLike(@PathVariable("bookId") Long bookId){
        return new ResponseEntity(success(usedBookLikeService.like(getCurrentUserId(),bookId)),HttpStatus.OK);
    }

    //내가 좋아요 한 목록
    @GetMapping("/like")
    public ResponseEntity getMyLike(){
       return new ResponseEntity(success(usedBookService.getUserLiked(getCurrentUserId())),HttpStatus.OK);
    }

    //전체 카테고리 조회
    @GetMapping("/category")
    public ResponseEntity getCategorys(){
        List<Category> parents = Category.AllParent();
        Map<String,List<String>> map = new HashMap<>();
        for(Category parent : parents){
            List<String> childrenKr = parent.children().stream().map(c -> c.getKr()).collect(Collectors.toList());
            map.put(parent.getKr(),childrenKr);
        }
        return new ResponseEntity(success(map),HttpStatus.OK);
    }

    //판매중,판매완료 책 개수
    @GetMapping("/groupcount")
    public ResponseEntity gruopCount() {
        Map<SaleState,Long> groupCount = usedBookService.getGroupCount();
        return new ResponseEntity(success(groupCount),HttpStatus.OK);
    }

    private Long getCurrentUserId(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }

}
