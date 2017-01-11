package ro.luptaciu.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Test.
 */
@Entity
@Table(name = "test")
public class Test implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "answer_date")
    private LocalDate answerDate;

    @Column(name = "test_code")
    private String testCode;

    @ManyToOne
    private Candidates candidateId;

    public Test() {
    }

    public Test(LocalDate answerDate, String testCode, Candidates candidateId) {
        this.answerDate = answerDate;
        this.testCode = testCode;
        this.candidateId = candidateId;
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

    public Test answerDate(LocalDate answerDate) {
        this.answerDate = answerDate;
        return this;
    }

    public void setAnswerDate(LocalDate answerDate) {
        this.answerDate = answerDate;
    }

    public String getTestCode() {
        return testCode;
    }

    public Test testCode(String testCode) {
        this.testCode = testCode;
        return this;
    }

    public void setTestCode(String testCode) {
        this.testCode = testCode;
    }

    public Candidates getCandidateId() {
        return candidateId;
    }

    public Test candidateId(Candidates candidates) {
        this.candidateId = candidates;
        return this;
    }

    public void setCandidateId(Candidates candidates) {
        this.candidateId = candidates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Test test = (Test) o;
        if(test.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, test.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Test{" +
            "id=" + id +
            ", answerDate='" + answerDate + "'" +
            ", testCode='" + testCode + "'" +
            '}';
    }
}
