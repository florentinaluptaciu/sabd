package ro.luptaciu.service;

import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.beans.factory.annotation.Autowired;
import ro.luptaciu.domain.*;
import ro.luptaciu.repository.QuestionRepository;
import ro.luptaciu.repository.SubcategoryRepository;
import ro.luptaciu.repository.TestRepository;
import ro.luptaciu.repository.TestxquestionRepository;
import ro.luptaciu.service.dto.CandidatesDTO;
import ro.luptaciu.service.dto.QuestionDTO;
import ro.luptaciu.service.dto.SubcategoryModel;
import ro.luptaciu.service.dto.TestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import ro.luptaciu.service.mapper.QuestionMapper;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.nio.ByteBuffer;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Test.
 */
@Service
@Transactional
public class TestService {

    private final Logger log = LoggerFactory.getLogger(TestService.class);

    @Inject
    private TestRepository testRepository;
    @Inject
    private SubcategoryRepository subcategoryRepository;

    @Inject
    private QuestionRepository questionRepository;

    @Inject
    private TestxquestionRepository testxquestionRepository;

    @Inject
    private QuestionMapper questionMapper;

    @Autowired
    EntityManager em;

    public TestDTO testToTestDTO(Test test, List<SubcategoryModel> subcategories) {

        return new TestDTO(test.getId(), test.getAnswerDate(), test.getTestCode(), candidatesToCandidatesDTO(test.getCandidateId()),
            test.getCandidateId().getFirstName(), test.getCandidateId().getLastName(), subcategories);
    }

    public TestDTO testToTestDTO(Test test) {

        return new TestDTO(test.getId(), test.getAnswerDate(), test.getTestCode(), candidatesToCandidatesDTO(test.getCandidateId()),
            test.getCandidateId().getFirstName(), test.getCandidateId().getLastName(), null);
    }

    public Test testDTOToTest(TestDTO testDTO) {
        Test test = new Test();
        test.setAnswerDate(testDTO.getAnswerDate());
        test.setCandidateId(candidatesDTOToCandidates(testDTO.getCandidateId()));
        test.setTestCode(testDTO.getTestCode());
        test.setId(testDTO.getId());

        return test;

    }

    public CandidatesDTO candidatesToCandidatesDTO(Candidates candidates) {
        if (candidates == null) {
            return null;
        }

        CandidatesDTO candidatesDTO = new CandidatesDTO();

        candidatesDTO.setId(candidates.getId());
        candidatesDTO.setFirstName(candidates.getFirstName());
        candidatesDTO.setLastName(candidates.getLastName());
        candidatesDTO.setEmail(candidates.getEmail());
        candidatesDTO.setPhoneNumber(candidates.getPhoneNumber());

        return candidatesDTO;
    }


    public Candidates candidatesDTOToCandidates(CandidatesDTO candidatesDTO) {
        if (candidatesDTO == null) {
            return null;
        }

        Candidates candidates = new Candidates();

        candidates.setId(candidatesDTO.getId());
        candidates.setFirstName(candidatesDTO.getFirstName());
        candidates.setLastName(candidatesDTO.getLastName());
        candidates.setEmail(candidatesDTO.getEmail());
        candidates.setPhoneNumber(candidatesDTO.getPhoneNumber());

        return candidates;
    }

    /**
     * Save a test.
     *
     * @param testDTO the entity to save
     * @return the persisted entity
     */
    public TestDTO save(TestDTO testDTO) {
        log.debug("Request to save Test : {}", testDTO);

        Test test = testDTOToTest(testDTO);
        test = testRepository.saveAndFlush(test);

        List<Testxquestion> testxquestions = getRandomTestXQuestions(test, testDTO);


        testxquestions.forEach(a -> testxquestionRepository.save(a));

        TestDTO result = testToTestDTO(test, testDTO.getSubcategories());
        return result;
    }

    public List<Testxquestion> getRandomTestXQuestions(Test test, TestDTO testDTO) {

        System.out.println("\n\n\n\n " + testDTO.getSubcategories());
        List<Testxquestion> testxquestions = new ArrayList<>();

        for (SubcategoryModel subcategoryModel : testDTO.getSubcategories()) {
            if (subcategoryModel.getNoOfQues() != null) {
                List<Question> question = questionRepository.findBySubcategoryId(subcategoryModel.getId());

                for (int i = 0; i < subcategoryModel.getNoOfQues(); i++) {
                    Question randomQuestion = getRandomQuestion(question);

                    testxquestions.add(new Testxquestion(test, randomQuestion));
                    question.remove(randomQuestion);

                    System.out.println("\n\n\n\n ce a gasit el" + question);
                }
            }
        }
        System.out.println("\n\n\n\n " + testxquestions);

        return testxquestions;
    }

    public Question getRandomQuestion(List<Question> questions) {
        int index = ThreadLocalRandom.current().nextInt(0, questions.size());
        return questions.get(index);
    }

    /**
     * Get all the tests.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<TestDTO> findAll() {
        log.debug("Request to get all Tests");
        List<TestDTO> result = testRepository.findAll().stream()
            .map(this::testToTestDTO)
            .collect(Collectors.toCollection(LinkedList::new));

        return result;
    }

    /**
     * Get one test by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public TestDTO findOne(Long id) {
        log.debug("Request to get Test : {}", id);
        Test test = testRepository.findOne(id);
        TestDTO testDTO = testToTestDTO(test);
        return testDTO;
    }

    /**
     * Delete the  test by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Test : {}", id);
        testRepository.delete(id);
    }

    public List<QuestionDTO> findTestByCode(String code) {

        List<Testxquestion> testxquestions = testxquestionRepository.findAll();
        List<Question> questions = new ArrayList<>();
        for (Testxquestion testxquestion : testxquestions) {
            if (testxquestion.getTest().getTestCode().equals(code)) {
                questions.add(testxquestion.getQuestion());
            }
        }
        ;


        return questionMapper.questionsToQuestionDTOs(questions);

    }

    public String calculateResult(List<QuestionDTO> questions, String code) {

        List<Testxquestion> testxquestions = testxquestionRepository.findAll();
        String result = "Correct answers = ";
        int i = 0;
        for (QuestionDTO questionDTO : questions) {

            if (questionDTO.getTesteeAnswer().equals(questionDTO.getRightAnswer())) {
                i++;
            }
        }
        result = result + i + " of " + questions.size();

        for (Testxquestion testxquestion : testxquestions) {
            for (QuestionDTO questionDTO : questions) {
                if (testxquestion.getTest().getTestCode().equals(code) && testxquestion.getQuestion().getId().equals(questionDTO.getId())) {
                    testxquestion.setAnswer(questionDTO.getTesteeAnswer());
                    Test test = testxquestion.getTest();
                    testxquestionRepository.save(testxquestion);
                    test.setAnswerDate(LocalDate.now());
                    testRepository.save(test);
                }
            }
        }
        return result;

    }
}
