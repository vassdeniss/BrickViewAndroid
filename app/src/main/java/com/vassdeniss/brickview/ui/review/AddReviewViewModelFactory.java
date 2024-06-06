package com.vassdeniss.brickview.ui.review;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class AddReviewViewModelFactory implements ViewModelProvider.Factory {
    @NonNull
    @Override
    @SuppressWarnings("unchecked")
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(AddReviewViewModel.class)) {
            return (T) new AddReviewViewModel();
        } else {
            throw new IllegalArgumentException("Unknown ViewModel class");
        }
    }
}
