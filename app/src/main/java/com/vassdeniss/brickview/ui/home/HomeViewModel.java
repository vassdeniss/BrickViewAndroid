package com.vassdeniss.brickview.ui.home;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.vassdeniss.brickview.VolleyRequestHelper;
import com.vassdeniss.brickview.data.model.SetData;

import org.json.JSONArray;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<SetData[]> data = new MutableLiveData<>();

    public LiveData<SetData[]> getData() {
        return this.data;
    }

    public void getAllSets(Context context) {
        VolleyRequestHelper.VolleyCallback<JSONArray> callbacks = new VolleyRequestHelper.VolleyCallback<JSONArray>() {
            @Override
            public void onSuccess(JSONArray result) {
                Gson gson = new Gson();
                SetData[] setData = gson.fromJson(result.toString(), SetData[].class);
                data.setValue(setData);
            }

            @Override
            public void onError(VolleyError error) { }
        };

        new VolleyRequestHelper.Builder()
                .setContext(context)
                .useMethod(Request.Method.GET)
                .toUrl("/sets/allWithReviews")
                .addArrayCallback(callbacks)
                .execute();
    }
}
