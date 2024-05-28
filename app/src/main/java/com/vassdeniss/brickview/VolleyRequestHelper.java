package com.vassdeniss.brickview;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.vassdeniss.brickview.data.model.Tokens;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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

    public Map<String, String> makeTokenHeaders(Tokens tokens) {
        Map<String, String> headers = new HashMap<>();
        headers.put("X-Authorization", tokens.getAccessToken());
        headers.put("X-Refresh", tokens.getRefreshToken());
        return headers;
    }

    public String defaultErrorCallback(VolleyError error) {
        if (error.networkResponse != null && error.networkResponse.data != null) {
            try {
                String errorMessage = new String(error.networkResponse.data);
                JSONObject jsonObject = new JSONObject(errorMessage);
                return jsonObject.getString("message");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return "";
    }

    public JSONObject createBody(String... params) {
        if (params.length % 2 != 0) {
            throw new IllegalArgumentException("Invalid number of arguments passed. Must be even to form key-value pairs.");
        }

        JSONObject body = new JSONObject();
        try {
            for (int i = 0; i < params.length; i += 2) {
                body.put(params[i], params[i + 1]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return body;
        }

        return body;
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

    private void makeArrayRequest(
            int method,
            String url,
            @Nullable Map<String, String> headers,
            VolleyCallback<JSONArray> callback) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(method, this.baseUrl + url, null,
                callback::onSuccess,
                callback::onError) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                return headers != null ? headers : super.getHeaders();
            }
        };

        this.getRequestQueue().add(jsonArrayRequest);
    }

    public interface VolleyCallback<T> {
        void onSuccess(T result);
        void onError(VolleyError error);
    }

    public static class Builder {
        private Context ctx;
        private int method;
        private String url;
        private Map<String, String> headers;
        private JSONObject body;
        private VolleyCallback<JSONObject> callback;

        public Builder setContext(Context ctx) {
            this.ctx = ctx;
            return this;
        }

        public Builder useMethod(int method) {
            this.method = method;
            return this;
        }

        public Builder toUrl(String url) {
            this.url = url;
            return this;
        }

        public Builder withHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public Builder withBody(JSONObject body) {
            this.body = body;
            return this;
        }

        public Builder addCallback(VolleyCallback<JSONObject> callback) {
            this.callback = callback;
            return this;
        }

        public void execute() {
            VolleyRequestHelper helper = VolleyRequestHelper.getInstance(this.ctx);
            helper.makeRequest(this.method, this.url, this.headers, this.body, this.callback);
        }
    }

    public static class ArrayBuilder {
        private Context ctx;
        private int method;
        private String url;
        private Map<String, String> headers;
        private VolleyCallback<JSONArray> callback;

        public ArrayBuilder setContext(Context ctx) {
            this.ctx = ctx;
            return this;
        }

        public ArrayBuilder useMethod(int method) {
            this.method = method;
            return this;
        }

        public ArrayBuilder toUrl(String url) {
            this.url = url;
            return this;
        }

        public ArrayBuilder withHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public ArrayBuilder addCallback(VolleyCallback<JSONArray> callback) {
            this.callback = callback;
            return this;
        }

        public void execute() {
            VolleyRequestHelper helper = VolleyRequestHelper.getInstance(this.ctx);
            helper.makeArrayRequest(this.method, this.url, this.headers, this.callback);
        }
    }
}
