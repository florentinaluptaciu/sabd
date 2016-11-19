package ro.luptaciu.repository;

import ro.luptaciu.domain.Candidates;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Candidates entity.
 */
@SuppressWarnings("unused")
public interface CandidatesRepository extends JpaRepository<Candidates,Long> {

}
