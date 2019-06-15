import org.fortune.doc.server.DocServerContainer;
import org.fortune.doc.server.conf.web.RootApplicationContextConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * @author: Landy
 * @date: 2019/5/7 22:27
 * @description:
 */
@RunWith(SpringJUnit4ClassRunner.class) //调用Spring单元测试类
@WebAppConfiguration  //调用Java Web组件，如自动注入ServletContext Bean等,即自动加载WebApplicationAutoInitializer类的初始化动作
@ContextConfiguration(classes = {
        RootApplicationContextConfiguration.class,  // common settings configuration
}) //加载Spring配置文件
public class DocServerTest {

    @Test
    public void test() {
        System.out.println("Hello World");
        DocServerContainer docServerContainer = DocServerContainer.getInstance();
        System.out.println("=======================" + docServerContainer.getAccount("fortune").getRootPath());
    }

}
