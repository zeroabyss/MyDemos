package zero.maptest;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Aiy on 2017/3/7.
 */

public class Copy {
    private SQLiteDatabase database;
    public Copy(Context context){
        database=new Sql(context,1).getWritableDatabase();
    }
    public void init(){

        database.insert("marker",null,v1(1,109.519598,18.281098));
        database.insert("marker",null,v1(2,109.514711,18.276807));
        database.insert("marker",null,v1(3,109.52082,18.275056));
        database.insert("marker",null,v1(4,109.525958,18.274095));
        database.insert("marker",null,v1(5,109.53584,18.279245));
    }

    private ContentValues v1(int name,double latitude,double longtitude){
        ContentValues values=new ContentValues();
        values.put("name",name);
        values.put("latitude",latitude);
        values.put("longtitude",longtitude);
        return values;
    }
}
