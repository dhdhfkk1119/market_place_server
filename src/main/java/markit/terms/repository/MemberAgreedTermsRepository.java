package markit.terms.repository;

import markit.terms.domain.MemberAgreedTerms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAgreedTermsRepository extends JpaRepository<MemberAgreedTerms, Long> {
}
