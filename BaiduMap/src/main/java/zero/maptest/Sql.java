package zero.maptest;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Aiy on 2017/3/7.
 */

public class Sql extends SQLiteOpenHelper {
    private static final String sql_Name="map_test.db";

    public Sql(Context context, int version) {
        super(context, sql_Name, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table marker(" +
                "id integer primary key autoincrement," +
                "name integer," +
                "longtitude real," +
                "latitude real)");
        db.insert("marker",null,v1(1,109.519598,18.281098));
        db.insert("marker",null,v1(2,109.52206,18.278588));
        db.insert("marker",null,v1(3,109.516787,18.278678));
        db.insert("marker",null,v1(4,109.519167,18.276086));
        db.insert("marker",null,v1(5,109.525617,18.274078));


        db.insert("marker",null,v1(6,109.520892,18.274275));
        db.insert("marker",null,v1(7,109.523641,18.276902));
        db.insert("marker",null,v1(8,109.517766,18.277571));
        db.insert("marker",null,v1(9,109.519454,18.278918));
        db.insert("marker",null,v1(10,109.52091,18.277545));

        db.insert("marker",null,v1(11,109.522437,18.275906));
        db.insert("marker",null,v1(12,109.517945,18.275022));
        db.insert("marker",null,v1(13,109.519679,18.273108));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    private ContentValues v1(int name, double latitude, double longtitude){
        ContentValues values=new ContentValues();
        values.put("name",name);
        values.put("latitude",latitude);
        values.put("longtitude",longtitude);
        return values;
    }
}
