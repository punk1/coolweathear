package com.example.coolweathear;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.coolweathear.gson.Forecast;
import com.example.coolweathear.gson.Weather;
import com.example.coolweathear.util.HttpUtil;
import com.example.coolweathear.util.Utility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class WeatherActivity extends AppCompatActivity {
    private ScrollView weatherLayout;
    private TextView titleCity;
    private TextView titleUpadateTime;
    private TextView degreeText;
    private TextView weatherInfoText;
    private LinearLayout forecastLayout;
    private TextView aqiText;
    private TextView pm25Text;
    private TextView comfortText;
    private TextView carWashText;
    private TextView sportText;
    private ImageView bingPicImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21){
            View decorview = getWindow().getDecorView();
            decorview.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            );
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        //初始化各种控件
        bingPicImg = findViewById(R.id.bing_pic_img);
        weatherLayout =(ScrollView) findViewById(R.id.weather_layout);
        titleCity = (TextView) findViewById(R.id.title_city);
        titleUpadateTime = findViewById(R.id.title_updata_time);
        degreeText = findViewById(R.id.degree_text);
        weatherInfoText = findViewById(R.id.weather_info_text);
        forecastLayout = findViewById(R.id.forecast_layout);
        aqiText  =findViewById(R.id.aqi_text);
        pm25Text = findViewById(R.id.pm25_text);
        comfortText =findViewById(R.id.comfort_text);
        carWashText =findViewById(R.id.car_wash_text);
        sportText = findViewById(R.id.sport_text);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String weatherString = prefs.getString("weather",null);
        if (weatherString != null){
            //有缓存时直接解析天气数据
            Weather weather = Utility.handleWeatherResponse(weatherString);
            showWeatherInfo (weather);
        }else {
            String weatherId = getIntent().getStringExtra("weather_id");
            Log.d("天气地址","对么"+weatherId);
            System.out.println(weatherId);
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather (weatherId);
        }
        String bingpic = prefs.getString("bing_pic",null);
        if  (bingpic != null){
            Glide.with(this).load(bingpic).into(bingPicImg);
        }else {
            loadBingPic();
        }
    }

    private void loadBingPic() {
        String requestBingpic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkhttpRequest(requestBingpic, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final  String bingpic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("bing_pic",bingpic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(WeatherActivity.this).load(bingpic).into(bingPicImg);

                    }
                });


            }
        });
    }


    /*
    根据天气id 请求城市信息
     */
    private void requestWeather(final String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid=" + weatherId + "&key=ed37c3ce0eae4f82ab568842d0268105";
        HttpUtil.sendOkhttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this,"失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final  String responseTExt = response.body().string();
                Log.d("服务器地址","有么"+responseTExt);
                final Weather weather = Utility.handleWeatherResponse(responseTExt);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (weather != null && "ok".equals(weather.status)){
                            SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                            editor.putString("weather",responseTExt);
                            editor.apply();
                            showWeatherInfo(weather);
                        }else {
                            Toast.makeText(WeatherActivity.this,"获取天气数据失败",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
        loadBingPic();
    }

    private void showWeatherInfo(Weather weather) {
        String cityNane = weather.basic.cityName;
        String degree = weather.now.temperature + "℃";

        String weatherInfp = weather.now.more.info;
        titleCity.setText(cityNane);
        degreeText.setText(degree);

        weatherInfoText.setText(weatherInfp);
        forecastLayout.removeAllViews();
        for (Forecast forecast :weather.forecastList){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dataText = (TextView)view.findViewById(R.id.date_text);
            TextView infoText = (TextView)view.findViewById(R.id.info_text);
            TextView maxText =(TextView)view.findViewById(R.id.max_text);
            TextView minText = (TextView)view.findViewById(R.id.min_text);
            dataText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        if (weather.aqi != null){
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度:"+weather.suggestion.comfort.info;
        String carWash = "洗车指数:"+weather.suggestion.carWash.info;
        String sport = "运动建议:"+weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);


    }
}
