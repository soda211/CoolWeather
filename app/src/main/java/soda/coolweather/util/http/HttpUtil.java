package soda.coolweather.util.http;

import android.os.Build;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 网络请求工具类
 * Created by soda on 2016/11/28.
 */

public class HttpUtil {

    public static final int CONNECTTIMEOUT = 5000;
    public static final int READTIMEOUT = 5000;

    public static void sendHttpRequest(final String address, final HttpCallBackListener httpCallBackListener){

        new Thread(new Runnable() {
            BufferedReader bufferedReader;
            HttpURLConnection urlConnection;
            @Override
            public void run() {
                try {
                    URL url = new URL(address);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestProperty("Accept-Encoding", "");
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setConnectTimeout(CONNECTTIMEOUT);
                    urlConnection.setReadTimeout(READTIMEOUT);
                    InputStream inputStream = urlConnection.getInputStream();
                    bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }
                    Log.e("server", response.toString());
                    if (httpCallBackListener != null)
                        httpCallBackListener.onSuccess(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    if (httpCallBackListener != null)
                        httpCallBackListener.onError(e);
                } finally {
                    if (bufferedReader != null)
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                            Log.e("cool", "读取流关闭异常"+e.toString());
                        }
                    if (urlConnection != null)
                        urlConnection.disconnect(); // 关闭连接，释放资源
                }
            }
        }).start();

    }

}
