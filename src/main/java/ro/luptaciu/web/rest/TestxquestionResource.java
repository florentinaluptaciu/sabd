package ro.luptaciu.web.rest;

import com.codahale.metrics.annotation.Timed;
import ro.luptaciu.domain.Testxquestion;

import ro.luptaciu.repository.TestxquestionRepository;
import ro.luptaciu.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Testxquestion.
 */
@RestController
@RequestMapping("/api")
public class TestxquestionResource {

    private final Logger log = LoggerFactory.getLogger(TestxquestionResource.class);
        
    @Inject
    private TestxquestionRepository testxquestionRepository;

    /**
     * POST  /testxquestions : Create a new testxquestion.
     *
     * @param testxquestion the testxquestion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new testxquestion, or with status 400 (Bad Request) if the testxquestion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/testxquestions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Testxquestion> createTestxquestion(@RequestBody Testxquestion testxquestion) throws URISyntaxException {
        log.debug("REST request to save Testxquestion : {}", testxquestion);
        if (testxquestion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("testxquestion", "idexists", "A new testxquestion cannot already have an ID")).body(null);
        }
        Testxquestion result = testxquestionRepository.save(testxquestion);
        return ResponseEntity.created(new URI("/api/testxquestions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("testxquestion", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /testxquestions : Updates an existing testxquestion.
     *
     * @param testxquestion the testxquestion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated testxquestion,
     * or with status 400 (Bad Request) if the testxquestion is not valid,
     * or with status 500 (Internal Server Error) if the testxquestion couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/testxquestions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Testxquestion> updateTestxquestion(@RequestBody Testxquestion testxquestion) throws URISyntaxException {
        log.debug("REST request to update Testxquestion : {}", testxquestion);
        if (testxquestion.getId() == null) {
            return createTestxquestion(testxquestion);
        }
        Testxquestion result = testxquestionRepository.save(testxquestion);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("testxquestion", testxquestion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /testxquestions : get all the testxquestions.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of testxquestions in body
     */
    @RequestMapping(value = "/testxquestions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Testxquestion> getAllTestxquestions() {
        log.debug("REST request to get all Testxquestions");
        List<Testxquestion> testxquestions = testxquestionRepository.findAll();
        return testxquestions;
    }

    /**
     * GET  /testxquestions/:id : get the "id" testxquestion.
     *
     * @param id the id of the testxquestion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the testxquestion, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/testxquestions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Testxquestion> getTestxquestion(@PathVariable Long id) {
        log.debug("REST request to get Testxquestion : {}", id);
        Testxquestion testxquestion = testxquestionRepository.findOne(id);
        return Optional.ofNullable(testxquestion)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /testxquestions/:id : delete the "id" testxquestion.
     *
     * @param id the id of the testxquestion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/testxquestions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTestxquestion(@PathVariable Long id) {
        log.debug("REST request to delete Testxquestion : {}", id);
        testxquestionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("testxquestion", id.toString())).build();
    }

}
