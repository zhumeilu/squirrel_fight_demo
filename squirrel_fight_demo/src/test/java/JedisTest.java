import com.lemeng.common.redis.SerializeUtil;
import com.lemeng.game.domain.AssistRecord;
import com.lemeng.game.domain.Room;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/29
 * Time: 11:46
 */
public class JedisTest {

    @Test
    public void test1(){
        Jedis jedis = new Jedis("localhost");
        String set = jedis.set("name", "zml");
        String name = jedis.get("name");
        System.out.println(name);
        AssistRecord record = new AssistRecord();
        record.setAssistId(1);
        record.setCreateDate(new Date());
        record.setEnemyId(2);
        record.setId(1);
//        jedis.lpush()
    }
    @Test
    public void test2(){
        List<Room> list = new ArrayList<Room>();
        list.add(new Room(1,1));
        list.add(new Room(2,2));
        byte[] serialize = SerializeUtil.serialize(list);
        List<Room> unserialize = (List<Room>)SerializeUtil.unserialize(serialize);
        System.out.println(unserialize);
    }
}
