package com.bookpie.shop.service;

import com.bookpie.shop.domain.*;
import com.bookpie.shop.domain.dto.*;
import com.bookpie.shop.domain.enums.SaleState;
import com.bookpie.shop.repository.TagRepository;
import com.bookpie.shop.repository.UsedBookRepository;
import com.bookpie.shop.repository.UserRepository;
import com.bookpie.shop.utils.FileUtil;
import com.bookpie.shop.utils.PageUtil;
import com.bookpie.shop.utils.PageUtil.PageDto;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsedBookService {

    private final UsedBookRepository usedBookRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    @Value("${path.image.dev}")
    private String filePath;

    // 중고도서 등록
    @Transactional
    public Long uploadUsedBook(Long id, UsedBookCreateDto dto,List<MultipartFile> files){
        User user = userRepository.findById(id).get();
        UsedBook usedBook = UsedBook.createUsedBook(user,dto);
        List<Tag> tags = dto.getTags().stream().map(t -> new Tag(t)).collect(Collectors.toList());
        Set<Tag> tagSet = tagRepository.saveAll(tags);
        Set<BookTag> bookTags = tagSet.stream().map(t -> BookTag.createBookTag(t)).collect(Collectors.toSet());
        for(BookTag bookTag: bookTags){
            usedBook.addBookTag(bookTag);
        }
        List<String> fileNames = files.stream().map(file -> {
            try {
                return FileUtil.save(filePath, file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
        ;
        for (String fileName : fileNames){
            log.debug(fileName);
            if (fileName != null){
                Image image = new Image(fileName);
                usedBook.addImage(image);
            }
        }
        if(!fileNames.isEmpty()) usedBook.setThumbnail(fileNames.get(0));
        return usedBookRepository.save(usedBook);
    }


    //중고도서 상세조회
    public UsedBookDto getUsedBook(Long id){
        UsedBook usedBook = usedBookRepository.findByIdDetail(id).orElseThrow(()->new EntityNotFoundException("등록된 책이 없습니다."));
        log.debug(usedBook.getImages().toString());
        UsedBookDto usedBookDto = UsedBookDto.createUsedBookDto(usedBook);
        return usedBookDto;
    }

    //중고도서 검색
    public PageDto getUsedBookList(FindUsedBookDto findUsedBookDto){
        List<UsedBook> result = usedBookRepository.findAll(findUsedBookDto);
        if(findUsedBookDto.getPageCount() == 0){
            findUsedBookDto.setPageCount(usedBookRepository.count(findUsedBookDto));
        }
        Long pageCount =findUsedBookDto.getPageCount()/findUsedBookDto.getLimit();
        if (findUsedBookDto.getPageCount()% findUsedBookDto.getLimit() !=0){pageCount++;}
        log.debug(String.valueOf(result.size()));
        List<UsedBookListDto> collect = result.stream().map(UsedBookListDto::new).collect(Collectors.toList());
        return new PageDto(pageCount,collect);
    }



    //중고도서 삭제
    @Transactional
    public boolean delete(Long id){
        return usedBookRepository.delete(id);
    }

    //회원이 올린 중고도서
    public PageDto getUserUpload(Long userId,int offset,int limit,Long pageCount){
        if (pageCount == 0){
            Long total = usedBookRepository.count(userId);
            pageCount = total/limit;
            if (total%limit != 0) pageCount++;
        }
        List<UsedBook> result = usedBookRepository.findAllByUserId(userId, offset, limit);
        List<UsedBookListDto> collect = result.stream().map(UsedBookListDto::new).collect(Collectors.toList());
        return new PageDto(pageCount,collect);
    }

    //판매중,판매완료 책 개수
    public Map<SaleState,Long> getGroupCount(){
        List<Tuple> result = usedBookRepository.groupCount();
        Map<SaleState,Long> map = new HashMap<>();
        result.stream().forEach(res->map.put(res.get(0,SaleState.class),res.get(1,Long.class)));
        Arrays.stream(SaleState.values()).filter(state -> !map.containsKey(state)).forEach(state -> map.put(state, 0l));
        return map;
    }
    public List<UsedBookListDto> getUserLiked(Long id){
        List<UsedBook> result = usedBookRepository.getLikedBook(id);
        return result.stream().map(UsedBookListDto::new).collect(Collectors.toList());
    }

    //중고도서 끌어올리기 (up)
    @Transactional
    public LocalDateTime updateModifiedDate(Long id){
        UsedBook usedBook = usedBookRepository.findById(id).orElseThrow(()->new EntityNotFoundException("중고도서를 찾을 수 없습니다."));
        usedBook.updateModifiedDate();
        return usedBook.getModifiedDate();
    }

}
