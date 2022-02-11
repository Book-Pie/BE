package com.bookpie.shop.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    // 구독을 하는 사람
    @JoinColumn(name = "from_user_id")
    @ManyToOne
    private User fromUser;

    // 구독을 당하는 사람
    @JoinColumn(name = "to_user_id")
    @ManyToOne
    private User toUser;

    @Builder
    public Follow(User fromUser, User toUser) {
        this.fromUser = fromUser;
        this.toUser = toUser;
    }

}
