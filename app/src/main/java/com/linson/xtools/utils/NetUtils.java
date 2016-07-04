package com.linson.xtools.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.apache.http.Header;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by Administrator on 2016/5/3.
 */

public class NetUtils {
    public static void sendJson(String flag, String data, String path, RequestCallBack requestCallBack) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("sendFlag", flag);
        params.addBodyParameter("data", data);
        httpUtils.send(HttpRequest.HttpMethod.POST, path, params
                , requestCallBack);
    }

    public static void sendJson(final String data, final String path) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("data", data);
        //Lu.i(params.getCharset());
        httpUtils.send(HttpRequest.HttpMethod.POST, path, params
                , new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.i("log", "sendJson onSuccess " + responseInfo.result.toString());
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Log.i("log", "sendJson onFailure " + s + " e=" + e.getMessage());
                        Log.i("log", "sendJson onFailure " + s + " data=" + data);
                        Log.i("log", "sendJson onFailure " + s + " path=" + path);
                        e.printStackTrace();
                    }
                });
    }

    public static void sendJson(final String data, final String path, String myParams) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter(myParams, data);
        //Lu.i(params.getCharset());
        httpUtils.send(HttpRequest.HttpMethod.POST, path, params
                , new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {
                        Log.i("log", "sendJson onSuccess " + responseInfo.result.toString());
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {
                        Log.i("log", "sendJson onFailure " + s + " e=" + e.getMessage());
                        Log.i("log", "sendJson onFailure " + s + " data=" + data);
                        Log.i("log", "sendJson onFailure " + s + " path=" + path);
                        e.printStackTrace();
                    }
                });
    }

    public static void sendJson(String data, String path, RequestCallBack requestCallBack) {
        HttpUtils httpUtils = new HttpUtils();
        RequestParams params = new RequestParams();
        params.addBodyParameter("data", data);
        httpUtils.send(HttpRequest.HttpMethod.POST, path, params, requestCallBack);
    }

    /**
     * 检测当的网络（WLAN、3G/2G）状态
     *
     * @param context Context
     * @return true 表示网络可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 当前网络是连接的
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    // 当前所连接的网络可用
                    return true;
                }
            }
        }
        return false;
    }

    public static void uploadByAsyncHttpClient(File file, String url) {
        // 先判断文件是否存在
        if (file.exists() && file.length() > 0) {
            // 1. 创建AsyncHttpClient对象
            AsyncHttpClient client = new AsyncHttpClient();
            // 2.设置参数体
            com.loopj.android.http.RequestParams params = new com.loopj.android.http.RequestParams();
            try {
                // 其实这里的异常不可能出现，因为上边已经做了判断
                params.put("profile_picture", file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 3.上传文件
            client.post(url, params, new AsyncHttpResponseHandler() {
                // 上传成功时回调的方法
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      byte[] responseBody) {
                }
                // 上传失败时回调的方法
                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      byte[] responseBody, Throwable error) {
                }
            });
        } else {
            Lu.i("文件不存在，无法上传");
        }
    }

    public static void uploadByAsyncHttpClient(File file, String url, AsyncHttpResponseHandler handler) {
        // 先判断文件是否存在
        if (file.exists() && file.length() > 0) {
            // 1. 创建AsyncHttpClient对象
            AsyncHttpClient client = new AsyncHttpClient();
            // 2.设置参数体
            com.loopj.android.http.RequestParams params = new com.loopj.android.http.RequestParams();
            try {
                // 其实这里的异常不可能出现，因为上边已经做了判断
                params.put("profile_picture", file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            // 3.上传文件
            client.post(url, params, handler);
        } else {
            Lu.i("文件不存在，无法上传");
        }
    }
}
