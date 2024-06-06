package com.vassdeniss.brickview.ui.review;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.FragmentKt;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vassdeniss.brickview.R;
import com.vassdeniss.brickview.databinding.FragmentAddReviewBinding;

public class AddReviewFragment extends Fragment {
    private FragmentAddReviewBinding binding;
    private AddReviewViewModel addReviewViewModel;
    private String id;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentAddReviewBinding.inflate(inflater, container, false);

        final Bundle args = this.getArguments();
        if (args != null) {
            this.id = args.getString("_id");
        }

        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.addReviewViewModel = new ViewModelProvider(this, new AddReviewViewModelFactory())
                .get(AddReviewViewModel.class);

        this.binding.submitButton.setOnClickListener(v -> {
            this.binding.submitButton.setEnabled(false);
            this.addReviewViewModel.postSetReview(this.getContext(), this.id, this.binding.editTextView.getText().toString());
        });

        this.addReviewViewModel.getResult().observe(this.getViewLifecycleOwner(), result -> {
            if (result == null || result.isEmpty()) {
                return;
            }

            this.binding.submitButton.setEnabled(true);
            
            if (result.getError() != null) {
                Toast.makeText(getContext().getApplicationContext(), result.getError(), Toast.LENGTH_SHORT).show();
            }

            if (result.getSuccess() != null) {
                FragmentKt.findNavController(this).navigate(R.id.action_add_to_home);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.binding = null;
    }
}
