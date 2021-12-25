package com.bookpie.shop.domain.dto;

import com.bookpie.shop.domain.Tag;
import com.bookpie.shop.domain.enums.BookState;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class UsedBookCreateDto {
    private String title;
    private String content;
    private int price;
    private BookState state;
    private List<String> tags = new ArrayList<>();
}
