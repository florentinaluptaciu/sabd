package ro.luptaciu.repository;

import ro.luptaciu.domain.Question;
import ro.luptaciu.domain.Test;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Test entity.
 */
@SuppressWarnings("unused")
public interface TestRepository extends JpaRepository<Test,Long> {
    @Query(value = "select q.* from Question q join TestxQuestion txq on q.id = txq.question_id join Test t on t.id = txq.test_id where t.test_code = ?1", nativeQuery = true)
    List<Question> findQuestionsByCode(String code);
}
