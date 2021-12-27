package com.bookpie.shop.controller;

import com.bookpie.shop.domain.UsedBook;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.dto.UsedBookCreateDto;
import com.bookpie.shop.repository.UsedBookRepository;
import com.bookpie.shop.service.UsedBookService;
import com.bookpie.shop.utils.ApiUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.bookpie.shop.utils.ApiUtil.*;

@RestController
@RequestMapping("/api/usedbook")
@RequiredArgsConstructor
@Slf4j
public class UsedBookController {

    private final UsedBookService usedBookService;

    @PostMapping("")
    public ApiResult upload(@RequestPart("images")List<MultipartFile> images,
                            @RequestParam("usedBook")String request) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        UsedBookCreateDto usedBookCreateDto = objectMapper.readValue(request, new TypeReference<UsedBookCreateDto>() {});
        log.debug(usedBookCreateDto.toString());
        return success(usedBookService.uploadUsedBook(getCurrentUserId(),usedBookCreateDto,images));

    }
    @GetMapping("/{id}")
    public ApiResult getUsedBookDetail(@PathVariable("id")Long id){
        return success(usedBookService.getUsedBook(id));
    }

    @GetMapping("")
    public ApiResult getUsedBookList(@RequestParam(value = "page",required=false,defaultValue = "1") int page,
                                     @RequestParam(value = "offset",required = false,defaultValue = "20") int offset,
                                    @RequestParam(value = "sort",required = false,defaultValue = "date")String sort){
        return success(1);
    }

    private Long getCurrentUserId(){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return user.getId();
    }
}
