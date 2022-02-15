package com.bookpie.shop.utils;

import lombok.Getter;
import lombok.Setter;

public class PageUtil {

    @Getter
    @Setter
    public static class PageDto<T>{
        private Long pageCount;
        private Long totalElement;
        private T pages;

        public PageDto(){}

        public PageDto(Long count,Long totalElement,T data){
            this.pageCount = count;
            this.totalElement = totalElement;
            this.pages=data;
        }
    }


}
