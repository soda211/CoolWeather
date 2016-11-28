package soda.coolweather.activity;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import soda.coolweather.R;
import soda.coolweather.db.CoolWeatherDb;
import soda.coolweather.model.City;
import soda.coolweather.model.County;
import soda.coolweather.model.Province;
import soda.coolweather.util.Utility;
import soda.coolweather.util.http.HttpCallBackListener;
import soda.coolweather.util.http.HttpUtil;

public class ChooseAreaActivity extends AppCompatActivity {

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;

    private ProgressDialog mProgressDialog;
    private TextView mTvTitle;
    private ListView mLvArea;
    private ArrayAdapter<String> mAdapter;
    private List<String> mDataList = new ArrayList<>();
    private CoolWeatherDb  mCoolWeatherDb;
    //省级列表
    private List<Province> mProvinceList;
    //市级列表
    private List<City> mCityList;
    //县级列表
    private List<County> mCountyList;

    //当前选择层级
    private int currentLevel;

    private Province mSelectedProvince;

    private City mSelectedCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.choose_area);
        //初始化控件
        mTvTitle = (TextView) findViewById(R.id.tv_title);
        mLvArea = (ListView) findViewById(R.id.lv_area);
        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mDataList);
        mLvArea.setAdapter(mAdapter);
        mCoolWeatherDb = CoolWeatherDb.getInstance(this);
        mLvArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel == LEVEL_PROVINCE) {
                    //查询市级数据
                    mSelectedProvince = mProvinceList.get(position);
                    queryCities();
                } else if (currentLevel == LEVEL_CITY) {
                    //查询县级数据
                    mSelectedCity = mCityList.get(position);
                    queryCounties();
                }
            }
        });
        queryProvinces();
    }

    /**
     * 查询所有省
     */
    private void queryProvinces() {
        mProvinceList = mCoolWeatherDb.loadProvinces();
        if (mProvinceList.size() > 0) {
            mDataList.clear();
            for (Province p : mProvinceList) {
                mDataList.add(p.getProvinceName());
            }
            mAdapter.notifyDataSetChanged();
            mLvArea.setSelection(0);
            mTvTitle.setText("中国");
            currentLevel = LEVEL_PROVINCE;
        } else {
            //从网络获取
            queryFromServer(null, "province");
        }
    }


    /**
     * 从网络获取数据
     * @param code 地区代码
     * @param type 省 市 县
     */
    private void queryFromServer(String code, final String type) {
        String address = "http://www.weather.com.cn/data/list3/city.xml";
        if (!TextUtils.isEmpty(code))
            address = String.format("http://www.weather.com.cn/data/list3/city%s.xml", code);
        showProgress();
        HttpUtil.sendHttpRequest(address, new HttpCallBackListener() {
            @Override
            public void onSuccess(String response) {
                boolean result = false;
                if ("province".equals(type)) {
                    result = Utility.handleProvinceResponse(response, mCoolWeatherDb);
                } else if ("city".equals(type)){
                    result = Utility.handleCityResponse(response, mCoolWeatherDb, mSelectedProvince.getProvinceCode());
                } else if ("county".equals(type)){
                    result = Utility.handleCountyResponse(response, mCoolWeatherDb, mSelectedCity.getCityCode());
                }
                if (result) {
                    //数据库存储服务器返回数据操作成功
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgress();
                            if ("province".equals(type)) {
                                queryProvinces();
                            } else if ("city".equals(type)){
                                queryCities();
                            } else if ("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgress();
                        Toast.makeText(ChooseAreaActivity.this, "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void showProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("加载中...");
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    private void closeProgress(){
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    /**
     * 查询市下所有县
     */
    private void queryCounties() {
        mCountyList = mCoolWeatherDb.loadCounties(mSelectedCity.getCityCode());
        if (mCountyList.size() > 0) {
            mDataList.clear();
            for (County c : mCountyList) {
                mDataList.add(c.getCountyName());
            }
            mAdapter.notifyDataSetChanged();
            mLvArea.setSelection(0);
            mTvTitle.setText(mSelectedCity.getCityName());
            currentLevel = LEVEL_COUNTY;
        } else {
            //从网络获取
            queryFromServer(mSelectedCity.getCityCode(), "county");
        }
    }

    /**
     * 查询省下所有市
     */
    private void queryCities() {
        mCityList = mCoolWeatherDb.loadCities(mSelectedProvince.getProvinceCode());
        if (mCityList.size() > 0) {
            mDataList.clear();
            for (City c : mCityList) {
                mDataList.add(c.getCityName());
            }
            mAdapter.notifyDataSetChanged();
            mLvArea.setSelection(0);
            mTvTitle.setText(mSelectedProvince.getProvinceName());
            currentLevel = LEVEL_CITY;
        } else {
            //从网络获取
            queryFromServer(mSelectedProvince.getProvinceCode(), "city");
        }
    }

    @Override
    public void onBackPressed() {
        switch (currentLevel) {
            case LEVEL_COUNTY:
                queryCities();
                break;
            case LEVEL_CITY:
                queryProvinces();
                break;
            default:
               super.onBackPressed();
        }
    }
}
