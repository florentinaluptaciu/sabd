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

    @Mapping(source = "subcategory.id", target = "subcategoryId")
    @Mapping(source = "subcategory.subcategoryName", target = "subcategorySubcategoryName")
    QuestionDTO questionToQuestionDTO(Question question);

    List<QuestionDTO> questionsToQuestionDTOs(List<Question> questions);

    @Mapping(source = "subcategoryId", target = "subcategory")
    Question questionDTOToQuestion(QuestionDTO questionDTO);

    List<Question> questionDTOsToQuestions(List<QuestionDTO> questionDTOs);

    default Subcategory subcategoryFromId(Long id) {
        if (id == null) {
            return null;
        }
        Subcategory subcategory = new Subcategory();
        subcategory.setId(id);
        return subcategory;
    }
}
