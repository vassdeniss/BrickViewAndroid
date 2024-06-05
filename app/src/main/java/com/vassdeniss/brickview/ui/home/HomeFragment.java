package com.vassdeniss.brickview.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.FragmentKt;
import androidx.recyclerview.widget.RecyclerView;

import com.vassdeniss.brickview.R;
import com.vassdeniss.brickview.data.model.SetAdapter;
import com.vassdeniss.brickview.data.model.SetData;
import com.vassdeniss.brickview.databinding.FragmentHomeBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment implements SetAdapter.OnItemClickListener {
    private FragmentHomeBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final HomeViewModel homeViewModel = new ViewModelProvider(this, new HomeViewModelFactory())
                .get(HomeViewModel.class);

        this.binding = FragmentHomeBinding.inflate(inflater, container, false);

        final RecyclerView view = this.binding.recyclerView;
        final List<SetData> data = new ArrayList<>();
        final SetAdapter adapter = new SetAdapter(getContext(), data, this);
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

    @Override
    public void onItemClick(final String id) {
        final NavController navController = FragmentKt.findNavController(this);

        final Bundle bundle = new Bundle();
        bundle.putString("_id", id);

        navController.navigate(R.id.action_home_to_review, bundle);
    }
}
