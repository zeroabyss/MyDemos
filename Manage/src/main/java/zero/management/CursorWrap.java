package zero.management;

import android.database.CursorWrapper;
import android.util.Log;

import java.util.UUID;

import zero.management.Datababse_Values.Table;

/**
 *任务描述： 浮标执行的过程。
 *创建时间： 2017/7/30 11:55
 */

public class CursorWrap extends CursorWrapper {
    private static final String TAG = "CursorWrap";
    public CursorWrap(android.database.Cursor cursor) {
        super(cursor);
    }

    public Person getPerson(){
        int num=getInt(getColumnIndex(Table.cols.NUM));
        String password=getString(getColumnIndex(Table.cols.PASSWORD));
        String from=getString(getColumnIndex(Table.cols.FROM));
        String sex=getString(getColumnIndex(Table.cols.SEX));
        int score=getInt(getColumnIndex(Table.cols.SCORE));
        String subject=getString(getColumnIndex(Table.cols.SUBJECT));
        String name=getString(getColumnIndex(Table.cols.NAME));
        String uuid=getString(getColumnIndex(Table.cols.UUID));

        Person person=new Person(UUID.fromString(uuid));
        person.setNum(num);
        person.setPassword(password);
        person.setFrom(from);
        person.setScore(score);
        person.setName(name);
        person.setSex(sex);
        person.setSubject(subject);
        Log.d(TAG, getInt(getColumnIndex("_id"))+" ");
        return person;
    }
}
