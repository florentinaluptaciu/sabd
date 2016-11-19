package ro.luptaciu.web.rest;

import ro.luptaciu.InterviewApp;

import ro.luptaciu.domain.Question;
import ro.luptaciu.repository.QuestionRepository;
import ro.luptaciu.service.QuestionService;
import ro.luptaciu.service.dto.QuestionDTO;
import ro.luptaciu.service.mapper.QuestionMapper;

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
import org.springframework.util.Base64Utils;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ro.luptaciu.domain.enumeration.CATEGORY;
/**
 * Test class for the QuestionResource REST controller.
 *
 * @see QuestionResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InterviewApp.class)
public class QuestionResourceIntTest {

    private static final String DEFAULT_CONTENT = "";
    private static final String UPDATED_CONTENT = "";

    private static final String DEFAULT_ANSWER_1 = "AAAAA";
    private static final String UPDATED_ANSWER_1 = "BBBBB";

    private static final String DEFAULT_ANSWER_2 = "AAAAA";
    private static final String UPDATED_ANSWER_2 = "BBBBB";

    private static final String DEFAULT_ANSWER_3 = "AAAAA";
    private static final String UPDATED_ANSWER_3 = "BBBBB";

    private static final Integer DEFAULT_RIGHT_ANSWER = 1;
    private static final Integer UPDATED_RIGHT_ANSWER = 2;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final CATEGORY DEFAULT_CATEGORY = CATEGORY.JAVA;
    private static final CATEGORY UPDATED_CATEGORY = CATEGORY.HTML;

    @Inject
    private QuestionRepository questionRepository;

    @Inject
    private QuestionMapper questionMapper;

    @Inject
    private QuestionService questionService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restQuestionMockMvc;

    private Question question;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        QuestionResource questionResource = new QuestionResource();
        ReflectionTestUtils.setField(questionResource, "questionService", questionService);
        this.restQuestionMockMvc = MockMvcBuilders.standaloneSetup(questionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Question createEntity(EntityManager em) {
        Question question = new Question()
                .content(DEFAULT_CONTENT)
                .answer1(DEFAULT_ANSWER_1)
                .answer2(DEFAULT_ANSWER_2)
                .answer3(DEFAULT_ANSWER_3)
                .rightAnswer(DEFAULT_RIGHT_ANSWER)
                .isActive(DEFAULT_IS_ACTIVE)
                .category(DEFAULT_CATEGORY);
        return question;
    }

    @Before
    public void initTest() {
        question = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuestion() throws Exception {
        int databaseSizeBeforeCreate = questionRepository.findAll().size();

        // Create the Question
        QuestionDTO questionDTO = questionMapper.questionToQuestionDTO(question);

        restQuestionMockMvc.perform(post("/api/questions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(questionDTO)))
                .andExpect(status().isCreated());

        // Validate the Question in the database
        List<Question> questions = questionRepository.findAll();
        assertThat(questions).hasSize(databaseSizeBeforeCreate + 1);
        Question testQuestion = questions.get(questions.size() - 1);
        assertThat(testQuestion.getContent()).isEqualTo(DEFAULT_CONTENT);
        assertThat(testQuestion.getAnswer1()).isEqualTo(DEFAULT_ANSWER_1);
        assertThat(testQuestion.getAnswer2()).isEqualTo(DEFAULT_ANSWER_2);
        assertThat(testQuestion.getAnswer3()).isEqualTo(DEFAULT_ANSWER_3);
        assertThat(testQuestion.getRightAnswer()).isEqualTo(DEFAULT_RIGHT_ANSWER);
        assertThat(testQuestion.isIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
        assertThat(testQuestion.getCategory()).isEqualTo(DEFAULT_CATEGORY);
    }

    @Test
    @Transactional
    public void getAllQuestions() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questions
        restQuestionMockMvc.perform(get("/api/questions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(question.getId().intValue())))
                .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
                .andExpect(jsonPath("$.[*].answer1").value(hasItem(DEFAULT_ANSWER_1.toString())))
                .andExpect(jsonPath("$.[*].answer2").value(hasItem(DEFAULT_ANSWER_2.toString())))
                .andExpect(jsonPath("$.[*].answer3").value(hasItem(DEFAULT_ANSWER_3.toString())))
                .andExpect(jsonPath("$.[*].rightAnswer").value(hasItem(DEFAULT_RIGHT_ANSWER)))
                .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
                .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY.toString())));
    }

    @Test
    @Transactional
    public void getQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get the question
        restQuestionMockMvc.perform(get("/api/questions/{id}", question.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(question.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.answer1").value(DEFAULT_ANSWER_1.toString()))
            .andExpect(jsonPath("$.answer2").value(DEFAULT_ANSWER_2.toString()))
            .andExpect(jsonPath("$.answer3").value(DEFAULT_ANSWER_3.toString()))
            .andExpect(jsonPath("$.rightAnswer").value(DEFAULT_RIGHT_ANSWER))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingQuestion() throws Exception {
        // Get the question
        restQuestionMockMvc.perform(get("/api/questions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();

        // Update the question
        Question updatedQuestion = questionRepository.findOne(question.getId());
        updatedQuestion
                .content(UPDATED_CONTENT)
                .answer1(UPDATED_ANSWER_1)
                .answer2(UPDATED_ANSWER_2)
                .answer3(UPDATED_ANSWER_3)
                .rightAnswer(UPDATED_RIGHT_ANSWER)
                .isActive(UPDATED_IS_ACTIVE)
                .category(UPDATED_CATEGORY);
        QuestionDTO questionDTO = questionMapper.questionToQuestionDTO(updatedQuestion);

        restQuestionMockMvc.perform(put("/api/questions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(questionDTO)))
                .andExpect(status().isOk());

        // Validate the Question in the database
        List<Question> questions = questionRepository.findAll();
        assertThat(questions).hasSize(databaseSizeBeforeUpdate);
        Question testQuestion = questions.get(questions.size() - 1);
        assertThat(testQuestion.getContent()).isEqualTo(UPDATED_CONTENT);
        assertThat(testQuestion.getAnswer1()).isEqualTo(UPDATED_ANSWER_1);
        assertThat(testQuestion.getAnswer2()).isEqualTo(UPDATED_ANSWER_2);
        assertThat(testQuestion.getAnswer3()).isEqualTo(UPDATED_ANSWER_3);
        assertThat(testQuestion.getRightAnswer()).isEqualTo(UPDATED_RIGHT_ANSWER);
        assertThat(testQuestion.isIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
        assertThat(testQuestion.getCategory()).isEqualTo(UPDATED_CATEGORY);
    }

    @Test
    @Transactional
    public void deleteQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);
        int databaseSizeBeforeDelete = questionRepository.findAll().size();

        // Get the question
        restQuestionMockMvc.perform(delete("/api/questions/{id}", question.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Question> questions = questionRepository.findAll();
        assertThat(questions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
