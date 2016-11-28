package soda.coolweather.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by soda on 2016/11/28.
 */

public class CoolWeatherOpenHelper extends SQLiteOpenHelper {

    /**
     * 省份建表语句
     */
    public static final String CREATE_PROVINCE = "CREATE TABLE Province(" +
                                                                             "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                                                             "province_name TEXT, " +
                                                                             "province_code TEXT)";

    /**
     * 城市建表语句
     */
    public static final String CREATE_CITY = "CREATE TABLE City(" +
                                                                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                                                    "city_name TEXT, " +
                                                                    "city_code TEXT," +
                                                                    "province_code, TEXT)";

    /**
     * 县建表语句
     */
    public static final String CREATE_COUNTY = "CREATE TABLE County(" +
                                                                        "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                                                                        "county_name TEXT, " +
                                                                        "county_code TEXT," +
                                                                        "city_code, TEXT)";

    public CoolWeatherOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PROVINCE);
        db.execSQL(CREATE_CITY);
        db.execSQL(CREATE_COUNTY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
