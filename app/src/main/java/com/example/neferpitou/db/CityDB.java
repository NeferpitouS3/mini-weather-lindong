package com.example.neferpitou.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.neferpitou.bean.City;

import java.util.ArrayList;
import java.util.List;

/**读取数据库中的数据
 * Created by Neferpitou on 2016/10/11.
 */
public class CityDB {
    public static final String CITY_DB_NAME = "city.db";
    private static final String CITY_TABLE_NAME = "city";
    private SQLiteDatabase db;

    public CityDB(Context context,String path){
        db = context.openOrCreateDatabase(path,Context.MODE_PRIVATE,null);
    }

    public List<City> getAllCity(){
        List<City> list = new ArrayList<City>();
        //Cursor 是每行数据的集合。
        Cursor c = db.rawQuery("SELECT * from "+CITY_TABLE_NAME , null);
        while(c.moveToNext()){
            //getColumnIndex(string name)根据name返回列索引    getString(int index)根据列索引以字符串形式返回该位置的值
            String province = c.getString(c.getColumnIndex("province"));
            String city = c.getString(c.getColumnIndex("city"));
            String number = c.getString(c.getColumnIndex("number"));
            String allPY = c.getString(c.getColumnIndex("allpy"));
            String allFirstPY = c.getString(c.getColumnIndex("allfirstpy"));
            String firstPY = c.getString(c.getColumnIndex("firstpy"));
            City item = new City(province,city,number,firstPY,allPY,allFirstPY);
            list.add(item);
        }
        return list;
    }
}
