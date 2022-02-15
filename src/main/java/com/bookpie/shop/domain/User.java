package com.bookpie.shop.domain;

import com.bookpie.shop.domain.dto.UserCreateDto;
import com.bookpie.shop.domain.dto.UserUpdateDto;
import com.bookpie.shop.domain.enums.Grade;
import com.bookpie.shop.domain.enums.LoginType;
import com.bookpie.shop.domain.enums.Role;
import lombok.*;
import org.hibernate.annotations.Formula;
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
@ToString(exclude = "boards")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements UserDetails {

    @Id @GeneratedValue
    @Column(name = "user_id")
    private Long id;



    @Column(nullable = false,unique = true)
    private String email;

    private String password;
    private String name;
    private String phone;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    private String nickName;

    private LocalDateTime createDate;
    @Embedded private Address address;
    private float rating;
    private String withDraw;

    @Enumerated(EnumType.STRING)
    private Grade grade;
    @Embedded private Point point;

    private String image;

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

    @OneToMany(mappedBy = "user")
    private List<OrderPoint> orderPoint = new ArrayList<>();

    @Formula("(select count(1) from user_review r where r.order_id in (select o.id from orders o join used_book u on o.book_id = u.used_id where u.user_id = user_id))")
    private int reviewCount;

    @Basic(fetch = FetchType.LAZY)
    @Formula("(select count(1) from used_book ub where ub.user_id = user_id)")
    private int bookCount;

    @ElementCollection(fetch = FetchType.LAZY)
    @Enumerated(EnumType.STRING)
    private List<Role> roles = new ArrayList<>();

    @Builder
    public User(String name,String password,String nickName,Role role,LocalDateTime createDate,
                Grade grade,Point point,LoginType loginType,String phone,Address address,String email){
        this.name = name;
        this.password = password;
        this.nickName = nickName;
        this.roles = Collections.singletonList(role);
        this.createDate = createDate;
        this.grade = grade;
        this.point = point;
        this.loginType = loginType;
        this.phone = phone;
        this.address = address;
        this.email = email;
    }

    public static User oauthCreate(String email,String name,LoginType type){
        return User.builder()
                .email(email)
                .name(name)
                .loginType(type)
                .nickName(type.toString()+" "+name)
                .role(Role.ROLE_USER)
                .createDate(LocalDateTime.now())
                .point(Point.createDefaultPoint())
                .grade(Grade.GENERAL)
                .build();

    }


    
    public static User createUser(UserCreateDto userCreateDto){
        return User.builder()
                .name(userCreateDto.getName())
                .nickName(userCreateDto.getNickName())
                .email(userCreateDto.getEmail())
                .address(userCreateDto.getAddress())
                .createDate(LocalDateTime.now())
                .grade(Grade.GENERAL)
                .loginType(LoginType.LOCAL)
                .point(Point.createDefaultPoint())
                .role(Role.ROLE_USER)
                .phone(userCreateDto.getPhone())
                .password(userCreateDto.getPassword())
                .build();

    }

    public void addOrder(Order order){
        this.orders.add(order);
    }

    public void update(UserUpdateDto userUpdateDto){
        this.name = userUpdateDto.getName();
        this.phone = userUpdateDto.getPhone();
        this.address = userUpdateDto.getAddress();
    }

    public void deleteAccount(String reason){
        this.withDraw = reason;
        this.grade = Grade.WITH_DRAW;
    }

    public void addRating(float rating){
        this.rating = ( (this.rating*this.reviewCount) + rating)/(reviewCount+1);
    }

    public void removeRating(float rating){
        this.rating = ( (this.rating*this.reviewCount) - rating)/(reviewCount-1);
    }

    public void fixRating(float preRating,float updateRating){
        this.rating = ((this.rating*this.reviewCount)-preRating+updateRating)/reviewCount;
    }
    public void changeNickname(String nickName){
        this.nickName = nickName;
    }

    public void changeImage(String imageName) {
        this.image = imageName;
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
        return this.email;
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
