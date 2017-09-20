import com.lemeng.user.domain.User;
import com.lemeng.user.manager.IUserManager;
import com.lemeng.user.mapper.UserMapper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/20
 * Time: 14:13
 */
@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试
@ContextConfiguration({"classpath:applicationContext.xml"}) //加载配置文件
public class MapperTest {


    @Autowired
    private IUserManager userManager;

    @org.junit.Test
    public void test(){
        User zml = userManager.login("zml", "123");
        System.out.println(zml);
    }

}
