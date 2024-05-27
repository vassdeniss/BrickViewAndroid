package com.vassdeniss.brickview.ui.profile;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.vassdeniss.brickview.BottomNavigationHelper;
import com.vassdeniss.brickview.R;
import com.vassdeniss.brickview.VolleyRequestHelper;
import com.vassdeniss.brickview.data.UserRepository;
import com.vassdeniss.brickview.data.model.ProfileSetAdapter;
import com.vassdeniss.brickview.data.model.ProfileSetData;
import com.vassdeniss.brickview.data.model.Set;
import com.vassdeniss.brickview.data.model.User;
import com.vassdeniss.brickview.databinding.FragmentProfileBinding;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private final UserRepository userRepository;
    private final VolleyRequestHelper helper;

    public ProfileFragment() {
        this.userRepository = UserRepository.getInstance();
        this.helper = VolleyRequestHelper.getInstance(this.getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        this.binding = FragmentProfileBinding.inflate(inflater, container, false);
        return this.binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final UserRepository repo = UserRepository.getInstance();
        final User user = repo.getLoggedInUser();
        final String image = user.getImage();

        final String pureBase64Encoded = image.substring(image.indexOf(",")  + 1);
        final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
        final Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        this.binding.avatarImageView.setImageBitmap(bitmap);

        final String username = repo.getLoggedInUser().getUsername();
        this.binding.usernameTextview.setText("Hello " + username);

        RecyclerView rView = this.binding.recyclerView;
        rView.setLayoutManager(new LinearLayoutManager(getContext()));
        List<ProfileSetData> data = new ArrayList<>();

        for (Set set : repo.getLoggedInUser().getSets()) {
            data.add(new ProfileSetData(set));
        }

        ProfileSetAdapter adapter = new ProfileSetAdapter(getContext(), data);
        rView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createItemTouchCallback(data, adapter));
        itemTouchHelper.attachToRecyclerView(rView);

        this.binding.logoutButton.setOnClickListener(v -> this.logout());
        this.binding.addSetButton.setOnClickListener(v -> this.addSet(data, adapter));
    }

    private void logout() {
        userRepository.logout();
        findNavController(this).navigate(R.id.action_profile_to_home);
        BottomNavigationHelper.updateNav(getActivity());
    }

    private void addSet(List<ProfileSetData> data, ProfileSetAdapter adapter) {
        this.binding.addSetButton.setEnabled(false);
        this.binding.loading.setVisibility(View.VISIBLE);
        VolleyRequestHelper.VolleyCallback<JSONObject> callbacks = new VolleyRequestHelper.VolleyCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                userRepository.updateUser(result);
                updateUi();
                data.clear();
                for (Set set : userRepository.getLoggedInUser().getSets()) {
                    data.add(new ProfileSetData(set));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(VolleyError error) {
                helper.defaultErrorCallback(error);
                updateUi();
            }

            private void updateUi() {
                binding.loading.setVisibility(View.GONE);
                binding.addSetButton.setEnabled(true);
            }
        };

        new VolleyRequestHelper.Builder()
                .setContext(this.getContext())
                .useMethod(Request.Method.POST)
                .toUrl("/sets/add-set")
                .withHeaders(helper.makeTokenHeaders(userRepository.getLoggedInUser().getTokens()))
                .withBody(helper.createBody("setId", this.binding.setNumberTextView.getText().toString()))
                .addCallback(callbacks)
                .execute();
    }

    private void deleteSet(ProfileSetData set, ProfileSetAdapter adapter, int pos) {
        this.binding.addSetButton.setEnabled(false);
        this.binding.loading.setVisibility(View.VISIBLE);
        VolleyRequestHelper.VolleyCallback<JSONObject> callbacks = new VolleyRequestHelper.VolleyCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                userRepository.updateUser(result);
                adapter.removeItem(pos);
                updateUi();
            }

            @Override
            public void onError(VolleyError error) {
                helper.defaultErrorCallback(error);
                adapter.notifyItemChanged(pos);
                updateUi();
            }

            private void updateUi() {
                binding.loading.setVisibility(View.GONE);
                binding.addSetButton.setEnabled(true);
            }
        };

        new VolleyRequestHelper.Builder()
                .setContext(getContext())
                .useMethod(Request.Method.DELETE)
                .toUrl("/sets/delete/" + set.getSetId())
                .withHeaders(helper.makeTokenHeaders(userRepository.getLoggedInUser().getTokens()))
                .addCallback(callbacks)
                .execute();
    }

    private ItemTouchHelper.SimpleCallback createItemTouchCallback(List<ProfileSetData> data, ProfileSetAdapter adapter) {
        return new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                ProfileSetData set = data.get(pos);
                new AlertDialog.Builder(getContext())
                        .setTitle("Confirm Delete")
                        .setMessage("Are you sure you want to delete this set?")
                        .setPositiveButton("Delete", (dialog, which) -> deleteSet(set, adapter, pos))
                        .setNegativeButton("Cancel", (dialog, which) -> adapter.notifyItemChanged(viewHolder.getAdapterPosition()))
                        .show();
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                int backgroundCornerOffset = 20;
                View itemView = viewHolder.itemView;
                Drawable icon = ContextCompat.getDrawable(getContext().getApplicationContext(), R.drawable.trash_can);
                ColorDrawable background = new ColorDrawable(Color.RED);

                int iconSize = icon.getIntrinsicHeight() / 2;
                int iconMargin = (itemView.getHeight() - iconSize) / 2;
                int iconTop = itemView.getTop() + (itemView.getHeight() - iconSize) / 2;
                int iconBottom = iconTop + iconSize;
                icon.setTint(Color.WHITE);

                if (dX < 0) {
                    int iconLeft = itemView.getRight() - iconMargin - iconSize;
                    int iconRight = iconLeft + iconSize;
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                    background.setBounds(itemView.getRight() + ((int) dX) - backgroundCornerOffset,
                            itemView.getTop(), itemView.getRight(), itemView.getBottom());

                } else {
                    background.setBounds(0, 0, 0, 0);
                }

                background.draw(c);
                icon.draw(c);
            }
        };
    }
}
