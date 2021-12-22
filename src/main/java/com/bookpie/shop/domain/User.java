package com.bookpie.shop.domain;

import com.bookpie.shop.domain.dto.UserCreateDto;
import com.bookpie.shop.domain.dto.UserUpdateDto;
import com.bookpie.shop.domain.enums.Grade;
import com.bookpie.shop.domain.enums.Role;
import lombok.Builder;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
public class User implements UserDetails {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;

    private String username;
    private String email;
    private String password;
    private String name;
    private String phone;
    private String nickName;
    private LocalDateTime createDate;
    @Embedded private Address address;
    private float rating;
    private String image;
    private String withDraw;

    @Enumerated(EnumType.STRING) private Grade grade;
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

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private List<Role> roles = new ArrayList<>();

    
    public static User createUser(UserCreateDto userCreateDto){
        User user = new User();
        user.username = userCreateDto.getUsername();
        user.name = userCreateDto.getName();
        user.address = userCreateDto.getAddress();
        user.email = userCreateDto.getEmail();
        user.password = userCreateDto.getPassword();
        user.phone = userCreateDto.getPhone();
        user.nickName = userCreateDto.getNickName();
        user.point = Point.createDefaultPoint();
        user.roles = Collections.singletonList(Role.ROLE_USER);
        user.createDate = LocalDateTime.now();
        user.grade = Grade.GENERAL;
        return user;
    }

    public void update(UserUpdateDto userUpdateDto){
        this.name = userUpdateDto.getName();
        this.phone = userUpdateDto.getPhone();
        this.email = userUpdateDto.getEmail();
        this.address = userUpdateDto.getAddress();
    }

    public void deleteAccount(String reason){
        this.withDraw = reason;
        this.grade = Grade.WITH_DRAW;
    }
    public void changeNickname(String nickName){
        this.nickName = nickName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map((Role role) -> new SimpleGrantedAuthority(role.toString()))
                .collect(Collectors.toList());
    }
    public void changePassword(String password){this.password = password;}

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
