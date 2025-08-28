package com.market.market_place.members.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberUpdateRequest {

    private String name;

    // TODO: 추후 프로필 이미지 변경 등 다른 필드 추가 가능
    // private MultipartFile profileImage;
}
