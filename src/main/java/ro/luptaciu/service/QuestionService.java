package ro.luptaciu.service;

import ro.luptaciu.domain.Question;
import ro.luptaciu.repository.QuestionRepository;
import ro.luptaciu.service.dto.QuestionDTO;
import ro.luptaciu.service.mapper.QuestionMapper;
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
 * Service Implementation for managing Question.
 */
@Service
@Transactional
public class QuestionService {

    private final Logger log = LoggerFactory.getLogger(QuestionService.class);
    
    @Inject
    private QuestionRepository questionRepository;

    @Inject
    private QuestionMapper questionMapper;

    /**
     * Save a question.
     *
     * @param questionDTO the entity to save
     * @return the persisted entity
     */
    public QuestionDTO save(QuestionDTO questionDTO) {
        log.debug("Request to save Question : {}", questionDTO);
        Question question = questionMapper.questionDTOToQuestion(questionDTO);
        question = questionRepository.save(question);
        QuestionDTO result = questionMapper.questionToQuestionDTO(question);
        return result;
    }

    /**
     *  Get all the questions.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<QuestionDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Questions");
        Page<Question> result = questionRepository.findAll(pageable);
        return result.map(question -> questionMapper.questionToQuestionDTO(question));
    }

    /**
     *  Get one question by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public QuestionDTO findOne(Long id) {
        log.debug("Request to get Question : {}", id);
        Question question = questionRepository.findOne(id);
        QuestionDTO questionDTO = questionMapper.questionToQuestionDTO(question);
        return questionDTO;
    }

    /**
     *  Delete the  question by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Question : {}", id);
        questionRepository.delete(id);
    }
}
