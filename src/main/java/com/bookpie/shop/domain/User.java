package com.bookpie.shop.domain;

import com.bookpie.shop.domain.dto.UserCreateDto;
import com.bookpie.shop.domain.enums.Grade;
import com.bookpie.shop.domain.enums.Role;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class User {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String email;
    private String password;
    private String name;
    private String phone;
    private String nickName;
    private LocalDateTime createDate;
    @Embedded private Address address;
    private Role role;
    private float rating;
    private String image;
    private String withDraw;
    private Grade grade;
    @Embedded private Point point;

    @OneToMany(mappedBy = "buyer")
    private List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "seller")
    private List<UsedBook> uploadBooks = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Board> boards = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Reply> replies = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<BookReview> bookReviews = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UsedBookLike> likes = new ArrayList<>();




    public static User createUser(UserCreateDto userCreateDto){
        User user = new User();
        user.name = userCreateDto.getName();
        user.address = userCreateDto.getAddress();
        user.email = userCreateDto.getEmail();
        user.password = userCreateDto.getPassword();
        user.phone = userCreateDto.getPhone();
        user.nickName = userCreateDto.getNickName();
        user.point = Point.createDefaultPoint();
        return user;
    }


}
