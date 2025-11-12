package markit.members.repositories;

import markit.members.domain.Member;
import markit.members.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    // Member 객체로 리프레시 토큰을 찾는 메소드
    Optional<RefreshToken> findByMember(Member member);

    // Member ID로 리프레시 토큰을 삭제하는 메소드 (필요 시 사용)
    void deleteByMember(Member member);
}
