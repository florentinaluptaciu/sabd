package ro.luptaciu.web.rest;

import ro.luptaciu.InterviewApp;

import ro.luptaciu.domain.Candidates;
import ro.luptaciu.repository.CandidatesRepository;
import ro.luptaciu.service.CandidatesService;
import ro.luptaciu.service.dto.CandidatesDTO;
import ro.luptaciu.service.mapper.CandidatesMapper;

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
 * Test class for the CandidatesResource REST controller.
 *
 * @see CandidatesResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InterviewApp.class)
public class CandidatesResourceIntTest {

    private static final String DEFAULT_FIRST_NAME = "AAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBB";

    private static final String DEFAULT_EMAIL = "AAAAA";
    private static final String UPDATED_EMAIL = "BBBBB";

    private static final String DEFAULT_PHONE_NUMBER = "AAAAA";
    private static final String UPDATED_PHONE_NUMBER = "BBBBB";

    @Inject
    private CandidatesRepository candidatesRepository;

    @Inject
    private CandidatesMapper candidatesMapper;

    @Inject
    private CandidatesService candidatesService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCandidatesMockMvc;

    private Candidates candidates;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CandidatesResource candidatesResource = new CandidatesResource();
        ReflectionTestUtils.setField(candidatesResource, "candidatesService", candidatesService);
        this.restCandidatesMockMvc = MockMvcBuilders.standaloneSetup(candidatesResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Candidates createEntity(EntityManager em) {
        Candidates candidates = new Candidates()
                .firstName(DEFAULT_FIRST_NAME)
                .lastName(DEFAULT_LAST_NAME)
                .email(DEFAULT_EMAIL)
                .phoneNumber(DEFAULT_PHONE_NUMBER);
        return candidates;
    }

    @Before
    public void initTest() {
        candidates = createEntity(em);
    }

    @Test
    @Transactional
    public void createCandidates() throws Exception {
        int databaseSizeBeforeCreate = candidatesRepository.findAll().size();

        // Create the Candidates
        CandidatesDTO candidatesDTO = candidatesMapper.candidatesToCandidatesDTO(candidates);

        restCandidatesMockMvc.perform(post("/api/candidates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(candidatesDTO)))
                .andExpect(status().isCreated());

        // Validate the Candidates in the database
        List<Candidates> candidates = candidatesRepository.findAll();
        assertThat(candidates).hasSize(databaseSizeBeforeCreate + 1);
        Candidates testCandidates = candidates.get(candidates.size() - 1);
        assertThat(testCandidates.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testCandidates.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testCandidates.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testCandidates.getPhoneNumber()).isEqualTo(DEFAULT_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void getAllCandidates() throws Exception {
        // Initialize the database
        candidatesRepository.saveAndFlush(candidates);

        // Get all the candidates
        restCandidatesMockMvc.perform(get("/api/candidates?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(candidates.getId().intValue())))
                .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME.toString())))
                .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL.toString())))
                .andExpect(jsonPath("$.[*].phoneNumber").value(hasItem(DEFAULT_PHONE_NUMBER.toString())));
    }

    @Test
    @Transactional
    public void getCandidates() throws Exception {
        // Initialize the database
        candidatesRepository.saveAndFlush(candidates);

        // Get the candidates
        restCandidatesMockMvc.perform(get("/api/candidates/{id}", candidates.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(candidates.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME.toString()))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME.toString()))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL.toString()))
            .andExpect(jsonPath("$.phoneNumber").value(DEFAULT_PHONE_NUMBER.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCandidates() throws Exception {
        // Get the candidates
        restCandidatesMockMvc.perform(get("/api/candidates/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCandidates() throws Exception {
        // Initialize the database
        candidatesRepository.saveAndFlush(candidates);
        int databaseSizeBeforeUpdate = candidatesRepository.findAll().size();

        // Update the candidates
        Candidates updatedCandidates = candidatesRepository.findOne(candidates.getId());
        updatedCandidates
                .firstName(UPDATED_FIRST_NAME)
                .lastName(UPDATED_LAST_NAME)
                .email(UPDATED_EMAIL)
                .phoneNumber(UPDATED_PHONE_NUMBER);
        CandidatesDTO candidatesDTO = candidatesMapper.candidatesToCandidatesDTO(updatedCandidates);

        restCandidatesMockMvc.perform(put("/api/candidates")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(candidatesDTO)))
                .andExpect(status().isOk());

        // Validate the Candidates in the database
        List<Candidates> candidates = candidatesRepository.findAll();
        assertThat(candidates).hasSize(databaseSizeBeforeUpdate);
        Candidates testCandidates = candidates.get(candidates.size() - 1);
        assertThat(testCandidates.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testCandidates.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testCandidates.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testCandidates.getPhoneNumber()).isEqualTo(UPDATED_PHONE_NUMBER);
    }

    @Test
    @Transactional
    public void deleteCandidates() throws Exception {
        // Initialize the database
        candidatesRepository.saveAndFlush(candidates);
        int databaseSizeBeforeDelete = candidatesRepository.findAll().size();

        // Get the candidates
        restCandidatesMockMvc.perform(delete("/api/candidates/{id}", candidates.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Candidates> candidates = candidatesRepository.findAll();
        assertThat(candidates).hasSize(databaseSizeBeforeDelete - 1);
    }
}
