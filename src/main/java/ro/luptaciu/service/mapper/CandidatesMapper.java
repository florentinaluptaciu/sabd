package ro.luptaciu.service.mapper;

import ro.luptaciu.domain.*;
import ro.luptaciu.service.dto.CandidatesDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Candidates and its DTO CandidatesDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CandidatesMapper {

    CandidatesDTO candidatesToCandidatesDTO(Candidates candidates);

    List<CandidatesDTO> candidatesToCandidatesDTOs(List<Candidates> candidates);

    Candidates candidatesDTOToCandidates(CandidatesDTO candidatesDTO);

    List<Candidates> candidatesDTOsToCandidates(List<CandidatesDTO> candidatesDTOs);
}
