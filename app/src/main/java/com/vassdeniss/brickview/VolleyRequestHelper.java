package com.vassdeniss.brickview;

import android.content.Context;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolleyRequestHelper {
    private static VolleyRequestHelper instance;
    private RequestQueue requestQueue;
    private static Context ctx;
    private String baseUrl;

    private VolleyRequestHelper(Context context) {
        ctx = context;
        this.baseUrl = "https://brickview.api.vasspass.net";
        this.requestQueue = this.getRequestQueue();
    }

    public static synchronized VolleyRequestHelper getInstance(Context context) {
        if (instance == null) {
            instance = new VolleyRequestHelper(context);
        }

        return instance;
    }

    public RequestQueue getRequestQueue() {
        if (this.requestQueue == null) {
            this.requestQueue = Volley.newRequestQueue(ctx.getApplicationContext());
        }

        return this.requestQueue;
    }

    public void makeGetRequest(String url,
                               @Nullable Map<String, String> headers,
                               VolleyCallback<JSONObject> callback) {
        this.makeRequest(Request.Method.GET, url, headers, null, callback);
    }

    public void makePostRequest(String url,
                               @Nullable Map<String, String> headers,
                               JSONObject body,
                               VolleyCallback<JSONObject> callback) {
        this.makeRequest(Request.Method.POST, url, headers, body, callback);
    }

    public void makeDeleteRequest(String url,
                                Map<String, String> headers,
                                @Nullable JSONObject body,
                                VolleyCallback<JSONObject> callback) {
        this.makeRequest(Request.Method.DELETE, url, headers, body, callback);
    }

    public Map<String, String> makeTokenHeaders(String access, String refresh) {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Authorization", access);
        headers.put("X-Refresh", refresh);
        return headers;
    }

    private void makeRequest(
            int method,
            String url,
            @Nullable Map<String, String> headers,
            @Nullable JSONObject body,
            VolleyCallback<JSONObject> callback) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(method, this.baseUrl + url, body,
                callback::onSuccess,
                callback::onError) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers != null ? headers : super.getHeaders();
            }
        };

        this.getRequestQueue().add(jsonObjectRequest);
    }

    public interface VolleyCallback<T> {
        void onSuccess(T result);
        void onError(VolleyError error);
    }
}
