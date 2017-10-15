import com.lemeng.game.domain.Room;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/10/10
 * Time: 16:18
 */
public class ListTest {

    @Test
    public void test1(){
        List<Room> list = new ArrayList<Room>();
        Room room = new Room(1, 1);
        list.add(room);
        System.out.println(list);
        room.setScore(2);
        System.out.println(list);

    }
}
