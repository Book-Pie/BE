package com.bookpie.shop.domain.dto;

import com.bookpie.shop.domain.Address;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.constraints.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateDto {
    //@NotBlank(message = "공백.")
    //@Size(min=4,max = 20,message = "아이디는 4글자 이상 20글자 이하여야 합니다.1")
    //private String username;

    @Email(message = "이메일 형식이 잘못되었습니다.")
    @NotBlank(message = "이메일 형식이 잘못되었습니다.")
    private String email;

    @NotBlank(message = "비밀번호에는 공백이 포함될 수 없습니다.")
    private String password;

    @NotBlank(message = "이름을 입력해주세요.")
    private String name;

    private String phone;
    private String nickName;
    private Address address;

}
