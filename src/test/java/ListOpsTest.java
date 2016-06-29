/**
 * Created by Administrator on 2015/11/27.
 */
/**
 * Mar 5, 2013
 */
import com.ucsmy.collection.Application;
import com.ucsmy.collection.controllers.CaptchaController;
import com.ucsmy.collection.controllers.JobController;
import com.ucsmy.collection.services.ListOps;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
 *
 * @author snowolf
 * @version 1.0
 * @since 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class ListOpsTest {

    private MockMvc mvc;

    private ListOps listOps;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders.standaloneSetup(new JobController()).build();
    }

    @Test
    public void test() {
        Assert.assertEquals(1,1);
    }
//    @Before
//    public void before() throws Exception {
//
//        System.out.println("------------IN---------------");
//        for (int i = 0; i < 5; i++) {
//            String uid = "u" + i;
//            System.out.println(uid);
//            listOps.in(key, uid);
//        }
//    }
//
//    @After
//    public void after() {
//        // ------------OUT---------------
//        System.out.println("------------OUT---------------");
//        long length = listOps.length(key);
//        for (long i = 0; i < length; i++) {
//            String uid = listOps.out(key);
//            System.out.println(uid);
//        }
//    }
//
//    @Test
//    public void stack() {
//        // ------------PUSH---------------
//        String key = "stack";
//        int len = 5;
//        System.out.println("------------PUSH---------------");
//        for (int i = 0; i < len; i++) {
//            String uid = "u" + System.currentTimeMillis();
//            System.out.println(uid);
//            listOps.push(key, uid);
//        }
//
//        long length = listOps.length(key);
//        assertEquals(len, length);
//
//        // ------------POP---------------
//        System.out.println("------------POP---------------");
//        for (long i = 0; i < length; i++) {
//            String uid = listOps.pop(key);
//            System.out.println(uid);
//        }
//    }
//
//    @Test
//    public void index() {
//
//        // -------------INDEX-------------
//        String value = listOps.index(key, 3);
//        assertEquals("u3", value);
//    }
//
//    @Test
//    public void range() {
//        // -------------RANGE-------------
//        List<String> list = listOps.range(key, 3, 5);
//        boolean result1 = list.contains("u3");
//        assertEquals(true, result1);
//
//        boolean result2 = list.contains("u1");
//        assertEquals(false, result2);
//    }
//
//    @Test
//    public void trim() {
//        // ------------TRIM---------------
//        List<String> list = listOps.range(key, 3, 5);
//        listOps.trim(key, 3, 5);
//        boolean result3 = list.contains("u1");
//        assertEquals(false, result3);
//    }
//
//    @Test
//    public void set() {
//        // ------------SET-----------------
//        List<String> list = listOps.range(key, 3, 5);
//        listOps.set(key, 4, "ux4");
//        boolean result4 = list.contains("u4");
//        assertEquals(true, result4);
//
//    }
//
//    @Test
//    public void remove() {
//        // ------------REMOVE-----------------
//        listOps.remove(key, 4, "u4");
//        String value = listOps.index(key, 4);
//        assertEquals(null, value);
//
//    }


}

