package soda.coolweather.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import soda.coolweather.model.City;
import soda.coolweather.model.County;
import soda.coolweather.model.Province;

/**
 * Created by soda on 2016/11/28.
 */

public class CoolWeatherDb {
    public static final String DB_NAME = "cool_weather";

    public static final int VERSION = 1;

    private static CoolWeatherDb mInstance;

    private SQLiteDatabase mDatabase;

    /**
     * 私有化构造方法 单例
     * @param context
     */
    private CoolWeatherDb(Context context){
        CoolWeatherOpenHelper coolWeatherOpenHelper = new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
        mDatabase = coolWeatherOpenHelper.getWritableDatabase();
    }

    /**
     * 获取单例对象
     * @param context
     * @return
     */
    public synchronized static CoolWeatherDb getInstance(Context context){
        if (mInstance == null)
            mInstance = new CoolWeatherDb(context);
        return mInstance;
    }
//省份
    /**
     * 将Province实例存入数据库
     * @param province
     */
    public void saveProvince(Province province){
        ContentValues contentValues = new ContentValues();
        contentValues.put("province_name", province.getProvinceName());
        contentValues.put("province_code", province.getProvinceCode());
        mDatabase.insert("Province", null, contentValues);
    }

    /**
     * 从数据库中读取省份列表
     * @return
     */
    public List<Province> loadProvinces(){
        List<Province> provinces = new ArrayList<>();
        Cursor cursor = mDatabase.query("Province", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Province province = new Province();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
                province.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
                provinces.add(province);
            } while (cursor.moveToNext());
        }
        return provinces;
    }
//市
    /**
     * 将City实例存入数据库
     * @param city
     */
    public void saveCity(City city){
        ContentValues contentValues = new ContentValues();
        contentValues.put("city_name", city.getCityName());
        contentValues.put("city_code", city.getCityCode());
        mDatabase.insert("City", null, contentValues);
    }

    /**
     * 从数据库中读取城市列表
     * @return
     */
    public List<City> loadCities(){
        List<City> cities = new ArrayList<>();
        Cursor cursor = mDatabase.query("City", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                City city = new City();
                city.setId(cursor.getInt(cursor.getColumnIndex("id")));
                city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
                city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
                cities.add(city);
            } while (cursor.moveToNext());
        }
        return cities;
    }
//显
    /**
     * 将County实例存入数据库
     * @param county
     */
    public void saveCounty(County county){
        ContentValues contentValues = new ContentValues();
        contentValues.put("county_name", county.getCountyName());
        contentValues.put("county_code", county.getCountyCode());
        mDatabase.insert("County", null, contentValues);
    }

    /**
     * 从数据库中读取省份列表
     * @return
     */
    public List<County> loadCounties(){
        List<County> provinces = new ArrayList<>();
        Cursor cursor = mDatabase.query("County", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                County province = new County();
                province.setId(cursor.getInt(cursor.getColumnIndex("id")));
                province.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
                province.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
                provinces.add(province);
            } while (cursor.moveToNext());
        }
        return provinces;
    }


}
