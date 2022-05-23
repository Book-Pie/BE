package com.bookpie.shop.service;

import com.bookpie.shop.domain.Point;
import com.bookpie.shop.domain.UsedBook;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.dto.UsedBookCreateDto;
import com.bookpie.shop.enums.BookState;
import com.bookpie.shop.enums.SaleState;
import com.bookpie.shop.repository.TagRepository;
import com.bookpie.shop.repository.UsedBookRepository;
import com.bookpie.shop.repository.UserRepository;
import com.bookpie.shop.utils.FileUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UsedBookServiceTest {

    @InjectMocks
    private UsedBookService usedBookService;

    @Mock
    private UsedBookRepository usedBookRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private TagRepository tagRepository;


    @BeforeAll
    public void init() throws Exception {
        mockStatic(FileUtil.class);
        when(FileUtil.save(any(),any())).thenReturn("filename");
    }

    @Test
    public void usedbookUploadTest() throws Exception{
        //given

        User user = user(1l);
        List<MultipartFile> files = new ArrayList<>();
        UsedBookCreateDto dto = dto();
        when(userRepository.findById(any())).thenReturn(Optional.ofNullable(user));
        //when
        usedBookService.uploadUsedBook(user.getId(),dto,files);
        //then
        verify(usedBookRepository).save(any());
        verify(tagRepository).saveAll(any());

    }

    @Test
    public void usedbookDeleteTest() throws Exception{
        //given
        User user = user(1l);
        UsedBook usedBook = book(user,2l);

        //when
        usedBookService.delete(user.getId());

        //then
        verify(usedBookRepository).delete(user.getId());
    }

    @Test
    public void usedbookUpdateTest() throws Exception{
        //given
        User user = user(1l);
        UsedBookCreateDto dto = dto();
        UsedBook usedBook = book(user,2l);
        when(usedBookRepository.findByIdDetail(2l)).thenReturn(Optional.ofNullable(usedBook));
        List<MultipartFile> files = new ArrayList<>();
        //when
        usedBookService.updateUsedBook(1l,2l,dto,files);
        Exception permissionException = assertThrows(IllegalArgumentException.class, () -> usedBookService.updateUsedBook(3l, usedBook.getId(), dto, files));
        usedBook.soldout();
        Exception stateException = assertThrows(IllegalArgumentException.class,()->usedBookService.updateUsedBook(1l,usedBook.getId(),dto,files));
        //then

        assertEquals("수정 권한이 없습니다.",permissionException.getMessage());
        assertEquals("판매중이거나 판매완료 상품은 수정할 수 없습니다.",stateException.getMessage());
        verify(tagRepository).saveAll(any());
    }

    @Test
    public void increseViewCountTest() throws Exception{
        //given
        User user = user(1l);
        UsedBook usedBook = book(user,2l);
        when(usedBookRepository.findById(2l)).thenReturn(Optional.ofNullable(usedBook));
        //when
        usedBookService.increseViewCount(2l);
        //then
        assertEquals(1,usedBook.getView());
    }

    @Test
    public void updateModifiedDateTest() throws Exception{
        //given
        User user = user(1l);
        UsedBook usedBook = book(user,2l);
        when(usedBookRepository.findById(2l)).thenReturn(Optional.ofNullable(usedBook));
        LocalDateTime pre = usedBook.getModifiedDate();
        //when
        usedBookService.updateModifiedDate(2l);
        //then
        assertNotEquals(pre,usedBook.getModifiedDate());
    }

    UsedBookCreateDto dto(){
        List<String> tags = new ArrayList<>(List.of("tag1","tag2"));
        return UsedBookCreateDto
                .builder()
                .title("제목 수정")
                .content("책 팔아요~~")
                .price(1000)
                .tags(tags)
                .build();
    }

    User user(Long id){
        return User.builder()
                   .id(id)
                   .name("user"+ id)
                   .nickName("nick"+ id)
                   .point(Point.createDefaultPoint())
                   .email("user"+ id +"@gmail.com")
                   .build();
    }

    UsedBook book(User user, Long id) {
        return UsedBook.builder()
                       .id(id)
                       .price(1000)
                       .title("책 팝니다")
                       .seller(user)
                       .bookState(BookState.UNRELEASED)
                       .saleState(SaleState.SALE)
                       .build();
    }
}