package com.example.aiy.crime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.aiy.crime.CrimeDbschema.CrimeTable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *任务描述： 单例结合数据库的CURD
 *创建时间： 2017/7/30 18:46
 */

public class CrimeLab {//数据库操作，各种添加删除查询方法，返回crime 返回list
    private static CrimeLab sCrimeLab;//单例
    private Context context;
    private SQLiteDatabase sqLiteDatabase;//数据库

    public static CrimeLab get(Context context){//该方法必须为静态
        if(sCrimeLab==null){
            sCrimeLab=new CrimeLab(context);
        }
        return sCrimeLab;
    }

    private CrimeLab(Context context){//构造方法变为私有 主要通过上面的get方法初始化。
        this.context=context.getApplicationContext();
        sqLiteDatabase=new CrimeBaseHelper(context).getWritableDatabase();
        //数据库开启读写功能。同时CrimeBaseHelper是创建数据库类，如果数据库不存在，则创建数据库。

    }
    public List<Crime> getCrime(){//返回整个数据库的list
        List<Crime>  crimes=new ArrayList<>();
        CrimeCursorWrapper cursor=queryCrimes(null,null);//由于要读整个表 所以不需要判断语句
        try{//指标操作都需要try
            cursor.moveToFirst();//移到首行
            while(!cursor.isAfterLast()){//循环 指标不是最后一行 就
                crimes.add(cursor.getCrime());
                cursor.moveToNext();
            }
        }finally {
            cursor.close();
        }
        return crimes;
    }

    public Crime getCrime(UUID id){//通过传入UUID 找到对应的crime
        CrimeCursorWrapper cursor =queryCrimes(
                CrimeTable.Cols.UUID + " = ?",//判断UUID
                new String[]{ id.toString() }
        );
        try{
            if(cursor.getCount()==0){
                return null;
            }
            cursor.moveToFirst();
            //查询完毕之后想要调用首先需要用到tofirst 因为指标位置并不是刚好在查询位置。
            return cursor.getCrime();//之后返回crime
        }finally {
            cursor.close();
        }
    }

    private static ContentValues getContentValues(Crime crime){
        //ContentValues可以用来储存值然后传给数据库
        ContentValues values=new ContentValues();
        values.put(CrimeTable.Cols.UUID,crime.getId().toString());
        values.put(CrimeTable.Cols.TITLE,crime.getTitle());
        values.put(CrimeTable.Cols.DATE,crime.getDate().getTime());
        values.put(CrimeTable.Cols.SOLVED,crime.isSolved()?1:0);
        values.put(CrimeTable.Cols.SUSPECT,crime.getSuspect());
        return values;
    }

    public void updateCrime(Crime crime){//更新数据
        String uuidString=crime.getId().toString();//调出uuid
        ContentValues values=getContentValues(crime);//把crime赋给values

        sqLiteDatabase.update(CrimeTable.NAME,values,//参数 表名，values值。
                CrimeTable.Cols.UUID +" = ?",//判断UUID和哪个一样
                new String[]{ uuidString }
        );

    }
    public void addCrime(Crime c){//插入crime例子
        ContentValues values=getContentValues(c);//同上
        sqLiteDatabase.insert(CrimeTable.NAME,null,values);

    }



    //读取数据库
    private CrimeCursorWrapper queryCrimes(String whereClause,String[] whereArgs){
        //第一个参数是SQL判断语句如：UUID +" = ?",第二个参数是new String[]{?} ?是判断语句中的。
        Cursor cursor=sqLiteDatabase.query(//query方法是读取数据
                CrimeTable.NAME,//表名
                null,
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new CrimeCursorWrapper(cursor);
        //简单来说就是 通过输入参数的判断句然后获得cursor 然后调用CrimeCursorWrapper.getCrime()
        // 把cursor取值创建一个crime例子返回。
    }
}
