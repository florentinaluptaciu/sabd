package ro.luptaciu.service;

import ro.luptaciu.domain.Candidates;
import ro.luptaciu.repository.CandidatesRepository;
import ro.luptaciu.service.dto.CandidatesDTO;
import ro.luptaciu.service.mapper.CandidatesMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Candidates.
 */
@Service
@Transactional
public class CandidatesService {

    private final Logger log = LoggerFactory.getLogger(CandidatesService.class);
    
    @Inject
    private CandidatesRepository candidatesRepository;

    @Inject
    private CandidatesMapper candidatesMapper;

    /**
     * Save a candidates.
     *
     * @param candidatesDTO the entity to save
     * @return the persisted entity
     */
    public CandidatesDTO save(CandidatesDTO candidatesDTO) {
        log.debug("Request to save Candidates : {}", candidatesDTO);
        Candidates candidates = candidatesMapper.candidatesDTOToCandidates(candidatesDTO);
        candidates = candidatesRepository.save(candidates);
        CandidatesDTO result = candidatesMapper.candidatesToCandidatesDTO(candidates);
        return result;
    }

    /**
     *  Get all the candidates.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<CandidatesDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Candidates");
        Page<Candidates> result = candidatesRepository.findAll(pageable);
        return result.map(candidates -> candidatesMapper.candidatesToCandidatesDTO(candidates));
    }

    /**
     *  Get one candidates by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public CandidatesDTO findOne(Long id) {
        log.debug("Request to get Candidates : {}", id);
        Candidates candidates = candidatesRepository.findOne(id);
        CandidatesDTO candidatesDTO = candidatesMapper.candidatesToCandidatesDTO(candidates);
        return candidatesDTO;
    }

    /**
     *  Delete the  candidates by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Candidates : {}", id);
        candidatesRepository.delete(id);
    }
}
