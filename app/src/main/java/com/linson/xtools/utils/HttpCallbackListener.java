package com.linson.xtools.utils;

/**
 * Created by Administrator on 2016/3/24.
 */
public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
