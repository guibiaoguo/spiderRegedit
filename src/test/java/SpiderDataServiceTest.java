import com.ucsmy.collection.Application;
import com.ucsmy.collection.JpaConfig;
import com.ucsmy.collection.services.SpiderDataService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2015/12/31.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Application.class})
public class SpiderDataServiceTest {

    @Resource
    private SpiderDataService spiderDataService;

    @Test
    public void insertData() {
        Assert.assertEquals("1","1");
        spiderDataService.insertData();
    }

    @Test
    public void savelink() {
        Assert.assertEquals("1","1");
        spiderDataService.saveLink();
    }

    @Test
    public void startJob() {
        Assert.assertEquals("1","1");
        spiderDataService.startJob();
    }

    @Test
    public void stopJob() {
        Assert.assertEquals("1","1");
        spiderDataService.stopJob();
    }

    @Test
    public void reStartJob() {
        Assert.assertEquals("1","1");
        spiderDataService.restartJob(1);
    }

    @Test
    public void reflash() {
        Assert.assertEquals("1","1");
        spiderDataService.reflash();
    }

    @Test
    public void reflashProxy() {
        Assert.assertEquals("1","1");
        spiderDataService.reflashPxoy();
    }


    @Test
    public void testLink() {
        spiderDataService.test();
    }
}
