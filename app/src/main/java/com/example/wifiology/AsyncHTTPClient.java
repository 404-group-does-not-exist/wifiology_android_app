package com.example.wifiology;

import com.loopj.android.http.*;

public class AsyncHTTPClient {
    private static final String BASE_URL = "http://100.115.92.204:5000/api/1.0/";

    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void auth (String userName, String password, AsyncHttpResponseHandler responseHandler){
        client.setBasicAuth(userName,password);
        client.get(getAbsoluteUrl("users/me"),responseHandler);
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
