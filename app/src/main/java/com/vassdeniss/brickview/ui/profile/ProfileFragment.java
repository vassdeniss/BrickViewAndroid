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
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vassdeniss.brickview.BottomNavigationHelper;
import com.vassdeniss.brickview.R;
import com.vassdeniss.brickview.data.UserRepository;
import com.vassdeniss.brickview.data.adapter.ProfileSetAdapter;
import com.vassdeniss.brickview.data.model.Set;
import com.vassdeniss.brickview.data.model.User;
import com.vassdeniss.brickview.databinding.FragmentProfileBinding;

import java.util.List;

public class ProfileFragment extends Fragment {
    private FragmentProfileBinding binding;
    private ProfileViewModel profileViewModel;

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

        profileViewModel = new ViewModelProvider(this, new ProfileViewModelFactory())
                .get(ProfileViewModel.class);

        final UserRepository repo = UserRepository.getInstance();
        final User user = repo.getLoggedInUser();

        final String image = user.getImage();
        if (image != null) {
            final String pureBase64Encoded = image.substring(image.indexOf(",")  + 1);
            final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
            final Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            this.binding.avatarImageView.setImageBitmap(bitmap);
        }

        final String username = repo.getLoggedInUser().getUsername();
        this.binding.usernameTextview.setText(String.format(this.getString(R.string.profile_greeting), username));

        final RecyclerView rView = this.binding.recyclerView;
        rView.setLayoutManager(new LinearLayoutManager(requireContext()));
        List<Set> data = repo.getLoggedInUser().getSets();

        final ProfileSetAdapter adapter = new ProfileSetAdapter(requireContext(), data);
        rView.setAdapter(adapter);

        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(this.createItemTouchCallback(this, data, adapter));
        itemTouchHelper.attachToRecyclerView(rView);

        profileViewModel.getResult().observe(this.getViewLifecycleOwner(), result -> {
            if (result == null) {
                return;
            }

            this.binding.loading.setVisibility(View.GONE);
            this.binding.addSetButton.setEnabled(true);

            if (result.getError() != null) {
                Toast.makeText(getContext(), result.getError(), Toast.LENGTH_LONG).show();
            }

            if (result.getSuccess() != null) {
                data.clear();
                data.addAll(result.getSuccess());
                adapter.notifyDataSetChanged();
            }
        });

        this.binding.logoutButton.setOnClickListener(v -> {
            profileViewModel.logout();
            findNavController(this).navigate(R.id.action_profile_to_home);
            BottomNavigationHelper.updateNav(requireActivity());
        });
        this.binding.addSetButton.setOnClickListener(v -> {
            this.binding.addSetButton.setEnabled(false);
            this.binding.loading.setVisibility(View.VISIBLE);

            final String setId = this.binding.setNumberTextView.getText().toString();
            this.binding.setNumberTextView.setText("");
            profileViewModel.addSet(requireContext(), setId);
        });
    }

    private ItemTouchHelper.SimpleCallback createItemTouchCallback(final ProfileFragment frag, final List<Set> data, final ProfileSetAdapter adapter) {
        return new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                final int pos = viewHolder.getAdapterPosition();
                final Set set = data.get(pos);

                if (set.getReview() == null || set.getReview().isEmpty()) {
                    return ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
                } else {
                    return ItemTouchHelper.LEFT;
                }
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final int pos = viewHolder.getAdapterPosition();
                final Set set = data.get(pos);

                if (direction == ItemTouchHelper.LEFT) {
                    new AlertDialog.Builder(requireContext())
                            .setTitle("Confirm Delete")
                            .setMessage("Are you sure you want to delete this set?")
                            .setPositiveButton("Delete", (dialog, which) -> {
                                binding.addSetButton.setEnabled(false);
                                binding.loading.setVisibility(View.VISIBLE);
                                profileViewModel.deleteSet(requireContext(), set);
                            })
                            .setNegativeButton("Cancel", (dialog, which) -> adapter.notifyItemChanged(pos))
                            .show();
                } else if (direction == ItemTouchHelper.RIGHT) {
                    final Bundle bundle = new Bundle();
                    bundle.putString("_id", set.getSetId());
                    findNavController(frag).navigate(R.id.action_profile_to_add, bundle);
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                final int backgroundCornerOffset = 20;
                final View itemView = viewHolder.itemView;

                Drawable icon;
                ColorDrawable background;
                if (dX > 0) {
                    icon = ContextCompat.getDrawable(getContext().getApplicationContext(), R.drawable.plus);
                    background = new ColorDrawable(Color.rgb(0, 100, 0));
                } else {
                    icon = ContextCompat.getDrawable(getContext().getApplicationContext(), R.drawable.trash_can);
                    background = new ColorDrawable(Color.RED);
                }

                final int iconSize = icon.getIntrinsicHeight() / 2;
                final int iconMargin = (itemView.getHeight() - iconSize) / 2;
                int iconTop = itemView.getTop() + (itemView.getHeight() - iconSize) / 2;
                int iconBottom = iconTop + iconSize;
                icon.setTint(Color.WHITE);

                if (dX > 0) {
                    iconTop = itemView.getTop() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                    iconBottom = iconTop + icon.getIntrinsicHeight();

                    final int iconLeft = itemView.getLeft() + (itemView.getHeight() - icon.getIntrinsicHeight()) / 2;
                    final int iconRight = iconLeft + icon.getIntrinsicHeight();
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);

                    background.setBounds(itemView.getLeft(), itemView.getTop(),
                            itemView.getLeft() + ((int) dX) + backgroundCornerOffset, itemView.getBottom());
                } else if (dX < 0) {
                    final int iconLeft = itemView.getRight() - iconMargin - iconSize;
                    final int iconRight = iconLeft + iconSize;
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
