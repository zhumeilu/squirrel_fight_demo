import com.lemeng.common.Const;
import com.lemeng.common.redis.JedisClusterUtil;
import com.lemeng.game.domain.Room;
import com.lemeng.user.domain.User;
import com.lemeng.user.manager.IUserManager;
import com.lemeng.user.mapper.UserMapper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.ArrayBlockingQueue;

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
    @Autowired
    JedisClusterUtil jedisClusterUtil;
    @org.junit.Test
    public void test(){
        User zml = userManager.login("zml", "123");
        System.out.println(zml);
    }
    @org.junit.Test
    public void test2(){
//        jedisClusterUtil.setString("name","zml");
        String name = jedisClusterUtil.getString("name");
        boolean name1 = jedisClusterUtil.delete("name");
        System.out.println(name);
        System.out.println(name1);
    }

    @Test
    public void test3(){
        ArrayBlockingQueue<Room> rooms = new ArrayBlockingQueue<Room>(10);
        Room room = new Room();
        room.setId(1);
        room.setScore(1);
        rooms.add(room);
        jedisClusterUtil.setObject(Const.RoomPrefix+room.getId(),room);
        Room b= (Room)jedisClusterUtil.getObject(Const.RoomPrefix+room.getId());
        System.out.println("room equal b"+room.equals(b));
        System.out.println(b);
        System.out.println("删除前"+rooms.size());
        rooms.remove(b);
        System.out.println("删除后1"+rooms.size());
        rooms.remove(room);
        System.out.println("删除后2"+rooms.size());


        jedisClusterUtil.delete(Const.RoomPrefix+room.getId());

    }
}
