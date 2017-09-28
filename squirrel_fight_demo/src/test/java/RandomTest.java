import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/28
 * Time: 18:22
 */
public class RandomTest {

    @Test
    public void test1(){
        for (int i=0;i<100;i++){
            int i1 = RandomUtils.nextInt(2, 7);
            System.out.println(i1);

        }
    }
}
