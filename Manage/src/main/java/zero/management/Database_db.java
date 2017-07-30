package zero.management;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import zero.management.Datababse_Values.Table;


/**
 *任务描述： 数据库的建表
 *创建时间： 2017/7/30 11:49
 */

public class Database_db extends SQLiteOpenHelper {
    private static final int VERSION=1;
    private static final String DATABASE_NAME="manage.db";

    public Database_db(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Table.TABLE_NAME +"("+
                " _id integer primary key autoincrement, "+
                Table.cols.NUM+" int, "+
                Table.cols.PASSWORD+", "+
                Table.cols.NAME+", "+
                Table.cols.SEX+", "+
                Table.cols.SUBJECT+", "+
                Table.cols.SCORE+", "+
                Table.cols.FROM+", "+
                Table.cols.UUID+
                ")"

        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
