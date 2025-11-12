package markit.members.dto_profile;

import markit.members.domain.Member;
import markit.members.domain.MemberProfile;
import markit.members.domain.MemberStatus;
import markit.members.domain.Provider;
import lombok.Getter;

@Getter
public class MyInfoResponse {

    private final Long id;
    private final String loginId;
    private final String email;
    private final String name;
    private final String role;
    private final MemberStatus status;
    private final Provider provider; // 로그인 방식 추가
    private final String profileImageBase64;

    public MyInfoResponse(Member member) {
        this.id = member.getId();
        this.loginId = member.getLoginId();
        this.role = member.getRole().name();
        this.status = member.getStatus();
        this.email = member.getEmail();
        this.provider = member.getProvider(); // 로그인 방식 설정

        MemberProfile profile = member.getMemberProfile();
        if (profile != null) {
            this.name = profile.getName();
            this.profileImageBase64 = profile.getProfileImageBase64();
        } else {
            this.name = null;
            this.profileImageBase64 = null;
        }
    }
}
