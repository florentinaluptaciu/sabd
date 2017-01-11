package ro.luptaciu.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Objects;


/**
 * A DTO for the Test entity.
 */
public class TestDTO implements Serializable {

    private Long id;

    private LocalDate answerDate;

    private String testCode;

    private List<SubcategoryModel> subcategories;

    private CandidatesDTO candidateId;


    private String candidateFirstName;

    private String candidateLastName;

    public TestDTO() {
    }


    public TestDTO(Long id, LocalDate answerDate, String testCode, CandidatesDTO candidateId, String candidateFirstName, String candidateLastName,List<SubcategoryModel>  subcategories) {
        this.id = id;
        this.answerDate = answerDate;
        this.testCode = testCode;
        this.candidateId = candidateId;
        this.candidateFirstName = candidateFirstName;
        this.candidateLastName = candidateLastName;

    }
    public List<SubcategoryModel> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<SubcategoryModel> subcategories) {
        this.subcategories = subcategories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getAnswerDate() {
        return answerDate;
    }

    public void setAnswerDate(LocalDate answerDate) {
        this.answerDate = answerDate;
    }

    public String getTestCode() {
        return testCode;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }

    public CandidatesDTO getCandidateId() {
        return candidateId;
    }

    public void setCandidateId(CandidatesDTO candidateId) {
        this.candidateId = candidateId;
    }

    public String getCandidateFirstName() {
        return candidateFirstName;
    }

    public void setCandidateFirstName(String candidateFirstName) {
        this.candidateFirstName = candidateFirstName;
    }

    public String getCandidateLastName() {
        return candidateLastName;
    }

    public void setCandidateLastName(String candidateLastName) {
        this.candidateLastName = candidateLastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TestDTO testDTO = (TestDTO) o;

        if ( ! Objects.equals(id, testDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "TestDTO{" +
            "id=" + id +
            ", answerDate='" + answerDate + "'" +
            ", testCode='" + testCode + "'" +
            '}';
    }
}
