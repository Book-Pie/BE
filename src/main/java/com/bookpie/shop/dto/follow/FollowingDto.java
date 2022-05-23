package com.bookpie.shop.dto.follow;

import com.bookpie.shop.domain.Follow;
import com.bookpie.shop.dto.UsedBookListDto;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FollowingDto {
    private Long followId;
    private Long userId;    //팔로우 당한 사람
    private String profile;   //팔로우 당한 사람 프로필 이미지
    private String nickName;
    private List<UsedBookListDto> usedBookList;
    private Boolean followCheck;

    public static FollowingDto createDto(Follow follow, Long currentUserId) {
        return FollowingDto.builder()
                .followId(follow.getId())
                .userId(follow.getToUser().getId())
                .profile(follow.getToUser().getImage())
                .nickName(follow.getToUser().getNickName())
                .usedBookList(follow.getToUser().getUploadBooks().stream()
                        .map(usedBook -> new UsedBookListDto(usedBook)).collect(Collectors.toList()))
                .followCheck(followCheckMethod(follow.getToUser().getFollowers(), currentUserId))
                .build();
    }

    private static Boolean followCheckMethod(List<Follow> follower, Long currentUserId) {
        Boolean check = false;
        if (currentUserId.equals(0L)) return false;
        for (Follow follow : follower) {
            if (follow.getFromUser().getId().equals(currentUserId)) check=true;
        }
        return check;
    }

}
