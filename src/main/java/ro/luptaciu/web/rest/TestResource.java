package ro.luptaciu.web.rest;

import com.codahale.metrics.annotation.Timed;
import ro.luptaciu.service.TestService;
import ro.luptaciu.service.dto.QuestionDTO;
import ro.luptaciu.web.rest.util.HeaderUtil;
import ro.luptaciu.service.dto.TestDTO;
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
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * REST controller for managing Test.
 */
@RestController
@RequestMapping("/api")
public class TestResource {

    private final Logger log = LoggerFactory.getLogger(TestResource.class);

    @Inject
    private TestService testService;

    /**
     * POST  /tests : Create a new test.
     *
     * @param testDTO the testDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new testDTO, or with status 400 (Bad Request) if the test has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tests",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TestDTO> createTest(@RequestBody TestDTO testDTO) throws URISyntaxException {
        log.debug("REST request to save Test : {}", testDTO);
        if (testDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("test", "idexists", "A new test cannot already have an ID")).body(null);
        }
        TestDTO result = testService.save(testDTO);
        return ResponseEntity.created(new URI("/api/tests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("test", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /tests : Updates an existing test.
     *
     * @param testDTO the testDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated testDTO,
     * or with status 400 (Bad Request) if the testDTO is not valid,
     * or with status 500 (Internal Server Error) if the testDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/tests",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TestDTO> updateTest(@RequestBody TestDTO testDTO) throws URISyntaxException {
        log.debug("REST request to update Test : {}", testDTO);
        if (testDTO.getId() == null) {
            return createTest(testDTO);
        }
        TestDTO result = testService.save(testDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("test", testDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /tests : get all the tests.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of tests in body
     */
    @RequestMapping(value = "/tests",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<TestDTO> getAllTests() {
        log.debug("REST request to get all Tests");
        return testService.findAll();
    }

    /**
     * GET  /tests/:id : get the "id" test.
     *
     * @param id the id of the testDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the testDTO, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/tests/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<TestDTO> getTest(@PathVariable Long id) {
        log.debug("REST request to get Test : {}", id);
        TestDTO testDTO = testService.findOne(id);
        return Optional.ofNullable(testDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /tests/:id : delete the "id" test.
     *
     * @param id the id of the testDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/tests/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteTest(@PathVariable Long id) {
        log.debug("REST request to delete Test : {}", id);
        testService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("test", id.toString())).build();
    }


    @RequestMapping(value = "/startTest",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<QuestionDTO> getAllTests(@RequestParam(required = false) String code) {
        log.debug("REST request to get all quesetion for code" + code);

        return testService.findTestByCode(code);
    }

    @RequestMapping(value = "/displayTest",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public String submitTest(@RequestBody(required = false) List<QuestionDTO> questions,@RequestParam(required = false) String code) {
        log.debug("REST request to get all quesetion for code" + questions);

        return "{\"right\":" + "\""+testService.calculateResult(questions,code)+"\"}";

    }
}
