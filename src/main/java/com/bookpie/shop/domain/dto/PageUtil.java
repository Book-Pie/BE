package com.bookpie.shop.domain.dto;

import lombok.Getter;
import lombok.Setter;

public class PageUtil {

    @Getter
    @Setter
    public static class PageDto<T>{
        private Long pageCount;
        private T pages;

        public PageDto(){}

        public PageDto(Long count,T data){
            this.pageCount = count;
            this.pages=data;
        }
    }


}
