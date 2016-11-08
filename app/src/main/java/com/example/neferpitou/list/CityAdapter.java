package com.example.neferpitou.list;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.neferpitou.bean.City;
import com.example.neferpitou.myweather.R;

import java.util.List;

/**
 * Created by Neferpitou on 2016/11/7.
 */

public class CityAdapter extends ArrayAdapter<City> {

    private int resourceId;

    public CityAdapter(Context context, int textViewResourceId, List<City> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        City city = getItem(position);
        View view;
        ViewHolder viewHolder;
        if(convertView==null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.cityName = (TextView)view.findViewById(R.id.item_name);
            viewHolder.cityFirstName = (TextView)view.findViewById(R.id.item_firstname);
            view.setTag(viewHolder);
        }else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }
        viewHolder.cityName.setText(city.getCity());
        viewHolder.cityFirstName.setText(city.getFirstPY());
        return view;
    }

    class ViewHolder{
        TextView cityName;
        TextView cityFirstName;
    }
}
