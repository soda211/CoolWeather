package soda.coolweather.util.http;

/**
 * Created by soda on 2016/11/28.
 */

public interface HttpCallBackListener {

    void onSuccess(String response);

    void onError(Exception e);

}
