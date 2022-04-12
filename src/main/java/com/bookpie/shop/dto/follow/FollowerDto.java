package com.bookpie.shop.dto.follow;

import com.bookpie.shop.domain.Follow;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FollowerDto {
    private Long followId;
    private Long userId;
    private String nickName;
    private String profile;
    private Boolean followCheck;

    public static FollowerDto createDto(Follow follow, Long currentUserId) {
        return FollowerDto.builder()
                .followId(follow.getId())
                .userId(follow.getFromUser().getId())
                .nickName(follow.getFromUser().getNickName())
                .profile(follow.getFromUser().getImage())
                .followCheck(followCheckMethod(follow.getFromUser().getFollowers(), currentUserId))
                .build();
    }

    private static Boolean followCheckMethod(List<Follow> list, Long currentUserId) {
        if (currentUserId.equals(0L)) return false;
        for (Follow follow : list) {
            if (follow.getFromUser().getId().equals(currentUserId)) {
                return true;
            }
        }
        return false;
    }

}
