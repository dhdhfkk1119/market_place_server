package com.market.market_place.members.dtos;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MemberUpdateRequest {

    private String name;

    // Base64 인코딩된 프로필 이미지 문자열 (최대 길이는 700만 자 = 5MB)
    @Size(max = 7000000, message = "이미지 파일 용량은 5MB를 초과할 수 없습니다.")
    private String profileImage;
}
