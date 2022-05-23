package com.bookpie.shop.dto;

import com.bookpie.shop.enums.Category;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RelatedUsedBookDto {
    private Category category;
    private List<String> tags = new ArrayList<>();
}
