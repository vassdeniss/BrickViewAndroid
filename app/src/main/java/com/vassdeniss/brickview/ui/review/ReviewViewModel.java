package com.vassdeniss.brickview.ui.review;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.vassdeniss.brickview.VolleyRequestHelper;
import com.vassdeniss.brickview.data.model.SetDetails;
import com.vassdeniss.brickview.ui.Result;

import org.json.JSONObject;

public class ReviewViewModel extends ViewModel {
    private final MutableLiveData<Result<SetDetails>> result = new MutableLiveData<>();

    public LiveData<Result<SetDetails>> getResult() {
        return this.result;
    }

    public void getReview(Context context, String id) {
        final VolleyRequestHelper.VolleyCallback<JSONObject> callback = new VolleyRequestHelper.VolleyCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonResult) {
                final Gson gson = new Gson();
                final SetDetails details = gson.fromJson(jsonResult.toString(), SetDetails.class);
            }

            @Override
            public void onError(VolleyError error) {
                VolleyRequestHelper.defaultErrorCallback(error);
            }
        };

        new VolleyRequestHelper.Builder()
                .setContext(context)
                .useMethod(Request.Method.GET)
                .toUrl("/reviews/get/" + id)
                .addCallback(callback)
                .execute();
    }
}
