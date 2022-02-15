package com.bookpie.shop.service;

import com.bookpie.shop.domain.*;
import com.bookpie.shop.domain.dto.*;
import com.bookpie.shop.domain.enums.SaleState;
import com.bookpie.shop.repository.*;
import com.bookpie.shop.utils.FileUtil;
import com.bookpie.shop.utils.PageUtil.PageDto;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
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
    private final BookTagAndImageRepository bookTagAndImageRepository;
    private final BookReviewRepository bookReviewRepository;
    private final UsedBookLikeRepository usedBookLikeRepository;

    @Value("${path.image.dev}")
    private String filePath;

    // 중고도서 등록
    @Transactional
    public Long uploadUsedBook(Long id, UsedBookCreateDto dto,List<MultipartFile> files) throws Exception{
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

    //중고도서 수정
    @Transactional
    public UsedBook updateUsedBook(Long userId,Long bookId,UsedBookCreateDto dto,List<MultipartFile> files) throws Exception{
        UsedBook usedBook = usedBookRepository.findByIdDetail(bookId).orElseThrow(()-> new EntityNotFoundException("중고도서를 찾을 수 없습니다."));
        if (!usedBook.getSeller().getId().equals(userId)){
            throw new IllegalArgumentException("수정 권한이 없습니다.");
        }
        if(usedBook.getSaleState() != SaleState.SALE){
            throw new IllegalArgumentException("판매중이거나 판매완료 상품은 수정할 수 없습니다.");
        }
        usedBook.getImages().stream().forEach(i->{
            try {
                FileUtil.delete(filePath, i.getFileName());
                bookTagAndImageRepository.removeImage(i);
            }catch (Exception e){
                e.printStackTrace();
            }
        });
        usedBook.getTags().stream().forEach(t->bookTagAndImageRepository.removeBookTag(t));
        usedBook.initUpdate();
        usedBook.update(dto);
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

        for (String fileName : fileNames){
            log.debug(fileName);
            if (fileName != null){
                Image image = new Image(fileName);
                usedBook.addImage(image);
            }
        }
        if(!fileNames.isEmpty()) usedBook.setThumbnail(fileNames.get(0));
        return usedBook;
    }

    //중고도서 상세조회
    public UsedBookDto getUsedBook(Long bookId,Long userId){
        UsedBook usedBook = usedBookRepository.findByIdDetail(bookId).orElseThrow(()->new EntityNotFoundException("등록된 책이 없습니다."));
        log.debug(usedBook.getImages().toString());
        boolean liked = false;
        if (!userId.equals(0)){
            liked = usedBookLikeRepository.isLiked(bookId,userId);
        }
        List<JSONObject> categories= bookReviewRepository.myCategory(usedBook.getSeller().getId());
        UsedBookDto usedBookDto = UsedBookDto.createUsedBookDto(usedBook);
        usedBookDto.addSellerCategories(categories);
        usedBookDto.setLiked(liked);
        return usedBookDto;
    }

    //중고도서 검색
    public PageDto getUsedBookList(FindUsedBookDto findUsedBookDto){
        List<UsedBook> result = usedBookRepository.findAll(findUsedBookDto);
        Long total = 0l;
        if(findUsedBookDto.getPageCount() == 0){
            total = usedBookRepository.count(findUsedBookDto);
            findUsedBookDto.setPageCount(total);
        }
        Long pageCount =findUsedBookDto.getPageCount()/findUsedBookDto.getLimit();
        if (findUsedBookDto.getPageCount()% findUsedBookDto.getLimit() !=0){pageCount++;}
        log.debug(String.valueOf(result.size()));
        List<UsedBookListDto> collect = result.stream().map(UsedBookListDto::new).collect(Collectors.toList());
        return new PageDto(pageCount,total,collect);
    }

    //isbn으로 중고도서 검색
    public List<UsedBookListDto> getUsedBookListByIsbn(String isbn){
        List<UsedBook> books = usedBookRepository.findByIsbn(isbn);
        return books.stream().map(UsedBookListDto::new).collect(Collectors.toList());
    }

    //중고도서 삭제
    @Transactional
    public boolean delete(Long id){
        return usedBookRepository.delete(id);
    }

    //회원이 올린 중고도서
    public PageDto getUserUpload(Long userId,int offset,int limit){
        Long total = usedBookRepository.count(userId);
        Long pageCount = total/limit;
        if (total%limit != 0) pageCount++;
        List<UsedBook> result = usedBookRepository.findAllByUserId(userId, offset, limit);
        List<UsedBookListDto> collect = result.stream().map(UsedBookListDto::new).collect(Collectors.toList());
        return new PageDto(pageCount,total,collect);
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

    //중고도서 조회수 증가
    @Transactional
    public int increseViewCount(Long id){
        UsedBook usedBook = usedBookRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("등록된 중고도서가 없습니다."));
        usedBook.increaseView();
        return usedBook.getView();
    }

    //연관 중고도서 추천
    public List<UsedBookListDto> getRelatedUsedBook(RelatedUsedBookDto dto){
        List<Long> tags = dto.getTags().stream().map(t -> tagRepository.findByName(t))
                                .filter(tag -> tag.isPresent())
                                .map(t -> t.get().getId())
                                .collect(Collectors.toList());
        List<UsedBook> usedBooks = usedBookRepository.findRelated(dto.getCategory(), tags);
        return usedBooks.stream().map(UsedBookListDto::new).collect(Collectors.toList());
    }
}
