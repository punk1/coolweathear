package com.example.coolweathear.db;


import android.util.Log;

import org.litepal.crud.DataSupport;

public class Country extends DataSupport {
    private int id;
    private String countryname;
    private String weatherid;
    private int cityid;

    public int getId() {
        return id;
    }

    public String getCountryname() {

        return countryname;
    }

    public String getWeatherid() {
        return weatherid;
    }

    public int getCityid() {
        return cityid;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCountryname(String countryname) {
        this.countryname = countryname;
    }

    public void setWeatherid(String weatherid) {
        this.weatherid = weatherid;
    }

    public void setCityid(int cityid) {
        this.cityid = cityid;
    }
}
