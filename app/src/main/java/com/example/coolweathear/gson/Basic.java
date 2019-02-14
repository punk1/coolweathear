package com.example.coolweathear.gson;

import com.google.gson.annotations.SerializedName;

public class Basic {
    @SerializedName("city")
    public  String cityName;
    @SerializedName("id")
    public String weatherid;

    public Updata updata;

    public class Updata {

    @SerializedName("loc")
        public String updateTime;
    }
}
