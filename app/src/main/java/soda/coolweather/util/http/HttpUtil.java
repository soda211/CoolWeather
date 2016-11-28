package soda.coolweather.util.http;

import java.io.BufferedReader;
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
            HttpURLConnection urlConnection;
            @Override
            public void run() {
                try {
                    URL url = new URL(address);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setConnectTimeout(CONNECTTIMEOUT);
                    urlConnection.setReadTimeout(READTIMEOUT);
                    InputStream inputStream = urlConnection.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        response.append(line);
                    }
                    if (httpCallBackListener != null)
                        httpCallBackListener.onSuccess(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    if (httpCallBackListener != null)
                        httpCallBackListener.onError(e);
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect(); // 关闭连接，释放资源
                }
            }
        }).start();

    }

}
