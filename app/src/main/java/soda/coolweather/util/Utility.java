package soda.coolweather.util;

import android.text.TextUtils;

import soda.coolweather.db.CoolWeatherDb;
import soda.coolweather.model.City;
import soda.coolweather.model.County;
import soda.coolweather.model.Province;

/**
 * 转换 返回数据字符串为数据实体
 * Created by soda on 2016/11/28.
 */

public class Utility {


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


}
