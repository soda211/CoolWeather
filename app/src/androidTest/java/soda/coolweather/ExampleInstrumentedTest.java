package soda.coolweather;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import org.junit.Test;
import org.junit.runner.RunWith;

import soda.coolweather.util.http.HttpCallBackListener;
import soda.coolweather.util.http.HttpUtil;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("soda.coolweather", appContext.getPackageName());
    }

    @Test
    public void logHttpResponse() throws Exception {
        HttpUtil.sendHttpRequest("http://www.weather.com.cn/data/list3/city190101.xml", new HttpCallBackListener() {
            @Override
            public void onSuccess(String response) {
                Log.e("fuck", response);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
    @Test
    public void logHttpResponse1() throws Exception {
        HttpUtil.sendHttpRequest("http://www.weather.com.cn/data/cityinfo/101190404.html", new HttpCallBackListener() {
            @Override
            public void onSuccess(String response) {
                Log.e("fuck", response);
            }

            @Override
            public void onError(Exception e) {

            }
        });
    }
}
