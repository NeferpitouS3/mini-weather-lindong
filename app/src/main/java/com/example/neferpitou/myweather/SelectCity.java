package com.example.neferpitou.myweather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.neferpitou.app.MyApplication;
import com.example.neferpitou.bean.City;
import com.example.neferpitou.db.CityDB;
import com.example.neferpitou.list.CityAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neferpitou on 2016/10/11.
 */
public class SelectCity extends Activity implements View.OnClickListener {
    private ImageView mBackBtn;
    private List<City> cityList;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);

        initCity();
        CityAdapter adapter = new CityAdapter(SelectCity.this,R.layout.city_item,cityList);
        listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(adapter);

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

        mBackBtn = (ImageView)findViewById(R.id.title_back);
        mBackBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.title_back:
                Intent intent = new Intent();
                intent.putExtra("cityCode","101010100");
                setResult(RESULT_OK,intent);  //(int resultCode,Intent data)
                finish();
                break;

            default:
                break;
        }
    }

    public void initCity(){
        MyApplication myApplication = MyApplication.getInstance();
        cityList = myApplication.getCityList();
    }
}
