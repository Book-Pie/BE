package com.bookpie.shop.dto;

import com.bookpie.shop.enums.BookState;
import com.bookpie.shop.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsedBookCreateDto {
    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotBlank(message = "내용을 입력해주세요.")
    private String content;
    @NotNull(message = "가격을 입력해주세요.")
    private int price;
    private String isbn;
    private BookState state;
    private Category fstCategory;
    private Category sndCategory;
    private List<String> tags = new ArrayList<>();
}
