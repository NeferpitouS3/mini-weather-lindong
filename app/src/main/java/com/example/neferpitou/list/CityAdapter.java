package com.example.neferpitou.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.example.neferpitou.bean.City;
import com.example.neferpitou.myweather.R;

import java.util.List;

/**
 * Created by Neferpitou on 2016/11/8.
 */

public class CityAdapter extends BaseAdapter implements SectionIndexer {

    private List<City> cityList;
    private Context mContext;

    public CityAdapter(Context mContext , List<City> cityList) {
        this.cityList = cityList;
        this.mContext = mContext;
    }

    public void updateListView(List<City> cityList){
        this.cityList = cityList;
        notifyDataSetChanged();
    }

    public int getCount(){
        return cityList.size();
    }

    public Object getItem(int position){
        return cityList.get(position);
    }

    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        final City mCity = cityList.get(position);
        if(convertView==null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.city_item,null);
            viewHolder.cityFirstPY = (TextView)convertView.findViewById(R.id.item_firstname);
            viewHolder.cityName    = (TextView)convertView.findViewById(R.id.item_name);
            viewHolder.cityProvince= (TextView)convertView.findViewById(R.id.item_province) ;
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }
        //section即首字母
        int section = getSectionForPosition(position);

        //如果当前位置 等于 cityList里第一个拥有该首字母的city的位置 ，则认为是该首字母第一次出现
        if(position==getPositionForSection(section)){
            viewHolder.cityFirstPY.setVisibility(View.VISIBLE);
            viewHolder.cityFirstPY.setText(mCity.getFirstPY());
        }else {
            viewHolder.cityFirstPY.setVisibility(View.GONE);
        }
        viewHolder.cityName.setText(cityList.get(position).getCity());
        viewHolder.cityProvince.setText(cityList.get(position).getProvince());
        return convertView;
    }

    final static class ViewHolder{
        TextView cityFirstPY;
        TextView cityName;
        TextView cityProvince;
    }

    //返回当前位置所属的首字母
    public int getSectionForPosition(int position){
        return cityList.get(position).getFirstPY().charAt(0);
    }

    //从0开始找到第一个首字母属于section的city返回它的位置
    public int getPositionForSection(int section){
        for(int i=0;i<getCount();i++){
            char firstChar = cityList.get(i).getFirstPY().charAt(0);
            if(firstChar == section){
                return i;
            }
        }
        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}
