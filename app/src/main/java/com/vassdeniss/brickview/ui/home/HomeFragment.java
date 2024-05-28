package com.vassdeniss.brickview.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.vassdeniss.brickview.VolleyRequestHelper;
import com.vassdeniss.brickview.data.model.SetAdapter;
import com.vassdeniss.brickview.data.model.SetData;
import com.vassdeniss.brickview.databinding.FragmentHomeBinding;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private RecyclerView view;
    private SetAdapter adapter;
    private List<SetData> data;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        this.binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = this.binding.getRoot();

        this.view = this.binding.recyclerView;
        this.view.setLayoutManager(new LinearLayoutManager(getContext()));
        this.data = new ArrayList<>();
        this.adapter = new SetAdapter(getContext(), this.data);
        this.view.setAdapter(this.adapter);

        VolleyRequestHelper.VolleyCallback<JSONArray> callbacks = new VolleyRequestHelper.VolleyCallback<JSONArray>() {
            @Override
            public void onSuccess(JSONArray result) {
                Gson gson = new Gson();
                SetData[] setData = gson.fromJson(result.toString(), SetData[].class);
                Collections.addAll(data, setData);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(VolleyError error) {
                Log.d("a", "A");
            }
        };

        new VolleyRequestHelper.ArrayBuilder()
                .setContext(this.getContext())
                .useMethod(Request.Method.GET)
                .toUrl("/sets/allWithReviews")
                .addCallback(callbacks)
                .execute();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        this.binding = null;
    }
}
