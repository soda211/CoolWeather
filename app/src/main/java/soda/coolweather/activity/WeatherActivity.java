package soda.coolweather.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.youmi.android.normal.banner.BannerManager;
import net.youmi.android.normal.banner.BannerViewListener;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import soda.coolweather.R;
import soda.coolweather.util.Constant;
import soda.coolweather.util.Utility;
import soda.coolweather.util.http.HttpCallBackListener;
import soda.coolweather.util.http.HttpUtil;

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
        ShareSDK.initSDK(this,"197bf05032f79");
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
        adLayout = (LinearLayout)findViewById( R.id.ll_banner);
        String county_code = getIntent().getStringExtra("county_code");
        if (!TextUtils.isEmpty(county_code)) {
            //有县级代码 就获取网络数据
            queryWeatherCode(county_code);
        } else {
            //没有县级代码直接显示本地数据
            showWeather();
        }

        switchCity.setOnClickListener(this);
        refreshWeather.setOnClickListener(this);
        Context context = this;
        // 获取广告条
        View bannerView = BannerManager.getInstance(context)
                .getBannerView(new BannerViewListener() {
                    @Override
                    public void onRequestSuccess() {
                        Log.e("ad", "onRequestSuccess");
                    }

                    @Override
                    public void onSwitchBanner() {
                        Log.e("ad", "onSwitchBanner");

                    }

                    @Override
                    public void onRequestFailed() {
                        Log.e("ad", "onRequestFailed");
                    }
                });
        // 将广告条加入到布局中
        adLayout.addView(bannerView);
    }

    /**
     * 查询县的天气代码
     * @param county_code
     */
    private void queryWeatherCode(String county_code) {
        String address = "http://www.weather.com.cn/data/list3/city"+county_code+".xml";
        queryFromServer(address, "code");
    }

    /**
     * 查询服务器获取地区天气代码和天气数据
     * @param address 服务器地址
     * @param type code = 查询地区天气代码 info = 查询天气信息
     */
    private void queryFromServer(String address, final String type) {
        HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
            @Override
            public void onSuccess(String response) {
                if ("code".equals(type)) {
                    if (!TextUtils.isEmpty(response)) {
                        String[] split = response.split("\\|");
                        if (split.length == 2) {
                            String countyCode = split[1];
                            queryWeatherInfo(countyCode);
                        }
                    }
                } else if ("info".equals(type)) {
                    Utility.handleWeatherResponse(WeatherActivity.this, response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showWeather();
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(WeatherActivity.this, "同步失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    /**
     * 查询天气信息
     * @param countyCode
     */
    private void queryWeatherInfo(String countyCode) {
        String address = "http://www.weather.com.cn/data/cityinfo/"+countyCode+".html";
        queryFromServer(address, "info");
    }


    /**
     * 显示存储在本地的天气数据
     */
    private void showWeather() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        cityName.setText(sp.getString(Constant.CITY_NAME, ""));
        currentDate.setText(sp.getString(Constant.CURRENT_TIME, ""));
        publishText.setText("今天"+sp.getString(Constant.PUBLISH_TIME, "")+"发布");
        temp1.setText(sp.getString(Constant.TEMP1, ""));
        temp2.setText(sp.getString(Constant.TEMP2, ""));
        weatherDesp.setText(sp.getString(Constant.WEATHER_DES, ""));
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityName.setVisibility(View.VISIBLE);
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
            Intent intent = new Intent(this, ChooseAreaActivity.class);
            intent.putExtra("from_weather_activity", true);
            startActivity(intent);
            finish();
        } else if ( v == refreshWeather ) {
            publishText.setText("同步中...");
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            String code = preferences.getString(Constant.WEATHER_CODE, "");
            if (!TextUtils.isEmpty(code))
                queryWeatherInfo(code);
        }
    }

    public void share(View v){
        ShareSDK.initSDK(this);
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间等使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，QQ和QQ空间等使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(this);
    }
}
