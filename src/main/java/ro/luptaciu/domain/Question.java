package ro.luptaciu.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

import ro.luptaciu.domain.enumeration.CATEGORY;

/**
 * A Question.
 */
@Entity
@Table(name = "question")
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Lob
    @Column(name = "content")
    private String content;

    @Column(name = "answer_1")
    private String answer1;

    @Column(name = "answer_2")
    private String answer2;

    @Column(name = "answer_3")
    private String answer3;

    @Column(name = "right_answer")
    private Integer rightAnswer;

    @Column(name = "is_active")
    private Boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "category")
    private CATEGORY category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public Question content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswer1() {
        return answer1;
    }

    public Question answer1(String answer1) {
        this.answer1 = answer1;
        return this;
    }

    public void setAnswer1(String answer1) {
        this.answer1 = answer1;
    }

    public String getAnswer2() {
        return answer2;
    }

    public Question answer2(String answer2) {
        this.answer2 = answer2;
        return this;
    }

    public void setAnswer2(String answer2) {
        this.answer2 = answer2;
    }

    public String getAnswer3() {
        return answer3;
    }

    public Question answer3(String answer3) {
        this.answer3 = answer3;
        return this;
    }

    public void setAnswer3(String answer3) {
        this.answer3 = answer3;
    }

    public Integer getRightAnswer() {
        return rightAnswer;
    }

    public Question rightAnswer(Integer rightAnswer) {
        this.rightAnswer = rightAnswer;
        return this;
    }

    public void setRightAnswer(Integer rightAnswer) {
        this.rightAnswer = rightAnswer;
    }

    public Boolean isIsActive() {
        return isActive;
    }

    public Question isActive(Boolean isActive) {
        this.isActive = isActive;
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public CATEGORY getCategory() {
        return category;
    }

    public Question category(CATEGORY category) {
        this.category = category;
        return this;
    }

    public void setCategory(CATEGORY category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Question question = (Question) o;
        if(question.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, question.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Question{" +
            "id=" + id +
            ", content='" + content + "'" +
            ", answer1='" + answer1 + "'" +
            ", answer2='" + answer2 + "'" +
            ", answer3='" + answer3 + "'" +
            ", rightAnswer='" + rightAnswer + "'" +
            ", isActive='" + isActive + "'" +
            ", category='" + category + "'" +
            '}';
    }
}
