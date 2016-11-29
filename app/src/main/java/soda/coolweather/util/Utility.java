package soda.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import soda.coolweather.db.CoolWeatherDb;
import soda.coolweather.model.City;
import soda.coolweather.model.County;
import soda.coolweather.model.Province;

/**
 * 转换 返回数据字符串为数据实体
 * Created by soda on 2016/11/28.
 */

public class Utility {
    //sharedPreferences用到的一些常量
    interface SpConstant{
        String CITY_SELECTED = "city_selected";
        String CITY_NAME = "city_name";
        String WEATHER_CODE = "weather_code";
        String TEMP1 = "temp1";
        String TEMP2 = "temp2";
        String WEATHER_DES = "weather_des";
        String PUBLISH_TIME = "publish_time";
        String CURRENT_TIME = "current_time";
    }



    /**
     * 解析处理 服务器返回的省级数据
     * @param response
     * @param weatherDb
     */
    public synchronized static boolean handleProvinceResponse(String response, CoolWeatherDb weatherDb){
        if (!TextUtils.isEmpty(response)) {
            String[] allProvinces = response.split(",");
            if (allProvinces != null && allProvinces.length > 0) {
                for (String p: allProvinces) {
                    String[] split = p.split("\\|");
                    Province province = new Province();
                    province.setProvinceCode(split[0]);
                    province.setProvinceName(split[1]);
                    weatherDb.saveProvince(province);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析处理 服务器返回的市级数据
     * @param response
     * @param weatherDb
     * @param  provinceId 省级代码
     */
    public synchronized static boolean handleCityResponse(String response, CoolWeatherDb weatherDb, String provinceId){
        if (!TextUtils.isEmpty(response)) {
            String[] cityList = response.split(",");
            if (cityList != null && cityList.length > 0) {
                for (String c: cityList) {
                    String[] split = c.split("\\|");
                    City city = new City();
                    city.setCityCode(split[0]);
                    city.setCityName(split[1]);
                    city.setProvinceCode(provinceId);
                    weatherDb.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * 解析处理 服务器返回的县级数据
     * @param response
     * @param weatherDb
     * @param cityId 市级代码
     */
    public synchronized static boolean handleCountyResponse(String response, CoolWeatherDb weatherDb, String cityId){
        if (!TextUtils.isEmpty(response)) {
            String[] countyList = response.split(",");
            if (countyList != null && countyList.length > 0) {
                for (String c: countyList) {
                    String[] split = c.split("\\|");
                    County county = new County();
                    county.setCountyCode(split[0]);
                    county.setCountyName(split[1]);
                    county.setCityCode(cityId);
                    weatherDb.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * {"weatherinfo":
     * {"city":"昆山",
     * "cityid":"101190404",
     * "temp1":"4℃",
     * "temp2":"16℃",
     * "weather":"晴转多云",
     * "img1":"n0.gif",
     * "img2":"d1.gif",
     * "ptime":"18:00"}
     * }
     * 处理服务器返回的天气数据并存储到本地
     * @param context
     * @param response
     */
    public void handleWeatherResponse(Context context, String response){
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherinfo = jsonObject.getJSONObject("weatherinfo");
            String city = weatherinfo.getString("city");
            String weatherCode = weatherinfo.getString("cityid");
            String temp1 = weatherinfo.getString("temp1");
            String temp2 = weatherinfo.getString("temp2");
            String weatherDes = weatherinfo.getString("weather");
            String publishTime = weatherinfo.getString("ptime");
            saveWeatherInfo(context, city, weatherCode, temp1, temp2, weatherDes, publishTime);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    /**
     * 将服务器返回的天气数据保存到SharedPreferences
     * @param context
     * @param cityName
     * @param weatherCode
     * @param temp1
     * @param temp2
     * @param weatherDes
     * @param publishTime
     */
    private void saveWeatherInfo(Context context, String cityName, String weatherCode, String temp1, String temp2, String weatherDes, String publishTime) {
        SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy年mm月dd日", Locale.CHINA);
        SharedPreferences.Editor edit = PreferenceManager.getDefaultSharedPreferences(context).edit();
        edit.putBoolean(SpConstant.CITY_SELECTED, true);
        edit.putString(SpConstant.CITY_NAME, cityName);
        edit.putString(SpConstant.WEATHER_CODE, weatherCode);
        edit.putString(SpConstant.TEMP1, temp1);
        edit.putString(SpConstant.TEMP1, temp2);
        edit.putString(SpConstant.WEATHER_DES, weatherDes);
        edit.putString(SpConstant.PUBLISH_TIME, publishTime);
        edit.putString(SpConstant.CURRENT_TIME, simpleFormat.format(new Date()));
        edit.commit();
    }

}
