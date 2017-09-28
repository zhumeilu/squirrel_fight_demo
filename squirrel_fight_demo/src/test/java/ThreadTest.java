import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/28
 * Time: 17:42
 */
public class ThreadTest {


    @Test
    public void test1(){

        List<String> strList = Arrays.asList("1","2","3","a","b","c");
        List<String> strList2 = new ArrayList<String>();
        Collections.copy(strList,strList2);
        new Thread(new Temp(strList2)).start();
        strList.clear();

    }
}

class Temp implements Runnable{

    List<String> strList;
    public Temp(List<String> strList){
        this.strList = strList;
    }
    public void run() {
        while(true){
            for (String str:strList) {
                System.out.println(str);
            }
        }
    }
}