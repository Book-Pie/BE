package com.bookpie.shop.domain.dto;

import com.bookpie.shop.domain.Address;
import com.bookpie.shop.domain.Point;
import com.bookpie.shop.domain.User;
import com.bookpie.shop.domain.enums.Grade;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDetailDto {
    private Long id;
    private String username;
    private String email;
    private String nickName;
    private String name;
    private String phone;
    private Grade grade;
    private float rating;
    private Address address;
    private Point point;
    private String image;
    private LocalDateTime createDate;
    public static UserDetailDto createUserDetailDto(User user){
        UserDetailDto dto = new UserDetailDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setNickName(user.getNickName());
        dto.setName(user.getName());
        dto.setPhone(user.getPhone());
        dto.setGrade(user.getGrade());
        dto.setAddress(user.getAddress());
        dto.setPoint(user.getPoint());
        dto.setImage(user.getImage());
        dto.setCreateDate(user.getCreateDate());
        return dto;
    }

}
