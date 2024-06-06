package com.vassdeniss.brickview.ui.review;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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
import com.vassdeniss.brickview.ui.profile.ProfileViewModelFactory;

import org.json.JSONObject;

public class ReviewFragment extends Fragment {
    private FragmentReviewBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentReviewBinding.inflate(inflater, container, false);

        final ReviewViewModel reviewViewModel = new ViewModelProvider(this, new ProfileViewModelFactory())
                .get(ReviewViewModel.class);

        final Bundle args = this.getArguments();
        if (args != null) {
            reviewViewModel.getReview(requireContext(), args.getString("_id"));
        }

        reviewViewModel.getResult().observe(this.getViewLifecycleOwner(), result -> {
            if (result == null) {
                return;
            }

            if (result.getSuccess() != null) {
                SetDetails details = result.getSuccess();

                this.binding.setReview.loadDataWithBaseURL(null, details.getContent(), "text/html", "UTF-8", null);

                Glide.with(requireContext())
                        .load(details.getSetImage())
                        .into(this.binding.reviewSetImage);

                this.binding.reviewSetTitle.setText(details.getSetName());

                final String text = String.format(this.getString(R.string.review_set_details),
                        details.getSetNumber(),
                        details.getSetParts(),
                        details.getSetYear(),
                        details.getSetMinifigCount());
                this.binding.reviewSetDetails.setText(text);
            }
        });

        return this.binding.getRoot();
    }
}
