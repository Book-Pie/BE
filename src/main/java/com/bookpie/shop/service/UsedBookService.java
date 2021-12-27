package com.bookpie.shop.service;

import com.bookpie.shop.domain.*;
import com.bookpie.shop.domain.dto.FindUsedBookDto;
import com.bookpie.shop.domain.dto.UsedBookCreateDto;
import com.bookpie.shop.domain.dto.UsedBookDto;
import com.bookpie.shop.domain.dto.UsedBookListDto;
import com.bookpie.shop.repository.TagRepository;
import com.bookpie.shop.repository.UsedBookRepository;
import com.bookpie.shop.repository.UserRepository;
import com.bookpie.shop.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
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

    public UsedBookDto getUsedBook(Long id){
        UsedBook usedBook = usedBookRepository.findById(id).orElseThrow(()->new IllegalArgumentException("등록된 책이 없습니다."));
        log.debug(usedBook.getImages().toString());
        UsedBookDto usedBookDto = UsedBookDto.createUsedBookDto(usedBook);
        return usedBookDto;
    }

    public List<UsedBookListDto> getUsedBookList(FindUsedBookDto findUsedBookDto){
        List<UsedBook> result = usedBookRepository.findAll(findUsedBookDto);
        log.debug(String.valueOf(result.size()));
        return result.stream().map(UsedBookListDto::new).collect(Collectors.toList());
    }

    public List<UsedBookListDto> getUserUpload(Long id){
        List<UsedBook> result = usedBookRepository.findByUserId(id);
        return result.stream().map(UsedBookListDto::new).collect(Collectors.toList());
    }

    @Transactional
    public boolean delete(Long id){
        return usedBookRepository.delete(id);
    }
}
