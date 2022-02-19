package com.bookpie.shop.domain.dto.follow;

import com.bookpie.shop.domain.Follow;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FollowerDto {
    private Long followId;
    private Long userId;
    private String nickName;
    private String profile;

    public static FollowerDto createDto(Follow follow) {
        return FollowerDto.builder()
                .followId(follow.getId())
                .userId(follow.getFromUser().getId())
                .nickName(follow.getFromUser().getNickName())
                .profile(follow.getFromUser().getImage())
                .build();
    }

}
