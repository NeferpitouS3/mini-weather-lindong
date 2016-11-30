package com.example.neferpitou.myweather;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.neferpitou.viewpager.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neferpitou on 2016/11/30.
 */

public class GuideActivity extends Activity implements ViewPager.OnPageChangeListener{

    ViewPagerAdapter viewPagerAdapter;
    ViewPager guideViewpager;
    List<View> viewList;
    Button into;

    private ImageView[] dots;
    private int[] ids={R.id.i1,R.id.i2,R.id.i3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guide_index);
        initGuideViews();
        initDots();
        into = (Button)viewList.get(2).findViewById(R.id.into);
        into.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences.Editor editor = getSharedPreferences("first", MODE_PRIVATE).edit();
                editor.putInt("Count",1);
                editor.commit();
                Intent intent = new Intent(GuideActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    void initDots(){
        dots = new ImageView[viewList.size()];
        for(int i=0;i<viewList.size();i++){
            dots[i] = (ImageView)findViewById(ids[i]);
        }
    }

    void initGuideViews(){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        viewList = new ArrayList<View>();
        viewList.add(layoutInflater.inflate(R.layout.guide_page1,null));
        viewList.add(layoutInflater.inflate(R.layout.guide_page2,null));
        viewList.add(layoutInflater.inflate(R.layout.guide_page3,null));

        viewPagerAdapter = new ViewPagerAdapter(viewList,this);
        guideViewpager = (ViewPager)findViewById(R.id.guide_viewpager);
        guideViewpager.setAdapter(viewPagerAdapter);
        guideViewpager.addOnPageChangeListener(this);
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
}
