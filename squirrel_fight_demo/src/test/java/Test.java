import java.io.IOException;

/**
 * Description:
 * User: zhumeilu
 * Date: 2017/9/20
 * Time: 11:08
 */
public class Test {

    @org.junit.Test
    public void test1(){

        try{
            throw new Error();
        }catch (NullPointerException e){
            System.out.println("-------exception");
        }finally {
            System.out.println("------finally--------");
        }
        System.out.println("-----结束--------");
    }
}
