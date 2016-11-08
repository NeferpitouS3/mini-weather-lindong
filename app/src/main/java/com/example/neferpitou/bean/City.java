package com.example.neferpitou.bean;

/**
 * Created by Neferpitou on 2016/10/11.
 */
public class City {
    private String province;
    private String city;
    private String number;
    private String allPY;           //所有的拼音             如：BEIJING
    private String allFirstPY;     //所有拼音单词的首字母   如：BJ
    private String firstPY;        //拼音的首字母           如：B

    public City(String province, String city, String number, String firstPY, String allPY, String allFirstPY) {
        this.province = province;
        this.city = city;
        this.number = number;
        this.firstPY = firstPY;
        this.allPY = allPY;
        this.allFirstPY = allFirstPY;
    }

    public String getProvince() {
        return province;
    }

    public String getCity() {
        return city;
    }

    public String getNumber() {
        return number;
    }

    public String getFirstPY() {
        return firstPY;
    }

    public String getAllPY() {
        return allPY;
    }

    public String getAllFirstPY() {
        return allFirstPY;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public void setFirstPY(String firstPY) {
        this.firstPY = firstPY;
    }

    public void setAllPY(String allPY) {
        this.allPY = allPY;
    }

    public void setAllFirstPY(String allFirstPY) {
        this.allFirstPY = allFirstPY;
    }
}
