package com.bookpie.shop.domain.dto;


import com.bookpie.shop.domain.Address;
import com.bookpie.shop.domain.Order;
import com.bookpie.shop.domain.User;
import lombok.Builder;
import lombok.Data;

@Data
public class OrderUserDto {
    private Long userId;
    private String nickName;
    private String name;
    private Address address;
    private String phone;
    @Builder
    public OrderUserDto(User user, Address address){
        this.userId = user.getId();
        this.nickName = user.getNickName();
        this.address = address;
        this.phone = user.getPhone();
        this.name = user.getName();
    }

    public OrderUserDto(User user){
        this.userId = user.getId();
        this.nickName = user.getNickName();
        this.address = user.getAddress();
        this.phone = user.getPhone();
        this.name = user.getName();
    }
}
