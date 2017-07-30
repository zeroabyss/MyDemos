package zero.management;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import zero.management.Datababse_Values.Table;

/**
 *任务描述： 单例，获得学生信息
 *创建时间： 2017/7/30 11:50
 */

public class Person_Lib {
    private volatile static Person_Lib personLib;
    private Context context;
    private SQLiteDatabase database;

    public static Person_Lib getPersonLib(Context context){
        if (personLib==null){
            synchronized (Person_Lib.class){
                if (personLib==null){
                    personLib=new Person_Lib(context);
                }
            }
        }
        return personLib;
    }
    /**
     * 方法简述： 构造方法，将context变成App的，这是防止内存泄露，因为单例的生命周期跟APP是一样长的，所以应该设置成APP的context不然活动没办法回收。
     */
    private Person_Lib(Context context){
        context=context.getApplicationContext();
        //获得数据库写入功能
        database=new Database_db(context).getWritableDatabase();
    }

    /**
     * 方法简述： 查询功能
     * @param IF 是“=”前面的
     * @Param IF_num 是等号后面的
     */
    private CursorWrap query(String IF, String[] IF_num){
        Cursor cursor=database.query(
                Table.TABLE_NAME,
                null,
                IF,
                IF_num,
                null,
                null,
                null
        );
        return new CursorWrap(cursor);
    }
    /**
     * 方法简述： 这是返回所有学生信息的list
     */
    public List<Person> getList(){
        List<Person> list=new ArrayList<>();
        CursorWrap cursor=query(null,null);
        try {
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                list.add(cursor.getPerson());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return list;
    }
    
    /**
     * 方法简述： 通过学号查询学生信息
     */
    public Person getPerson(int num){
        CursorWrap cursor=query(Table.cols.NUM+" = ?",
                new String[] { String.valueOf(num)});
        try{
            if (cursor.getCount()<0){
                //这里是cursor=null 出现意外
                return  null;  
            } else if(cursor.getCount()==0){
                //这里是cursor查到的是空的信息，说明没有人符合
                return null;
            }
            cursor.moveToFirst();
            return cursor.getPerson();
        }finally {
            cursor.close();
        }
    }
    
    /**
     *任务描述： 删除一个学生
     *创建时间： 2017/7/30 12:02
     */
    public void Delete(int num){
        database.delete(Table.TABLE_NAME,Table.cols.NUM+"=?",new String[]{String.valueOf(num)});
    }
    
    /**
     *任务描述： 标准构建一个ContentValues，让添加和更新功能使用。
     *创建时间： 2017/7/30 12:03
     */
    private static ContentValues getValuse(Person person){
        ContentValues values=new ContentValues();
        values.put(Table.cols.FROM,person.getFrom());
        values.put(Table.cols.NAME,person.getName());
        values.put(Table.cols.PASSWORD,person.getPassword());
        values.put(Table.cols.NUM,person.getNum());
        values.put(Table.cols.SCORE,person.getScore());
        values.put(Table.cols.SUBJECT,person.getSubject());
        values.put(Table.cols.SEX,person.getSex());
        values.put(Table.cols.UUID,person.getUuid().toString());
        return values;
    }

    public void addPerson(Person person){
        ContentValues values=getValuse(person);
        database.insert(Table.TABLE_NAME,null,values);
    }

    public void updatePerson(Person person){
        ContentValues values=getValuse(person);
        database.update(Table.TABLE_NAME,values,Table.cols.UUID +" = ?"
                ,new String[]{ person.getUuid().toString()});
    }
}
