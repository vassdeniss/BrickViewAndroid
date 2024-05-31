package com.vassdeniss.brickview.ui.review;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.vassdeniss.brickview.R;
import com.vassdeniss.brickview.VolleyRequestHelper;
import com.vassdeniss.brickview.data.model.SetDetails;
import com.vassdeniss.brickview.databinding.FragmentReviewBinding;

import org.json.JSONObject;

public class ReviewFragment extends Fragment {
    private FragmentReviewBinding binding;

    public ReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentReviewBinding.inflate(inflater, container, false);

        Bundle args = this.getArguments();
        if (args != null) {
            VolleyRequestHelper helper = VolleyRequestHelper.getInstance(this.getContext());

            VolleyRequestHelper.VolleyCallback<JSONObject> callback = new VolleyRequestHelper.VolleyCallback<JSONObject>() {
                @Override
                public void onSuccess(JSONObject result) {
                    Gson gson = new Gson();
                    SetDetails details = gson.fromJson(result.toString(), SetDetails.class);
                    binding.setReview.loadDataWithBaseURL(null, details.getContent(), "text/html", "UTF-8", null);

                    Glide.with(getContext())
                            .load(details.getSetImage())
                            .into(binding.reviewSetImage);

                    binding.reviewSetTitle.setText(details.getSetName());
                    binding.reviewSetDetails.setText("Set Id: " + details.getSetNumber() + "\n" + "Parts: " + details.getSetParts()  + "\n" + "Year: " + details.getSetYear()  + "\n" + "Minifigs: " + details.getSetMinifigCount());
                }

                @Override
                public void onError(VolleyError error) {
                    helper.defaultErrorCallback(error);
                }
            };

            new VolleyRequestHelper.Builder()
                    .setContext(this.getContext())
                    .useMethod(Request.Method.GET)
                    .toUrl("/reviews/get/" + args.getString("_id"))
                    .addCallback(callback)
                    .execute();
        }

        return this.binding.getRoot();
    }
}
