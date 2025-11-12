package markit.moderation.sanction.community_sanction;// package markit.community.sanction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CommunitySanctionRepository extends JpaRepository<CommunitySanction, Long> {

    // 특정 신고에 대해 이미 제재가 존재하는지 확인(중복 생성 방지)
    boolean existsByReport_Id(Long reportId);

    // 특정 회원의 제재 이력 중 가장 최근(가장 큰 id 기준) 제재 1건 조회
    // 누적 제재 횟수 확인 및 다음 제재 수위 계산에 사용
    Optional<CommunitySanction> findFirstByMember_IdOrderByIdDesc(Long memberId);

    // 특정 신고에 연결된 제재 1건 조회
    Optional<CommunitySanction> findByReport_Id(Long reportId);

    // 특정 회원의 "진행 중" 제재 목록 조회
    // 조건: active=true 이고, 종료 시각(endAt)이 now 이후(아직 안 끝난 상태)
    List<CommunitySanction> findByMember_IdAndActiveTrueAndEndAtAfter(Long memberId, LocalDateTime now);

    // 현재(now) 기준으로 이미 종료(endAt < now)된 "만료된" 제재 목록 조회
    // 만료된 제재를 비활성화 처리할 때 사용
    List<CommunitySanction> findByActiveTrueAndEndAtBefore(LocalDateTime now);

    // 특정 회원이 현재 "진행 중" 제재가 존재하는지 여부 확인
    // true 라면 해당 회원은 제재 상태
    boolean existsByMember_IdAndActiveTrueAndEndAtAfter(Long memberId, LocalDateTime now);

}
