package com.example.neferpitou.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Neferpitou on 2016/9/21.
 */
public class NetUtil {
    public static final int NETWORN_NONE = 0;     //无网络
    public static final int NETWORN_WIFI = 1;     //正在使用wifi
    public static final int NETWORN_MOBILE = 2;   //正在使用手机流量


    public static int getNetworkState(Context context){
        ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);  //获取系统的连接服务
        NetworkInfo networkInfo = connManager.getActiveNetworkInfo();   //获取网络连接状态
        if(networkInfo==null){
            return NETWORN_NONE;
        }

        int nType = networkInfo.getType();       //获取网络连接的类型
        if(nType == ConnectivityManager.TYPE_MOBILE){
            return  NETWORN_MOBILE;
        }else if(nType == ConnectivityManager.TYPE_WIFI){
            return NETWORN_WIFI;
        }
        return NETWORN_NONE;
    }
}
