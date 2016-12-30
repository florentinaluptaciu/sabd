package ro.luptaciu.web.rest;

import ro.luptaciu.InterviewApp;

import ro.luptaciu.domain.Subcategory;
import ro.luptaciu.repository.SubcategoryRepository;
import ro.luptaciu.service.dto.SubcategoryDTO;
import ro.luptaciu.service.mapper.SubcategoryMapper;

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
 * Test class for the SubcategoryResource REST controller.
 *
 * @see SubcategoryResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InterviewApp.class)
public class SubcategoryResourceIntTest {

    private static final String DEFAULT_SUBCATEGORY_NAME = "AAAAA";
    private static final String UPDATED_SUBCATEGORY_NAME = "BBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    @Inject
    private SubcategoryRepository subcategoryRepository;

    @Inject
    private SubcategoryMapper subcategoryMapper;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSubcategoryMockMvc;

    private Subcategory subcategory;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SubcategoryResource subcategoryResource = new SubcategoryResource();
        ReflectionTestUtils.setField(subcategoryResource, "subcategoryRepository", subcategoryRepository);
        ReflectionTestUtils.setField(subcategoryResource, "subcategoryMapper", subcategoryMapper);
        this.restSubcategoryMockMvc = MockMvcBuilders.standaloneSetup(subcategoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Subcategory createEntity(EntityManager em) {
        Subcategory subcategory = new Subcategory()
                .subcategoryName(DEFAULT_SUBCATEGORY_NAME)
                .isActive(DEFAULT_IS_ACTIVE);
        return subcategory;
    }

    @Before
    public void initTest() {
        subcategory = createEntity(em);
    }

    @Test
    @Transactional
    public void createSubcategory() throws Exception {
        int databaseSizeBeforeCreate = subcategoryRepository.findAll().size();

        // Create the Subcategory
        SubcategoryDTO subcategoryDTO = subcategoryMapper.subcategoryToSubcategoryDTO(subcategory);

        restSubcategoryMockMvc.perform(post("/api/subcategories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(subcategoryDTO)))
                .andExpect(status().isCreated());

        // Validate the Subcategory in the database
        List<Subcategory> subcategories = subcategoryRepository.findAll();
        assertThat(subcategories).hasSize(databaseSizeBeforeCreate + 1);
        Subcategory testSubcategory = subcategories.get(subcategories.size() - 1);
        assertThat(testSubcategory.getSubcategoryName()).isEqualTo(DEFAULT_SUBCATEGORY_NAME);
        assertThat(testSubcategory.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllSubcategories() throws Exception {
        // Initialize the database
        subcategoryRepository.saveAndFlush(subcategory);

        // Get all the subcategories
        restSubcategoryMockMvc.perform(get("/api/subcategories?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(subcategory.getId().intValue())))
                .andExpect(jsonPath("$.[*].subcategoryName").value(hasItem(DEFAULT_SUBCATEGORY_NAME.toString())))
                .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    public void getSubcategory() throws Exception {
        // Initialize the database
        subcategoryRepository.saveAndFlush(subcategory);

        // Get the subcategory
        restSubcategoryMockMvc.perform(get("/api/subcategories/{id}", subcategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(subcategory.getId().intValue()))
            .andExpect(jsonPath("$.subcategoryName").value(DEFAULT_SUBCATEGORY_NAME.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingSubcategory() throws Exception {
        // Get the subcategory
        restSubcategoryMockMvc.perform(get("/api/subcategories/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSubcategory() throws Exception {
        // Initialize the database
        subcategoryRepository.saveAndFlush(subcategory);
        int databaseSizeBeforeUpdate = subcategoryRepository.findAll().size();

        // Update the subcategory
        Subcategory updatedSubcategory = subcategoryRepository.findOne(subcategory.getId());
        updatedSubcategory
                .subcategoryName(UPDATED_SUBCATEGORY_NAME)
                .isActive(UPDATED_IS_ACTIVE);
        SubcategoryDTO subcategoryDTO = subcategoryMapper.subcategoryToSubcategoryDTO(updatedSubcategory);

        restSubcategoryMockMvc.perform(put("/api/subcategories")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(subcategoryDTO)))
                .andExpect(status().isOk());

        // Validate the Subcategory in the database
        List<Subcategory> subcategories = subcategoryRepository.findAll();
        assertThat(subcategories).hasSize(databaseSizeBeforeUpdate);
        Subcategory testSubcategory = subcategories.get(subcategories.size() - 1);
        assertThat(testSubcategory.getSubcategoryName()).isEqualTo(UPDATED_SUBCATEGORY_NAME);
        assertThat(testSubcategory.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void deleteSubcategory() throws Exception {
        // Initialize the database
        subcategoryRepository.saveAndFlush(subcategory);
        int databaseSizeBeforeDelete = subcategoryRepository.findAll().size();

        // Get the subcategory
        restSubcategoryMockMvc.perform(delete("/api/subcategories/{id}", subcategory.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Subcategory> subcategories = subcategoryRepository.findAll();
        assertThat(subcategories).hasSize(databaseSizeBeforeDelete - 1);
    }
}
