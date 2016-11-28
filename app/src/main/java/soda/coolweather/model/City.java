package soda.coolweather.model;

/**
 * Created by soda on 2016/11/28.
 */

public class City {
    private int id;
    private String cityName;
    private String cityCode;
    private String provinceCode;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceId) {
        this.provinceCode = provinceId;
    }
}
