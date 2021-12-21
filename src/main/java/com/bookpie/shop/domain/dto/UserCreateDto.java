package com.bookpie.shop.domain.dto;

import com.bookpie.shop.domain.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {
    private String username;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String nickName;
    private Address address;

}
