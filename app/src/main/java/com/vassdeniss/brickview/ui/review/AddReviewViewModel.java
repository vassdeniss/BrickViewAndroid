package com.vassdeniss.brickview.ui.review;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.vassdeniss.brickview.VolleyRequestHelper;
import com.vassdeniss.brickview.data.UserRepository;
import com.vassdeniss.brickview.data.model.User;
import com.vassdeniss.brickview.ui.Result;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddReviewViewModel extends ViewModel {
    private final UserRepository repo = UserRepository.getInstance();
    private final MutableLiveData<Result<User>> result = new MutableLiveData<>();

    public LiveData<Result<User>> getResult() {
        return this.result;
    }

    public void postSetReview(Context context, String id, String content) {
        if (isReviewInvalid(content)) {
            this.result.setValue(new Result<>("Error! Content is required!"));
            return;
        }

        if (isReviewTooShort(content)) {
            this.result.setValue(new Result<>("Error! Review too short!"));
            return;
        }

        final VolleyRequestHelper.VolleyCallback<JSONObject> callback = new VolleyRequestHelper.VolleyCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject jsonResult) {
                final Gson gson = new Gson();
                User user = null;
                try {
                    user = gson.fromJson(jsonResult.get("user").toString(), User.class);
                } catch (JSONException ignored) { }

                repo.updateUser(user);
                result.setValue(new Result<>(repo.getLoggedInUser()));
            }

            @Override
            public void onError(VolleyError error) {
                String message = VolleyRequestHelper.defaultErrorCallback(error);
                result.setValue(new Result<>(message));
            }
        };

        new VolleyRequestHelper.Builder()
                .setContext(context)
                .useMethod(Request.Method.POST)
                .toUrl("/reviews/create")
                .withHeaders(VolleyRequestHelper.makeTokenHeaders(this.repo.getLoggedInUser().getTokens()))
                .withBody(VolleyRequestHelper.createBody("_id", id, "content", content, "setVideoIds", "", "setImages", new JSONArray().toString()))
                .addCallback(callback)
                .execute();
    }

    private boolean isReviewTooShort(final String review) {
        return review.length() >= 50 && review.length() <= 5000;
    }

    private boolean isReviewInvalid(String review) {
        return review == null || review.isEmpty();
    }
}
