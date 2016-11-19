package ro.luptaciu.web.rest;

import com.codahale.metrics.annotation.Timed;
import ro.luptaciu.service.CandidatesService;
import ro.luptaciu.web.rest.util.HeaderUtil;
import ro.luptaciu.web.rest.util.PaginationUtil;
import ro.luptaciu.service.dto.CandidatesDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Candidates.
 */
@RestController
@RequestMapping("/api")
public class CandidatesResource {

    private final Logger log = LoggerFactory.getLogger(CandidatesResource.class);
        
    @Inject
    private CandidatesService candidatesService;

    /**
     * POST  /candidates : Create a new candidates.
     *
     * @param candidatesDTO the candidatesDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new candidatesDTO, or with status 400 (Bad Request) if the candidates has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/candidates",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CandidatesDTO> createCandidates(@RequestBody CandidatesDTO candidatesDTO) throws URISyntaxException {
        log.debug("REST request to save Candidates : {}", candidatesDTO);
        if (candidatesDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("candidates", "idexists", "A new candidates cannot already have an ID")).body(null);
        }
        CandidatesDTO result = candidatesService.save(candidatesDTO);
        return ResponseEntity.created(new URI("/api/candidates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("candidates", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /candidates : Updates an existing candidates.
     *
     * @param candidatesDTO the candidatesDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated candidatesDTO,
     * or with status 400 (Bad Request) if the candidatesDTO is not valid,
     * or with status 500 (Internal Server Error) if the candidatesDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/candidates",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CandidatesDTO> updateCandidates(@RequestBody CandidatesDTO candidatesDTO) throws URISyntaxException {
        log.debug("REST request to update Candidates : {}", candidatesDTO);
        if (candidatesDTO.getId() == null) {
            return createCandidates(candidatesDTO);
        }
        CandidatesDTO result = candidatesService.save(candidatesDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("candidates", candidatesDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /candidates : get all the candidates.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of candidates in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/candidates",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CandidatesDTO>> getAllCandidates(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Candidates");
        Page<CandidatesDTO> page = candidatesService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/candidates");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /candidates/:id : get the "id" candidates.
     *
     * @param id the id of the candidatesDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the candidatesDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/candidates/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CandidatesDTO> getCandidates(@PathVariable Long id) {
        log.debug("REST request to get Candidates : {}", id);
        CandidatesDTO candidatesDTO = candidatesService.findOne(id);
        return Optional.ofNullable(candidatesDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /candidates/:id : delete the "id" candidates.
     *
     * @param id the id of the candidatesDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/candidates/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCandidates(@PathVariable Long id) {
        log.debug("REST request to delete Candidates : {}", id);
        candidatesService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("candidates", id.toString())).build();
    }

}
