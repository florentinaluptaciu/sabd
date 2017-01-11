package ro.luptaciu.repository;

import ro.luptaciu.domain.Testxquestion;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Testxquestion entity.
 */
@SuppressWarnings("unused")
public interface TestxquestionRepository extends JpaRepository<Testxquestion,Long> {

}
