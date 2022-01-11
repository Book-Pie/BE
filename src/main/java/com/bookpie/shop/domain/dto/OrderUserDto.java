package com.bookpie.shop.domain.dto;


import com.bookpie.shop.domain.Address;
import com.bookpie.shop.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
public class OrderUserDto {
    private Long userId;
    private String nickName;
    private Address address;

    @Builder
    public OrderUserDto(User user){
        this.userId = user.getId();
        this.nickName = user.getNickName();
        this.address = user.getAddress();
    }
}
