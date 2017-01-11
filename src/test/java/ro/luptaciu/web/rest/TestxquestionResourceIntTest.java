package ro.luptaciu.web.rest;

import ro.luptaciu.InterviewApp;

import ro.luptaciu.domain.Testxquestion;
import ro.luptaciu.repository.TestxquestionRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the TestxquestionResource REST controller.
 *
 * @see TestxquestionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InterviewApp.class)
public class TestxquestionResourceIntTest {

    private static final Integer DEFAULT_ANSWER = 1;
    private static final Integer UPDATED_ANSWER = 2;

    @Inject
    private TestxquestionRepository testxquestionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restTestxquestionMockMvc;

    private Testxquestion testxquestion;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        TestxquestionResource testxquestionResource = new TestxquestionResource();
        ReflectionTestUtils.setField(testxquestionResource, "testxquestionRepository", testxquestionRepository);
        this.restTestxquestionMockMvc = MockMvcBuilders.standaloneSetup(testxquestionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Testxquestion createEntity(EntityManager em) {
        Testxquestion testxquestion = new Testxquestion()
                .answer(DEFAULT_ANSWER);
        return testxquestion;
    }

    @Before
    public void initTest() {
        testxquestion = createEntity(em);
    }

    @Test
    @Transactional
    public void createTestxquestion() throws Exception {
        int databaseSizeBeforeCreate = testxquestionRepository.findAll().size();

        // Create the Testxquestion

        restTestxquestionMockMvc.perform(post("/api/testxquestions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(testxquestion)))
                .andExpect(status().isCreated());

        // Validate the Testxquestion in the database
        List<Testxquestion> testxquestions = testxquestionRepository.findAll();
        assertThat(testxquestions).hasSize(databaseSizeBeforeCreate + 1);
        Testxquestion testTestxquestion = testxquestions.get(testxquestions.size() - 1);
        assertThat(testTestxquestion.getAnswer()).isEqualTo(DEFAULT_ANSWER);
    }

    @Test
    @Transactional
    public void getAllTestxquestions() throws Exception {
        // Initialize the database
        testxquestionRepository.saveAndFlush(testxquestion);

        // Get all the testxquestions
        restTestxquestionMockMvc.perform(get("/api/testxquestions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(testxquestion.getId().intValue())))
                .andExpect(jsonPath("$.[*].answer").value(hasItem(DEFAULT_ANSWER)));
    }

    @Test
    @Transactional
    public void getTestxquestion() throws Exception {
        // Initialize the database
        testxquestionRepository.saveAndFlush(testxquestion);

        // Get the testxquestion
        restTestxquestionMockMvc.perform(get("/api/testxquestions/{id}", testxquestion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(testxquestion.getId().intValue()))
            .andExpect(jsonPath("$.answer").value(DEFAULT_ANSWER));
    }

    @Test
    @Transactional
    public void getNonExistingTestxquestion() throws Exception {
        // Get the testxquestion
        restTestxquestionMockMvc.perform(get("/api/testxquestions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTestxquestion() throws Exception {
        // Initialize the database
        testxquestionRepository.saveAndFlush(testxquestion);
        int databaseSizeBeforeUpdate = testxquestionRepository.findAll().size();

        // Update the testxquestion
        Testxquestion updatedTestxquestion = testxquestionRepository.findOne(testxquestion.getId());
        updatedTestxquestion
                .answer(UPDATED_ANSWER);

        restTestxquestionMockMvc.perform(put("/api/testxquestions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedTestxquestion)))
                .andExpect(status().isOk());

        // Validate the Testxquestion in the database
        List<Testxquestion> testxquestions = testxquestionRepository.findAll();
        assertThat(testxquestions).hasSize(databaseSizeBeforeUpdate);
        Testxquestion testTestxquestion = testxquestions.get(testxquestions.size() - 1);
        assertThat(testTestxquestion.getAnswer()).isEqualTo(UPDATED_ANSWER);
    }

    @Test
    @Transactional
    public void deleteTestxquestion() throws Exception {
        // Initialize the database
        testxquestionRepository.saveAndFlush(testxquestion);
        int databaseSizeBeforeDelete = testxquestionRepository.findAll().size();

        // Get the testxquestion
        restTestxquestionMockMvc.perform(delete("/api/testxquestions/{id}", testxquestion.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Testxquestion> testxquestions = testxquestionRepository.findAll();
        assertThat(testxquestions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
