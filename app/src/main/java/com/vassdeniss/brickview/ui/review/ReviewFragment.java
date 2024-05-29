package com.vassdeniss.brickview.ui.review;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vassdeniss.brickview.R;
import com.vassdeniss.brickview.databinding.FragmentReviewBinding;

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
            String _id = args.getString("_id");
            this.binding.test.setText(_id);
        }

        return this.binding.getRoot();
    }
}
