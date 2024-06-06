package com.vassdeniss.brickview.ui.profile;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.vassdeniss.brickview.VolleyRequestHelper;
import com.vassdeniss.brickview.data.UserRepository;
import com.vassdeniss.brickview.data.model.Set;
import com.vassdeniss.brickview.data.model.User;
import com.vassdeniss.brickview.ui.Result;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ProfileViewModel extends ViewModel {
    private final UserRepository userRepository = UserRepository.getInstance();
    private final VolleyRequestHelper.VolleyCallback<JSONObject> callbacks = new VolleyRequestHelper.VolleyCallback<JSONObject>() {
        @Override
        public void onSuccess(final JSONObject jsonResult) {
            final Gson gson = new Gson();
            User user = null;
            try {
                user = gson.fromJson(jsonResult.get("user").toString(), User.class);
            } catch (JSONException ignored) { }

            userRepository.updateUser(user);
            result.setValue(new Result<>(userRepository.getLoggedInUser().getSets()));
        }

        @Override
        public void onError(final VolleyError error) {
            final String message = VolleyRequestHelper.defaultErrorCallback(error);
            result.setValue(new Result<>(message));
        }
    };
    private final MutableLiveData<Result<List<Set>>> result = new MutableLiveData<>();

    public LiveData<Result<List<Set>>> getResult() {
        return this.result;
    }

    public void logout() {
        this.userRepository.logout();
    }

    public void addSet(final Context context, final String setId) {
        new VolleyRequestHelper.Builder()
                .setContext(context)
                .useMethod(Request.Method.POST)
                .toUrl("/sets/add-set")
                .withHeaders(VolleyRequestHelper.makeTokenHeaders(userRepository.getLoggedInUser().getTokens()))
                .withBody(VolleyRequestHelper.createBody("setId", setId))
                .addCallback(this.callbacks)
                .execute();
    }

    public void deleteSet(final Context context, final Set set) {
        new VolleyRequestHelper.Builder()
                .setContext(context)
                .useMethod(Request.Method.DELETE)
                .toUrl("/sets/delete/" + set.getSetId())
                .withHeaders(VolleyRequestHelper.makeTokenHeaders(userRepository.getLoggedInUser().getTokens()))
                .addCallback(this.callbacks)
                .execute();
    }
}
