package ro.luptaciu.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Testxquestion.
 */
@Entity
@Table(name = "testxquestion")
public class Testxquestion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "answer")
    private Integer answer;

    @ManyToOne
    private Test test;

    @ManyToOne
    private Question question;


    public Testxquestion() {
    }

    public Testxquestion(Test test, Question question) {
        this.test = test;
        this.question = question;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAnswer() {
        return answer;
    }

    public Testxquestion answer(Integer answer) {
        this.answer = answer;
        return this;
    }

    public void setAnswer(Integer answer) {
        this.answer = answer;
    }

    public Test getTest() {
        return test;
    }

    public Testxquestion test(Test test) {
        this.test = test;
        return this;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public Question getQuestion() {
        return question;
    }

    public Testxquestion question(Question question) {
        this.question = question;
        return this;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Testxquestion testxquestion = (Testxquestion) o;
        if(testxquestion.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, testxquestion.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }


    @Override
    public String toString() {
        return "Testxquestion{" +
            "id=" + id +
            ", answer=" + answer +
            ", test=" + test +
            ", question=" + question +
            '}';
    }
}
