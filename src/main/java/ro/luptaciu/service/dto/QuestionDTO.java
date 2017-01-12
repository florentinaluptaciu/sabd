package ro.luptaciu.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import javax.persistence.Lob;


/**
 * A DTO for the Question entity.
 */
public class QuestionDTO implements Serializable {

    private Long id;

    @Lob
    private String content;

    private String answer1;

    private String answer2;

    private String answer3;

    private Integer rightAnswer;

    private Integer testeeAnswer;


    private Boolean isActive;


    private Long subcategoryId;


    private String subcategorySubcategoryName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getAnswer1() {
        return answer1;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }
    public String getAnswer2() {
        return answer2;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }
    public String getAnswer3() {
        return answer3;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }
    public Integer getRightAnswer() {
        return rightAnswer;
    }

    public void setRightAnswer(Integer rightAnswer) {
        this.rightAnswer = rightAnswer;
    }
    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Long getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(Long subcategoryId) {
        this.subcategoryId = subcategoryId;
    }


    public String getSubcategorySubcategoryName() {
        return subcategorySubcategoryName;
    }

    public void setSubcategorySubcategoryName(String subcategorySubcategoryName) {
        this.subcategorySubcategoryName = subcategorySubcategoryName;
    }

    public Integer getTesteeAnswer() {
        return testeeAnswer;
    }

    public void setTesteeAnswer(Integer testeeAnswer) {
        this.testeeAnswer = testeeAnswer;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        QuestionDTO questionDTO = (QuestionDTO) o;

        if ( ! Objects.equals(id, questionDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "QuestionDTO{" +
            "id=" + id +
            ", content='" + content + '\'' +
            ", answer1='" + answer1 + '\'' +
            ", answer2='" + answer2 + '\'' +
            ", answer3='" + answer3 + '\'' +
            ", rightAnswer=" + rightAnswer +
            ", testeeAnswer=" + testeeAnswer +
            ", isActive=" + isActive +
            ", subcategoryId=" + subcategoryId +
            ", subcategorySubcategoryName='" + subcategorySubcategoryName + '\'' +
            '}';
    }
}
