package com.bookpie.shop.domain.dto;

import com.bookpie.shop.domain.enums.Category;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RelatedUsedBookDto {
    private Category category;
    private List<String> tags = new ArrayList<>();
}
