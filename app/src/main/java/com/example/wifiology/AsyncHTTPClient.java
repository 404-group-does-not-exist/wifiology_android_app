package com.example.wifiology;

import android.content.Context;
import android.provider.SyncStateContract;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AsyncHTTPClient {
    private static final String BASE_URL = "http://100.115.92.204:5000/api/1.0/";

    private static RequestQueue queue;
    private static Context context;
    private static String username;
    private static String password;

    public static void setup(Context con){
        if (queue == null) {
            context = con;
            queue = Volley.newRequestQueue(con);
        }
    }

    public static void auth (String _userName, String _password, Response.Listener<String> responseHandler,Response.ErrorListener errorHandler){
        username = _userName;
        password = _password;
        getString("users/me",null,true,responseHandler,errorHandler);
    }

    public static void getString(String url, final HashMap<String, String> data,final boolean useAuth , Response.Listener<String> responseHandler,Response.ErrorListener errorHandler) {
        StringRequest getRequest = new StringRequest(Request.Method.GET, getAbsoluteUrl(url), responseHandler,errorHandler){
            @Override
            protected Map<String, String> getParams()
            {
                return data;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (useAuth) {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = username+":"+password;
                    String auth = "Basic "
                            + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                    headers.put("Content-Type", "application/json");
                    headers.put("Authorization", auth);
                    return headers;
                }else{
                    return super.getHeaders();
                }
            }
        };
        queue.add(getRequest);
    }

    public static void postString(String url, final HashMap<String, String> data,final boolean useAuth, Response.Listener<String> responseHandler,Response.ErrorListener errorHandler) {
        StringRequest postRequest = new StringRequest(Request.Method.POST, getAbsoluteUrl(url), responseHandler,errorHandler){
            @Override
            protected Map<String, String> getParams()
            {
                return data;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                if (useAuth) {
                    Map<String, String> headers = super.getHeaders();
                    // add headers <key,value>
                    String credentials = username + ":" + password;
                    String auth = "Basic "
                            + Base64.encodeToString(credentials.getBytes(),
                            Base64.NO_WRAP);
                    headers.put("Authorization", auth);
                    return headers;
                }else{
                    return super.getHeaders();
                }
            }
        };
        queue.add(postRequest);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }
}
