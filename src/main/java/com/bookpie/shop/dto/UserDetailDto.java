package com.bookpie.shop.dto;

import com.bookpie.shop.domain.Address;
import com.bookpie.shop.domain.Point;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.enums.Grade;
import com.bookpie.shop.enums.LoginType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDetailDto {
    private Long id;
    private String email;
    private String nickName;
    private String name;
    private String phone;
    private LoginType loginType;
    private Grade grade;
    private float rating;
    private Address address;
    private Point point;
    private String image;
    private LocalDateTime createDate;
    public static UserDetailDto createUserDetailDto(User user){
        UserDetailDto dto = new UserDetailDto();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setNickName(user.getNickName());
        dto.setName(user.getName());
        dto.setPhone(user.getPhone());
        dto.setGrade(user.getGrade());
        dto.setAddress(user.getAddress());
        dto.setPoint(user.getPoint());
        dto.setCreateDate(user.getCreateDate());
        dto.setImage(user.getImage());
        dto.setLoginType(user.getLoginType());
        dto.setRating(user.getRating());
        return dto;
    }

}
