package com.bookpie.shop.domain.dto.follow;

import com.bookpie.shop.domain.Follow;
import com.bookpie.shop.domain.dto.UsedBookListDto;
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

    public static FollowingDto createDto(Follow follow) {
        return FollowingDto.builder()
                .followId(follow.getId())
                .userId(follow.getToUser().getId())
                .profile(follow.getToUser().getImage())
                .nickName(follow.getToUser().getNickName())
                .usedBookList(follow.getToUser().getUploadBooks().stream()
                        .map(usedBook -> new UsedBookListDto(usedBook)).collect(Collectors.toList()))
                .build();
    }


}
