package com.vassdeniss.brickview.ui.home;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.vassdeniss.brickview.VolleyRequestHelper;
import com.vassdeniss.brickview.data.model.Set;

import org.json.JSONArray;

public class HomeViewModel extends ViewModel {
    private final MutableLiveData<Set[]> data = new MutableLiveData<>();

    public LiveData<Set[]> getData() {
        return this.data;
    }

    public void getAllSets(final Context context) {
        final VolleyRequestHelper.VolleyCallback<JSONArray> callbacks = new VolleyRequestHelper.VolleyCallback<JSONArray>() {
            @Override
            public void onSuccess(final JSONArray result) {
                final Gson gson = new Gson();
                final Set[] setData = gson.fromJson(result.toString(), Set[].class);
                data.setValue(setData);
            }

            @Override
            public void onError(final VolleyError error) {
                VolleyRequestHelper.defaultErrorCallback(error);
            }
        };

        new VolleyRequestHelper.Builder()
                .setContext(context)
                .useMethod(Request.Method.GET)
                .toUrl("/sets/allWithReviews")
                .addArrayCallback(callbacks)
                .execute();
    }
}
