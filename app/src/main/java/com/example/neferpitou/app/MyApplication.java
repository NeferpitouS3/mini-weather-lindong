package com.example.neferpitou.app;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.example.neferpitou.bean.City;
import com.example.neferpitou.db.CityDB;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neferpitou on 2016/10/11.
 */
public class MyApplication extends Application{
    private static final String TAG = "MyAPP";
    private static MyApplication mApplication;
    private CityDB mCityDB;
    private List<City> mCityList;

    {
        PlatformConfig.setWeixin("wx967daebe835fbeac", "5bb696d9ccd75a38c8a0bfe0675559b3");
        PlatformConfig.setSinaWeibo("419943956","bb56e2e5c91892c5d8c7bf25859d1a67");
        Config.REDIRECT_URL = "www.baidu.com";
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"MyApplication->Oncreate");
        UMShareAPI.get(this);
        mApplication = this;
        mCityDB = openCityDB();
        initCityList();
    }

    private void initCityList(){
        mCityList = new ArrayList<City>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareCityList();
            }
        }).start();
    }

    private boolean prepareCityList(){
        mCityList = mCityDB.getAllCity();
        int i=0;
        for(City city:mCityList){
            i++;
            String cityName = city.getCity();
            String cityCode = city.getNumber();
            Log.d(TAG,cityCode+":"+cityName);
        }
        Log.d(TAG,"城市数="+i);
        return true;
    }

    public List<City> getCityList(){
        return mCityList;
    }

    public static MyApplication getInstance(){
        return mApplication;
    }

    private CityDB openCityDB(){
        //打开数据库
        String path ="/data"
                + Environment.getDataDirectory().getAbsolutePath()        //内容为/data
                + File.separator+getPackageName()                       //内容分别为/和com.example.neferpitou.myweather
                + File.separator+"database1"
                + CityDB.CITY_DB_NAME;
        File db = new File(path);
        Log.d(TAG,path);
        //创建数据库
        if(!db.exists()){
            String pathfolder ="/data"
                    + Environment.getDataDirectory().getAbsolutePath()
                    + File.separator+getPackageName()
                    + File.separator+"database1"
                    + File.separator;
            File dirFirstFolder = new File(pathfolder);
            if(!dirFirstFolder.exists()){
                dirFirstFolder.mkdirs();
                Log.i("MyAPP","mkdirs");
            }
            Log.i("MyAPP","db is not exists");
            try {
                InputStream is = getAssets().open("city.db");
                FileOutputStream fos = new FileOutputStream(db);
                int len=-1;
                byte[] buffer = new byte[1024];
                while((len=is.read(buffer))!=-1){
                    fos.write(buffer,0,len);
                    fos.flush();
                }
                fos.close();
                is.close();
            }catch (IOException e){
                e.printStackTrace();
                System.exit(0);
            }
        }
        return new CityDB(this,path);
    }
}
