package ro.luptaciu.repository;

import ro.luptaciu.domain.Question;

import org.springframework.data.jpa.repository.*;
import ro.luptaciu.domain.Subcategory;

import java.util.List;

/**
 * Spring Data JPA repository for the Question entity.
 */
@SuppressWarnings("unused")
public interface QuestionRepository extends JpaRepository<Question,Long> {

    List<Question> findBySubcategoryId(Long subcategoryId);

}
