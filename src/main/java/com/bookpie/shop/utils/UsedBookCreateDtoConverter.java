package com.bookpie.shop.utils;

import com.bookpie.shop.domain.dto.UsedBookCreateDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsedBookCreateDtoConverter implements Converter<String, UsedBookCreateDto>{

    private final ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public UsedBookCreateDto convert(String source) {
        return null;
    }
}
