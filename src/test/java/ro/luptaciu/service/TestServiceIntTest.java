package ro.luptaciu.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ro.luptaciu.InterviewApp;
import ro.luptaciu.domain.PersistentToken;
import ro.luptaciu.domain.User;
import ro.luptaciu.repository.PersistentTokenRepository;
import ro.luptaciu.repository.UserRepository;
import ro.luptaciu.service.dto.SubcategoryModel;
import ro.luptaciu.service.dto.TestDTO;
import ro.luptaciu.service.util.RandomUtil;
import ro.luptaciu.domain.*;
import javax.inject.Inject;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = InterviewApp.class)
@Transactional
@ActiveProfiles("dev")
public class TestServiceIntTest {

    @Inject
    private PersistentTokenRepository persistentTokenRepository;

    @Inject
    private UserRepository userRepository;

    @Inject
    private TestService testService;

    @Test
    public void test(){
        ro.luptaciu.domain.Test test = new ro.luptaciu.domain.Test();

        TestDTO testDTO = new TestDTO();
        List<SubcategoryModel> subcategoryModels = new ArrayList<>();

        subcategoryModels.add(new SubcategoryModel(1004L,"C++",1));
        subcategoryModels.add(new SubcategoryModel(1013L,"sql",1));

        testDTO.setSubcategories(subcategoryModels);


        List<Testxquestion> testxquestions = testService.getRandomTestXQuestions(test,testDTO);

        System.out.println(testxquestions);

    }

}
