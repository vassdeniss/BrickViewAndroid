package com.vassdeniss.brickview.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.vassdeniss.brickview.data.model.SetAdapter;
import com.vassdeniss.brickview.data.model.SetData;
import com.vassdeniss.brickview.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this, new HomeViewModelFactory())
                .get(HomeViewModel.class);

        this.binding = FragmentHomeBinding.inflate(inflater, container, false);

        RecyclerView view = this.binding.recyclerView;
        List<SetData> data = new ArrayList<>();
        SetAdapter adapter = new SetAdapter(getContext(), data);
        view.setAdapter(adapter);

        homeViewModel.getAllSets(this.getContext());

        homeViewModel.getData().observe(this.getViewLifecycleOwner(), setData -> {
            if (setData == null) {
                return;
            }

            data.clear();
            Collections.addAll(data, setData);
            adapter.notifyDataSetChanged();
        });

        return this.binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.binding = null;
    }
}
