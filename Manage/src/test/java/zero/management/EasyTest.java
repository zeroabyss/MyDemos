package zero.management;

import org.junit.*;

/**
 * Created by Aiy on 2017/5/26.
 */

public class EasyTest {
    private Test test;
    @Before
    public void ready(){
        test=new Test();
    }

    @org.junit.Test
    public void testA(){
        test.setOne(1);
        Assert.assertEquals(1,test.getOne());
    }
    @org.junit.Test
    public void testB(){
        test.setOne(1);
        Assert.assertEquals(2,test.getOne());
    }
}
