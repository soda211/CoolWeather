package soda.coolweather.util;

import android.text.TextUtils;

import soda.coolweather.db.CoolWeatherDb;
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

}
