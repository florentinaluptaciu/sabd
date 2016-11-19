package ro.luptaciu.service.mapper;

import ro.luptaciu.domain.*;
import ro.luptaciu.service.dto.QuestionDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Question and its DTO QuestionDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface QuestionMapper {

    QuestionDTO questionToQuestionDTO(Question question);

    List<QuestionDTO> questionsToQuestionDTOs(List<Question> questions);

    Question questionDTOToQuestion(QuestionDTO questionDTO);

    List<Question> questionDTOsToQuestions(List<QuestionDTO> questionDTOs);
}
