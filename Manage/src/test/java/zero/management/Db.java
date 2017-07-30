package zero.management;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.runner.RunWith;

/**
 * Created by Aiy on 2017/5/26.
 */
@RunWith(AndroidJUnit4.class)
public class Db {
    private Person_Lib person_lib;
    private Context context;

    @Before
    public void setUp(){
        context= InstrumentationRegistry.getContext();
        person_lib=Person_Lib.getPersonLib(context);
        for (int i=0;i<3;i++){
            Person person=new Person();
            person.setNum(i);
            person.setName("学生"+i);
            person_lib.addPerson(person);
        }
    }
    @org.junit.Test
    public void testAdd(){
        Person person=new Person();
        person.setName("张三");
        person.setNum(10);
        person.setPassword("11111111");
        person.setScore(90);
        person_lib.addPerson(person);
    }
}
