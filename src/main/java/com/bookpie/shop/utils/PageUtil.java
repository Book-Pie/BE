package com.bookpie.shop.utils;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

public class PageUtil {

    @Getter
    @Setter
    public static class PageDto<T> implements Serializable {
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
