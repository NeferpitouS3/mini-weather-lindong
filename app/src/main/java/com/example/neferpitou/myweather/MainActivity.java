package com.example.neferpitou.myweather;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.neferpitou.bean.TodayWeather;
import com.example.neferpitou.location.MyLocationListener;
import com.example.neferpitou.service.MyService;
import com.example.neferpitou.util.NetUtil;
import com.example.neferpitou.viewpager.ViewPagerAdapter;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.BDNotifyListener;//假如用到位置提醒功能，需要import该类
import com.baidu.location.Poi;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by Neferpitou on 2016/9/20.
 */
public class MainActivity extends Activity implements View.OnClickListener,ViewPager.OnPageChangeListener{
    private ImageView mUpdataBtn;
    private ImageView mShareBtn;
    private ImageView mCitySelect;
    private ProgressBar mProgressBar;
    private Animation animation;
    private ImageView mLocation;

    private TextView cityTv,timeTv,humidityTv,weekTv,pmDataTv,pmQualityTv,temperatureTv,climateTv,windTv,city_name_Tv,nowTemperatureTv;
    private ImageView weatherImg,pmImg;

    private TextView yesterday_dateTv,yesterday_temperatureTv,yesterday_typeTv,yesterday_fengliTv;
    private TextView today_dateTv,today_temperatureTv,today_typeTv,today_fengliTv;
    private TextView future_date1Tv,future_temperature1Tv,future_type1Tv,future_fengli1Tv;
    private TextView future_date2Tv,future_temperature2Tv,future_type2Tv,future_fengli2Tv;
    private TextView future_date3Tv,future_temperature3Tv,future_type3Tv,future_fengli3Tv;
    private TextView future_date4Tv,future_temperature4Tv,future_type4Tv,future_fengli4Tv;
    private ImageView yesterday_imgTv,today_imgTv,future_img1Tv,future_img2Tv,future_img3Tv,future_img4Tv;

    private static final int UPDATE_TODAY_WEATHER = 1;
    String newCityCode="101010100";         //用来保证每次刷新都是当前的城市
    String cityName ="北京";            //用户修改SelectCity里标题的名字。
//    String cityCodeToSelectCity = "101010100";

    private ViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private List<View> views;
    private ImageView[] dots;
    private int[] ids={R.id.iv1,R.id.iv2};








    Intent serviceIntent;
//    MyService serviceBinder;
    IntentFilter intentFilter;





    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();

    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {

            Toast.makeText(MainActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            if(t!=null){
                Toast.makeText(MainActivity.this,platform + t.getMessage(), Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this,platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(MainActivity.this,platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    public void initFirst() {

        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation location) {
                Log.d("why","???");
                //Receive Location
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                sb.append(location.getTime());
                sb.append("\nerror code : ");
                sb.append(location.getLocType());
                sb.append("\nlatitude : ");
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");
                sb.append(location.getLongitude());
                sb.append("\nradius : ");
                sb.append(location.getRadius());
                Log.d("why",location.getLocType()+"");
                if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 单位：公里每小时
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 单位：米
                    sb.append("\ndirection : ");
                    sb.append(location.getDirection());// 单位度
                    sb.append("\naddr : ");
                    sb.append(location.getAddrStr());
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");

                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
                    sb.append("\naddr : ");
                    sb.append(location.getAddrStr());
                    //运营商信息
                    sb.append("\noperationers : ");
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }
                sb.append("\nlocationdescribe : ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                List<Poi> list = location.getPoiList();// POI数据
                if (list != null) {
                    sb.append("\npoilist size = : ");
                    sb.append(list.size());
                    for (Poi p : list) {
                        sb.append("\npoi= : ");
                        sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                    }
                }
                Log.i("BaiduLocationApiDem", sb.toString());
                Log.d("why",location.getAddrStr());
                Log.d("why",location.getCity());
                Log.d("why",location.getCityCode());
                Log.d("why",sb.toString());
                mLocationClient.stop();
            }
        });    //注册监听函数
    }


    private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

















































    @Override
    protected void onResume() {
        super.onResume();
        intentFilter = new IntentFilter();
        intentFilter.addAction("UPDATE_NOW");

        registerReceiver(intentReceiver,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(intentReceiver);
    }

    @Override
    protected void onDestroy() {
        stopService(serviceIntent);
        super.onDestroy();
    }

    private BroadcastReceiver intentReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            queryWeatherCode(newCityCode);
        }
    };



    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((TodayWeather)msg.obj);
                    cityName = ((TodayWeather) msg.obj).getCity();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_info);
        Log.d("myWeather", "程序开始");

        SharedPreferences sharedPreferences = getSharedPreferences("first", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.clear().commit();
        int count = sharedPreferences.getInt("Count",0);
        if(count == 0){
            Intent intent = new Intent(MainActivity.this,GuideActivity.class);
            startActivity(intent);
            finish();
        }


        mProgressBar = (ProgressBar)findViewById(R.id.title_update_progress);
        animation = AnimationUtils.loadAnimation(this, R.anim.rotate_update);
        LinearInterpolator linearInterpolator = new LinearInterpolator();    //设置动画匀速运动
        animation.setInterpolator(linearInterpolator);

        mShareBtn  = (ImageView) findViewById(R.id.title_share);
        mShareBtn.setOnClickListener(this);
        mUpdataBtn = (ImageView) findViewById(R.id.title_update_btn);
        mUpdataBtn.setOnClickListener(this);    //在该类里实现了点击接口（下面的onClick方法）
        mLocation = (ImageView)findViewById(R.id.title_location);
        mLocation.setOnClickListener(this);
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
            Log.d("myWeather", "网络OK");         //在logcat输出debug
//            Toast.makeText(MainActivity.this, "网络OK!", Toast.LENGTH_LONG).show();
        } else if(NetUtil.getNetworkState(this) == NetUtil.NETWORN_NONE){
            Log.d("myWeather", "网络挂了");
            Toast.makeText(MainActivity.this, "网络挂了!", Toast.LENGTH_LONG).show();
        }
        mCitySelect = (ImageView)findViewById(R.id.title_city_manager);
        mCitySelect.setOnClickListener(this);

        initPagers();
        initDots();

        initView();






        serviceIntent = new Intent(this,MyService.class);
        startService(serviceIntent);
//        bindService(serviceIntent,connection, Context.BIND_AUTO_CREATE);
    }



    void initView(){
        city_name_Tv = (TextView) findViewById(R.id.title_city_name);
        cityTv = (TextView) findViewById(R.id.city);
        timeTv = (TextView) findViewById(R.id.time);
        humidityTv = (TextView) findViewById(R.id.humidity);
        weekTv = (TextView) findViewById(R.id.week_today);
        pmDataTv = (TextView) findViewById(R.id.pm_data);
        pmQualityTv = (TextView) findViewById(R.id.pm2_5_quality);
        pmImg = (ImageView) findViewById(R.id.pm2_5_img);
        temperatureTv = (TextView) findViewById(R.id.temperature);
        nowTemperatureTv =(TextView)findViewById(R.id.nowtemperature);
        climateTv = (TextView) findViewById(R.id.climate);
        windTv = (TextView) findViewById(R.id.wind);
        weatherImg = (ImageView) findViewById(R.id.weather_img);
        city_name_Tv.setText("N/A");
        cityTv.setText("N/A");
        timeTv.setText("N/A");
        humidityTv.setText("N/A");
        pmDataTv.setText("N/A");
        pmQualityTv.setText("N/A");
        weekTv.setText("N/A");
        temperatureTv.setText("N/A");
        climateTv.setText("N/A");
        windTv.setText("N/A");
        nowTemperatureTv.setText("N/A");
        //viewpage里的昨天天气
        yesterday_dateTv = (TextView)views.get(0).findViewById(R.id.yesterday_date);
        yesterday_temperatureTv = (TextView)views.get(0).findViewById(R.id.yesterday_temperature);
        yesterday_typeTv = (TextView)views.get(0).findViewById(R.id.yesterday_type);
        yesterday_fengliTv = (TextView)views.get(0).findViewById(R.id.yesterday_fengli);
        yesterday_imgTv = (ImageView)views.get(0).findViewById(R.id.yesterday_img);
        //viewpage里的今天天气
        today_dateTv = (TextView)views.get(0).findViewById(R.id.today_date);
        today_temperatureTv = (TextView)views.get(0).findViewById(R.id.today_temperature);
        today_typeTv = (TextView)views.get(0).findViewById(R.id.today_type);
        today_fengliTv = (TextView)views.get(0).findViewById(R.id.today_fengli);
        today_imgTv = (ImageView)views.get(0).findViewById(R.id.today_img);
        //viewpage里的明天天气
        future_date1Tv = (TextView)views.get(0).findViewById(R.id.future_date1);
        future_temperature1Tv = (TextView)views.get(0).findViewById(R.id.future_temperature1);
        future_type1Tv = (TextView)views.get(0).findViewById(R.id.future_type1);
        future_fengli1Tv = (TextView)views.get(0).findViewById(R.id.future_fengli1);
        future_img1Tv = (ImageView)views.get(0).findViewById(R.id.future_img1);
        //viewpage里的后天天气
        future_date2Tv = (TextView)views.get(1).findViewById(R.id.future_date2);
        future_temperature2Tv = (TextView)views.get(1).findViewById(R.id.future_temperature2);
        future_type2Tv = (TextView)views.get(1).findViewById(R.id.future_type2);
        future_fengli2Tv = (TextView)views.get(1).findViewById(R.id.future_fengli2);
        future_img2Tv = (ImageView)views.get(1).findViewById(R.id.future_img2);
        //viewpage里的大后天天气
        future_date3Tv = (TextView)views.get(1).findViewById(R.id.future_date3);
        future_temperature3Tv = (TextView)views.get(1).findViewById(R.id.future_temperature3);
        future_type3Tv = (TextView)views.get(1).findViewById(R.id.future_type3);
        future_fengli3Tv = (TextView)views.get(1).findViewById(R.id.future_fengli3);
        future_img3Tv = (ImageView)views.get(1).findViewById(R.id.future_img3);
        //viewpage里的大大后台天气
        future_date4Tv = (TextView)views.get(1).findViewById(R.id.future_date4);
        future_temperature4Tv = (TextView)views.get(1).findViewById(R.id.future_temperature4);
        future_type4Tv = (TextView)views.get(1).findViewById(R.id.future_type4);
        future_fengli4Tv = (TextView)views.get(1).findViewById(R.id.future_fengli4);
        future_img4Tv = (ImageView)views.get(1).findViewById(R.id.future_img4);












    }

    void initDots(){
        dots = new ImageView[views.size()];
        for(int i=0;i<views.size();i++){
            dots[i] = (ImageView)findViewById(ids[i]);
        }
    }

    private void initPagers(){
        LayoutInflater inflater = LayoutInflater.from(this);
        views = new ArrayList<View>();
        views.add(inflater.inflate(R.layout.forecast_info1,null));
        views.add(inflater.inflate(R.layout.forecast_info2,null));
        viewPagerAdapter = new ViewPagerAdapter(views,this);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        viewPager.setAdapter(viewPagerAdapter);


        viewPager.addOnPageChangeListener(this);

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for(int a=0;a<ids.length;a++){
            if(a == position){
                dots[a].setImageResource(R.drawable.page_indicator_focused);
            }else{
                dots[a].setImageResource(R.drawable.page_indicator_unfocused);
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }



    private TodayWeather parseXML(String xmldata) {
        TodayWeather todayWeather = null;
        int fengxiangCount = 0;
        int fengliCount = 0;
        int dateCount = 0;
        int highCount = 0;
        int lowCount = 0;
        int typeCount = 0;

        int yesterdayTypeCount = 0;
        int yesterdayFengliCount = 0;

        int future1TypeCount = 0;
        int future1FengliCount = 0;
        int future2TypeCount = 0;
        int future2FengliCount = 0;
        int future3TypeCount = 0;
        int future3FengliCount = 0;
        int future4TypeCount = 0;
        int future4FengliCount = 0;

        int forecastTimes = 0;
        try {
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = fac.newPullParser();
            xmlPullParser.setInput(new StringReader(xmldata));
            int eventType = xmlPullParser.getEventType();
            Log.d("myWeather", "parseXML");
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        if(xmlPullParser.getName().equals("resp")){
                            todayWeather = new TodayWeather();
                        }
                        if(todayWeather!=null) {
                            if (xmlPullParser.getName().equals("city")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setCity(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("updatetime")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setUpdatetime(xmlPullParser.getText());
                            }else if (xmlPullParser.getName().equals("shidu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setShidu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("wendu")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setWendu(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("pm25")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setPm25(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("quality")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setQuality(xmlPullParser.getText());
                            } else if (xmlPullParser.getName().equals("fengxiang") && fengxiangCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengxiang(xmlPullParser.getText());
                                fengxiangCount++;
                            } else if (xmlPullParser.getName().equals("fengli") && fengliCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setFengli(xmlPullParser.getText());
                                fengliCount++;
                            } else if (xmlPullParser.getName().equals("date") && dateCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setDate(xmlPullParser.getText());
                                dateCount++;
                            } else if (xmlPullParser.getName().equals("high") && highCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setHigh(xmlPullParser.getText());
                                highCount++;
                            } else if (xmlPullParser.getName().equals("low") && lowCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setLow(xmlPullParser.getText());
                                lowCount++;
                            } else if (xmlPullParser.getName().equals("type") && typeCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setType(xmlPullParser.getText());
                                typeCount++;
                            }
                            //接下来获取的是viewpage内的最近6天的数据（从昨天到未来第四天,今天的内容上面已经解析）
                            //昨天
                            else if(xmlPullParser.getName().equals("date_1")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setYesterdayDate(xmlPullParser.getText());
                            }else if(xmlPullParser.getName().equals("high_1")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setYesterdayHigh(xmlPullParser.getText());
                            }else if(xmlPullParser.getName().equals("low_1")) {
                                eventType = xmlPullParser.next();
                                todayWeather.setYesterdayLow(xmlPullParser.getText());
                            }else if(xmlPullParser.getName().equals("type_1") && yesterdayTypeCount == 0) {
                                eventType = xmlPullParser.next();
                                todayWeather.setYesterdayType(xmlPullParser.getText());
                                yesterdayTypeCount++;
                            }else if(xmlPullParser.getName().equals("fl_1") && yesterdayFengliCount == 0){
                                eventType = xmlPullParser.next();
                                todayWeather.setYesterdayFengli(xmlPullParser.getText());
                                yesterdayFengliCount++;
                            }
                            //明天
                            else if(forecastTimes == 1){
                                if(xmlPullParser.getName().equals("date")){
                                    eventType = xmlPullParser.next();
                                    todayWeather.setFutureDate1(xmlPullParser.getText());
                                }else if(xmlPullParser.getName().equals("high")){
                                    eventType = xmlPullParser.next();
                                    todayWeather.setFutureHigh1(xmlPullParser.getText());
                                }else if(xmlPullParser.getName().equals("low")){
                                    eventType = xmlPullParser.next();
                                    todayWeather.setFutureLow1(xmlPullParser.getText());
                                }else if(xmlPullParser.getName().equals("type") && future1TypeCount ==0){
                                    eventType = xmlPullParser.next();
                                    todayWeather.setFutureType1(xmlPullParser.getText());
                                    future1TypeCount++;
                                }else if(xmlPullParser.getName().equals("fengli") && future1FengliCount==0){
                                    eventType = xmlPullParser.next();
                                    todayWeather.setFutureFengli1(xmlPullParser.getText());
                                    future1FengliCount++;
                                }
                            }
                            //后天
                            else if(forecastTimes == 2){
                                if(xmlPullParser.getName().equals("date")){
                                    eventType = xmlPullParser.next();
                                    todayWeather.setFutureDate2(xmlPullParser.getText());
                                }else if(xmlPullParser.getName().equals("high")){
                                    eventType = xmlPullParser.next();
                                    todayWeather.setFutureHigh2(xmlPullParser.getText());
                                }else if(xmlPullParser.getName().equals("low")){
                                    eventType = xmlPullParser.next();
                                    todayWeather.setFutureLow2(xmlPullParser.getText());
                                }else if(xmlPullParser.getName().equals("type") && future2TypeCount ==0){
                                    eventType = xmlPullParser.next();
                                    todayWeather.setFutureType2(xmlPullParser.getText());
                                    future2TypeCount++;
                                }else if(xmlPullParser.getName().equals("fengli") && future2FengliCount==0){
                                    eventType = xmlPullParser.next();
                                    todayWeather.setFutureFengli2(xmlPullParser.getText());
                                    future2FengliCount++;
                                }
                            }
                            //大后天
                            else if(forecastTimes == 3){
                                if(xmlPullParser.getName().equals("date")){
                                    eventType = xmlPullParser.next();
                                    todayWeather.setFutureDate3(xmlPullParser.getText());
                                }else if(xmlPullParser.getName().equals("high")){
                                    eventType = xmlPullParser.next();
                                    todayWeather.setFutureHigh3(xmlPullParser.getText());
                                }else if(xmlPullParser.getName().equals("low")){
                                    eventType = xmlPullParser.next();
                                    todayWeather.setFutureLow3(xmlPullParser.getText());
                                }else if(xmlPullParser.getName().equals("type") && future3TypeCount ==0){
                                    eventType = xmlPullParser.next();
                                    todayWeather.setFutureType3(xmlPullParser.getText());
                                    future3TypeCount++;
                                }else if(xmlPullParser.getName().equals("fengli") && future3FengliCount==0){
                                    eventType = xmlPullParser.next();
                                    todayWeather.setFutureFengli3(xmlPullParser.getText());
                                    future3FengliCount++;
                                }
                            }
                            //大大后天
                            else if(forecastTimes == 4){
                                if(xmlPullParser.getName().equals("date")){
                                    eventType = xmlPullParser.next();
                                    todayWeather.setFutureDate4(xmlPullParser.getText());
                                }else if(xmlPullParser.getName().equals("high")){
                                    eventType = xmlPullParser.next();
                                    todayWeather.setFutureHigh4(xmlPullParser.getText());
                                }else if(xmlPullParser.getName().equals("low")){
                                    eventType = xmlPullParser.next();
                                    todayWeather.setFutureLow4(xmlPullParser.getText());
                                }else if(xmlPullParser.getName().equals("type") && future4TypeCount ==0){
                                    eventType = xmlPullParser.next();
                                    todayWeather.setFutureType4(xmlPullParser.getText());
                                    future4TypeCount++;
                                }else if(xmlPullParser.getName().equals("fengli") && future4FengliCount==0){
                                    eventType = xmlPullParser.next();
                                    todayWeather.setFutureFengli4(xmlPullParser.getText());
                                    future4FengliCount++;
                                }
                            }

                        }
                        break;

                    case XmlPullParser.END_TAG:
                        if(xmlPullParser.getName().equals("weather")){
                            forecastTimes++;
                        }
                        break;
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return todayWeather;
    }

    /**
     * @param cityCode
     */
    private void queryWeatherCode(String cityCode) {
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey=" + cityCode;
        Log.d("myWeather", address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                TodayWeather todayWeather = null;
                try {
                    URL url = new URL(address);
                    con = (HttpURLConnection) url.openConnection();  //返回一个HttpURLConnection对象,它表示到URL所引用的远程对象的连接。
                    con.setRequestMethod("GET");
                    con.setConnectTimeout(8000);    //设置连接超时毫秒
                    con.setReadTimeout(8000);       //设置读取超时毫秒

                    InputStream in = con.getInputStream();       //得到网络返回的输入流

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while ((str = reader.readLine()) != null) {
                        response.append(str);
                        Log.d("myWeather", str);
                    }

                    String responseStr = response.toString();
                    Log.d("myWeather", responseStr);
                    todayWeather=parseXML(responseStr);
                    if(todayWeather!=null){
                        Log.d("myWeather",todayWeather.toString());
                     //   updateTodayWeather(todayWeather);    因为子线程里无法对UI进行操作，所以不能直接调用更新函数，需要先穿Message回主函数
                        Message msg = new Message();
                        msg.what = UPDATE_TODAY_WEATHER;
                        msg.obj = todayWeather;
                        mHandler.sendMessage(msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (con != null) {
                        con.disconnect();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.title_update_btn) {

            mUpdataBtn.setVisibility(View.INVISIBLE);
            mProgressBar.startAnimation(animation);

            Log.d("myWeather", "点刷新");
            SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
            String cityCode = sharedPreferences.getString("main_city_code","101010100");  //此北京 101010100，兰州 101160101 苍南101210709
//            SharedPreferences.Editor editor = sharedPreferences.edit();
//            editor.clear().commit();    //把SharedPreferences保存的数据清空了。
            //这段是我自己新加的功能
            if(newCityCode!=null){
                cityCode = newCityCode;
            }
            Log.d("myWeather", cityCode);

            if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                Log.d("myWeather", "网络OK");
                queryWeatherCode(cityCode);
            } else {
                Log.d("myWeather", "网络挂了");
                Toast.makeText(MainActivity.this, "网络挂了!", Toast.LENGTH_LONG).show();
            }
        }
        if(view.getId()==R.id.title_city_manager){
            Log.d("myWeather", "点选择城市");
            Intent i = new Intent(MainActivity.this,SelectCity.class);
//            startActivity(i);
            i.putExtra("city",cityName);
//            i.putExtra("CityCode",cityCodeToSelectCity);

            startActivityForResult(i,1);  // (Intent intent,int requestCode)
        }
        if(view.getId()==R.id.title_location){
            initFirst();
            Log.d("why","1");
            initLocation();
            mLocationClient.start();

            Log.d("why","2");
            Log.d("why","3");
//            BDLocation bd = mLocationClient.getLastKnownLocation();
//            if(bd == null){
//                Log.d("why","1");
//            }
            Log.d("why",""+mLocationClient.getAccessKey());
        }
        if(view.getId()==R.id.title_share){

            new ShareAction(MainActivity.this).withText(shareMess)
                    .setDisplayList(SHARE_MEDIA.SMS,SHARE_MEDIA.SINA,SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                    .setCallback(umShareListener).open();
        }
    }

    protected void onActivityResult(int requestCode ,int resultCode , Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode==RESULT_OK){
                    mUpdataBtn.setVisibility(View.INVISIBLE);
                    mProgressBar.startAnimation(animation);
                    newCityCode = data.getStringExtra("cityCode");
//                    cityCodeToSelectCity = newCityCode;
                    Log.d("myWeather","选择的城市代码为"+newCityCode);

                    if (NetUtil.getNetworkState(this) != NetUtil.NETWORN_NONE) {
                        Log.d("myWeather", "网络OK");
                        queryWeatherCode(newCityCode);
                    } else {
                        Log.d("myWeather", "网络挂了");
                        Toast.makeText(MainActivity.this, "网络挂了!", Toast.LENGTH_LONG).show();
                    }
                }
                break;
            default:
                break;
        }
    }

    String shareMess;
    void updateTodayWeather(TodayWeather todayWeather){
        String pm = todayWeather.getPm25();
        double pmInt;
        if(pm==null){
            pmInt = 0;
            pm = "0";
        }else{
            pmInt = Double.parseDouble(pm);
        }
        String quality = todayWeather.getQuality();
        if(quality == null){
            pmQualityTv.setText("无");
        }else{
            pmQualityTv.setText(todayWeather.getQuality());
        }

        String climate = todayWeather.getType();
        city_name_Tv.setText(todayWeather.getCity()+"天气");
        cityTv.setText(todayWeather.getCity());
        timeTv.setText(todayWeather.getUpdatetime()+"发布");
        humidityTv.setText("湿度："+todayWeather.getShidu());
        weekTv.setText(todayWeather.getDate());
        pmDataTv.setText(pm);
        temperatureTv.setText(todayWeather.getHigh()+"~"+todayWeather.getLow());
        climateTv.setText(climate);
        nowTemperatureTv.setText("温度："+todayWeather.getWendu()+"℃");
        windTv.setText("风力："+todayWeather.getFengli());
        Toast.makeText(MainActivity.this,"更新成功！",Toast.LENGTH_SHORT).show();

        shareMess = todayWeather.getCity()+":"+todayWeather.getDate()+",天气为:"+climate+",温度为:"+todayWeather.getHigh()+"~"+todayWeather.getLow()
                +",湿度为:"+todayWeather.getShidu()+",风力为:"+todayWeather.getFengli()+",pm2.5为:"+pm;
        if(pmInt<=50){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_0_50);
        }else if(pmInt<=100){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_51_100);
        }else if(pmInt<=150){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_101_150);
        }else if(pmInt<=200){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_151_200);
        }else if(pmInt<=300){
            pmImg.setImageResource(R.drawable.biz_plugin_weather_201_300);
        }else {
            pmImg.setImageResource(R.drawable.biz_plugin_weather_greater_300);
        }
        if(climate.equals("晴")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_qing);
        }else if(climate.equals("暴雪")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        }else if(climate.equals("暴雨")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_baoyu);
        }else if(climate.equals("大暴雪")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        }else if(climate.equals("大雪")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_daxue);
        }else if(climate.equals("大雨")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_dayu);
        }else if(climate.equals("多云")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        }else if(climate.equals("雷阵雨")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        }else if(climate.equals("雷阵雨冰雹")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        }else if(climate.equals("沙尘暴")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_shachenbao);
        }else if(climate.equals("特大暴雨")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu);
        }else if(climate.equals("雾")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_wu);
        }else if(climate.equals("小雨")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        }else if(climate.equals("小雪")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        }else if(climate.equals("阴")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_yin);
        }else if(climate.equals("雨夹雪")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        }else if(climate.equals("阵雪")) {
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        }else if(climate.equals("阵雨")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
        }else if(climate.equals("中雪")){
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
        }else if(climate.equals("中雨")) {
            weatherImg.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
        }

        yesterday_dateTv.setText(todayWeather.getYesterdayDate().substring(todayWeather.getYesterdayDate().indexOf('日')+1));
        yesterday_temperatureTv.setText(todayWeather.getYesterdayLow().substring(3)+"~"+todayWeather.getYesterdayHigh().substring(3));
        yesterday_typeTv.setText(todayWeather.getYesterdayType());
        yesterday_fengliTv.setText(todayWeather.getYesterdayFengli());

        today_dateTv.setText(todayWeather.getDate().substring(todayWeather.getDate().indexOf('日')+1));
        today_temperatureTv.setText(todayWeather.getLow().substring(3)+"~"+todayWeather.getHigh().substring(3));
        today_typeTv.setText(todayWeather.getType());
        today_fengliTv.setText(todayWeather.getFengli());

        future_date1Tv.setText(todayWeather.getFutureDate1().substring(todayWeather.getFutureDate1().indexOf('日')+1));
        future_temperature1Tv.setText(todayWeather.getFutureLow1().substring(3)+"~"+todayWeather.getFutureHigh1().substring(3));
        future_type1Tv.setText(todayWeather.getFutureType1());
        future_fengli1Tv.setText(todayWeather.getFutureFengli1());

        future_date2Tv.setText(todayWeather.getFutureDate2().substring(todayWeather.getFutureDate2().indexOf('日')+1));
        future_temperature2Tv.setText(todayWeather.getFutureLow2().substring(3)+"~"+todayWeather.getFutureHigh2().substring(3));
        future_type2Tv.setText(todayWeather.getFutureType2());
        future_fengli2Tv.setText(todayWeather.getFutureFengli2());

        future_date3Tv.setText(todayWeather.getFutureDate3().substring(todayWeather.getFutureDate3().indexOf('日')+1));
        future_temperature3Tv.setText(todayWeather.getFutureLow3().substring(3)+"~"+todayWeather.getFutureHigh3().substring(3));
        future_type3Tv.setText(todayWeather.getFutureType3());
        future_fengli3Tv.setText(todayWeather.getFutureFengli3());

        future_date4Tv.setText(todayWeather.getFutureDate4().substring(todayWeather.getFutureDate4().indexOf('日')+1));
        future_temperature4Tv.setText(todayWeather.getFutureLow4().substring(3)+"~"+todayWeather.getFutureHigh4().substring(3));
        future_type4Tv.setText(todayWeather.getFutureType4());
        future_fengli4Tv.setText(todayWeather.getFutureFengli4());

        String[] typeArr = {todayWeather.getYesterdayType(),todayWeather.getType(),todayWeather.getFutureType1(),todayWeather.getFutureType2(),
                todayWeather.getFutureType3(),todayWeather.getFutureType4()};
        ImageView[] imgArr = {yesterday_imgTv,today_imgTv,future_img1Tv,future_img2Tv,future_img3Tv,future_img4Tv};
        for(int i=0;i<typeArr.length;i++){
            String s = typeArr[i];
            ImageView tv = imgArr[i];
            switch (s){
                case "晴":
                    tv.setImageResource(R.drawable.biz_plugin_weather_qing1);
                    break;
                case "暴雪":
                    tv.setImageResource(R.drawable.biz_plugin_weather_baoxue1);
                    break;
                case "大暴雪":
                    tv.setImageResource(R.drawable.biz_plugin_weather_dabaoyu1);
                    break;
                case "大雪":
                    tv.setImageResource(R.drawable.biz_plugin_weather_daxue1);
                    break;
                case "大雨":
                    tv.setImageResource(R.drawable.biz_plugin_weather_dayu1);
                    break;
                case "多云":
                    tv.setImageResource(R.drawable.biz_plugin_weather_duoyun1);
                    break;
                case "雷阵雨":
                    tv.setImageResource(R.drawable.biz_plugin_weather_leizhenyu1);
                    break;
                case "雷阵雨冰雹":
                    tv.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao1);
                    break;
                case "沙尘暴":
                    tv.setImageResource(R.drawable.biz_plugin_weather_shachenbao1);
                    break;
                case "特大暴雨":
                    tv.setImageResource(R.drawable.biz_plugin_weather_tedabaoyu1);
                    break;
                case "雾":
                    tv.setImageResource(R.drawable.biz_plugin_weather_wu1);
                    break;
                case "小雨":
                    tv.setImageResource(R.drawable.biz_plugin_weather_xiaoyu1);
                    break;
                case "小雪":
                    tv.setImageResource(R.drawable.biz_plugin_weather_xiaoxue1);
                    break;
                case "阴":
                    tv.setImageResource(R.drawable.biz_plugin_weather_yin1);
                    break;
                case "雨夹雪":
                    tv.setImageResource(R.drawable.biz_plugin_weather_yujiaxue1);
                    break;
                case "阵雪":
                    tv.setImageResource(R.drawable.biz_plugin_weather_zhenxue1);
                    break;
                case "阵雨":
                    tv.setImageResource(R.drawable.biz_plugin_weather_zhenyu1);
                    break;
                case "中雪":
                    tv.setImageResource(R.drawable.biz_plugin_weather_zhongxue1);
                    break;
                case "中雨":
                    tv.setImageResource(R.drawable.biz_plugin_weather_zhongyu1);
                    break;
                default:
                    break;
            }
        }





        mProgressBar.clearAnimation();
        mUpdataBtn.setVisibility(View.VISIBLE);
    }
}
