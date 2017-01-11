package ro.luptaciu.repository;

import ro.luptaciu.domain.Test;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Test entity.
 */
@SuppressWarnings("unused")
public interface TestRepository extends JpaRepository<Test,Long> {

}
