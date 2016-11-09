package com.example.neferpitou.myweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.neferpitou.app.MyApplication;
import com.example.neferpitou.bean.City;
import com.example.neferpitou.list.FirstPYComparator;
import com.example.neferpitou.list.CityAdapter;

import java.util.Collections;
import java.util.List;

/**
 * Created by Neferpitou on 2016/10/11.
 */
public class SelectCity extends Activity implements View.OnClickListener{
    private ImageView mBackBtn;

    private List<City> cityList;
    private ListView listView;
    private FirstPYComparator pinyinComparator;
    private CityAdapter adapter;
//    String cityCodeFromMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);

        initViews();

        Bundle bundle = getIntent().getExtras();
        TextView mTitle = (TextView)findViewById(R.id.title_name);
        mTitle.setText("当前城市为："+bundle.get("city"));
//        cityCodeFromMain = (String)bundle.get("CityCode");

        mBackBtn = (ImageView)findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_back:
//                Intent intent = new Intent();
//                intent.putExtra("cityCode",cityCodeFromMain);
//                setResult(RESULT_OK,intent);  //(int resultCode,Intent data)
                finish();
                break;

            default:
                break;
        }
    }

    private void initViews() {

        pinyinComparator = new FirstPYComparator();

        initCity();

        listView = (ListView)findViewById(R.id.list_view);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City city = cityList.get(position);
                Intent intent = new Intent();
                intent.putExtra("cityCode",city.getNumber());
                setResult(RESULT_OK,intent);  //(int resultCode,Intent data)
                finish();
            }
        });

        // 根据A-Z进行排序
        Collections.sort(cityList, pinyinComparator);
        adapter = new CityAdapter(SelectCity.this, cityList);
        listView.setAdapter(adapter);
    }

    public void initCity(){
        MyApplication myApplication = MyApplication.getInstance();
        cityList = myApplication.getCityList();
    }
}
