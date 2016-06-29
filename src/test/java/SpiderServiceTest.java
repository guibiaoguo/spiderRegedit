import com.ucsmy.collection.Application;
import com.ucsmy.collection.dto.Link;
import com.ucsmy.collection.services.SpiderService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


/**
 * Created by Administrator on 2016/1/5.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
public class SpiderServiceTest {

    @Autowired
    private SpiderService spiderService;

    @Test
    public void getlinks() {
        Assert.assertEquals("1", "1");
        Link links = spiderService.getlinks("ll123",20L);
        for (Object link:links.getLinks()) {
            System.out.println(link);
        }
    }

    @Test
    public void test() {

    }

}
