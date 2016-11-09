package com.example.neferpitou.list;

import com.example.neferpitou.bean.City;

import java.util.Comparator;

/**
 * Created by Neferpitou on 2016/11/8.
 */

public class FirstPYComparator implements Comparator<City> {

    @Override
    public int compare(City o1, City o2) {
        return o1.getFirstPY().compareTo(o2.getFirstPY());
    }

}
