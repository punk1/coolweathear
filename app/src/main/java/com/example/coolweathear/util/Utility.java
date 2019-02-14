package com.example.coolweathear.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.coolweathear.db.City;
import com.example.coolweathear.db.Country;
import com.example.coolweathear.db.Province;
import com.example.coolweathear.gson.Weather;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utility {
    public static boolean handleProvince (String response){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allProvimces =  new JSONArray(response);
                for (int i = 0; i < allProvimces.length(); i++) {
                    JSONObject provinceObject = allProvimces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvince(provinceObject.getInt("id"));
                    province.save();

                }
                return  true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public static boolean handleCityResponse(String response,int provinceid){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCities = new JSONArray(response);

                for (int i = 0; i < allCities.length(); i++) {
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityname(cityObject.getString("name"));
                    city.setCitycode(cityObject.getInt("id"));
                    city.setProvinceid(provinceid);
                    city.save();
                }
                return true;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public static boolean handleCountryResponse (String response , int cityid){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++) {
                    JSONObject countObject = allCounties.getJSONObject(i);
                    Country country = new Country();
                    country.setCountryname(countObject.getString("name"));
                    Log.d("数据库","dui budui "+country.getCountryname());
                    country.setWeatherid(countObject.getString("weather_id"));
                    Log.d("数据库","对" +
                            "么"+country.getWeatherid());
                    country.setCityid(cityid);
                    country.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static Weather handleWeatherResponse (String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            Log.d("处理服务器","不对？");
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather");
            String weatherContent = jsonArray.getJSONObject(0).toString();
            return new Gson().fromJson(weatherContent,Weather.class);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}
