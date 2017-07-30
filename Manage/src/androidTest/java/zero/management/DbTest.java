package zero.management;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;
import org.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.List;



/**
 * Created by Aiy on 2017/5/26.
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class DbTest {
    private static Person_Lib person_lib;
    private static Context context;
        @BeforeClass
        public  static void setUp(){

            context= InstrumentationRegistry.getTargetContext();
            // SQLiteDatabase database=new Database_db(context).getWritableDatabase();
            person_lib=Person_Lib.getPersonLib(context);
            for (int i=0;i<3;i++){
                Person person=new Person();
                person.setNum(i+1);
                person.setName("学生"+(i+1));
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

    @Test
    public void testDelete(){
        person_lib.Delete(1);
    }

    @Test
    public void testUpdate(){
        List<Person> persons=person_lib.getList();
        Person person=persons.get(0);
        person.setName("修改后的学生");
        person_lib.updatePerson(person);
    }

    @Test
    public void testQuery(){
        Person person=person_lib.getPerson(2);
        Assert.assertEquals(person.getNum(),2);
    }
    @Test
    public void testErrorQuery(){
        Person person=person_lib.getPerson(5);
        Assert.assertEquals(person.getNum(),6);

    }

}
