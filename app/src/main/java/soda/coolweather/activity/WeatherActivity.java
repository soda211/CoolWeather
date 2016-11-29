package soda.coolweather.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import soda.coolweather.R;

/**
 * Created by soda on 2016/11/29.
 */

public class WeatherActivity extends Activity implements View.OnClickListener {
    private Button switchCity;
    private TextView cityName;
    private Button refreshWeather;
    private TextView publishText;
    private LinearLayout weatherInfoLayout;
    private TextView currentDate;
    private TextView weatherDesp;
    private TextView temp1;
    private TextView temp2;
    private LinearLayout adLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather_layout);
        findViews();
    }

    /**
     * Find the Views in the layout<br />
     * <br />
     * Auto-created on 2016-11-29 17:40:49 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    private void findViews() {
        switchCity = (Button)findViewById( R.id.switch_city );
        cityName = (TextView)findViewById( R.id.city_name );
        refreshWeather = (Button)findViewById( R.id.refresh_weather );
        publishText = (TextView)findViewById( R.id.publish_text );
        weatherInfoLayout = (LinearLayout)findViewById( R.id.weather_info_layout );
        currentDate = (TextView)findViewById( R.id.current_date );
        weatherDesp = (TextView)findViewById( R.id.weather_desp );
        temp1 = (TextView)findViewById( R.id.temp1 );
        temp2 = (TextView)findViewById( R.id.temp2 );
        adLayout = (LinearLayout)findViewById( R.id.adLayout );
        String county_code = getIntent().getStringExtra("county_code");
        if (!TextUtils.isEmpty(county_code)) {
            //有县级代码 就获取网络数据
        } else {
            //没有县级代码直接显示本地数据
            showWeather();
        }

        switchCity.setOnClickListener(this);
        refreshWeather.setOnClickListener(this);

    }

    /**
     * 显示存储在本地的天气数据
     */
    private void showWeather() {

    }

    /**
     * Handle button click events<br />
     * <br />
     * Auto-created on 2016-11-29 17:40:49 by Android Layout Finder
     * (http://www.buzzingandroid.com/tools/android-layout-finder)
     */
    @Override
    public void onClick(View v) {
        if ( v == switchCity ) {
            // Handle clicks for switchCity
        } else if ( v == refreshWeather ) {
            // Handle clicks for refreshWeather
        }
    }
}
