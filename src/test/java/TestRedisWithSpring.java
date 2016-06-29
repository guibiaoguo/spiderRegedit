import com.ucsmy.collection.controllers.CaptchaController;
import com.ucsmy.collection.controllers.JobController;
import com.ucsmy.collection.models.Captcha;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

/**
 * Created by Administrator on 2015/11/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MockServletContext.class)
@WebAppConfiguration
public class TestRedisWithSpring {
    private MockMvc mvc;
    @Before
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new CaptchaController()).build();
    }

    @Test
    public void getHello() throws Exception {
        ResultActions actions = mvc.perform(MockMvcRequestBuilders.get("captcha/listAjax").accept(MediaType.ALL));
//        ValueOperations<String, Object> ops = redisTemplate.opsForValue();
//        ListOperations<String,Object> listops = redisTemplate.opsForList();
//        Object object = listops.leftPop("java framework");
//        List<Captcha> captcha = (List<Captcha>) ops.get("com.ucsmy.collection.services.CaptchaServicefindAll");
        MvcResult result = actions.andReturn();
    }
}
