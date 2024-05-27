package com.vassdeniss.brickview.data.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vassdeniss.brickview.R;

import java.util.List;

public class ProfileSetAdapter extends RecyclerView.Adapter<ProfileSetAdapter.ViewHolder> {
    private List<ProfileSetData> data;
    private Context context;

    public ProfileSetAdapter(Context context, List<ProfileSetData> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_set, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileSetAdapter.ViewHolder holder, int position) {
        ProfileSetData data = this.data.get(position);

        Glide.with(this.context)
                .load(data.getImageUrl())
                .into(holder.setImage);

        String text = data.getName();
        if (text.length() > 24) {
            text = text.substring(0, 23) + "...";
        }

        holder.title.setText(text);
        holder.info.setText("Year: " + data.getYear() + "\n" + "Parts: " + data.getParts());
        holder.grid.setOnClickListener(view -> Toast.makeText(view.getContext(), "click on item: " + data.getName(),Toast.LENGTH_LONG).show());
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public void removeItem(int position) {
        this.data.remove(position);
        this.notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView setImage;
        public TextView title;
        public TextView info;
        public GridLayout grid;

        public ViewHolder(View itemView) {
            super(itemView);

            this.setImage = itemView.findViewById(R.id.profile_set_image);
            this.title = itemView.findViewById(R.id.profile_set_name);
            this.info = itemView.findViewById(R.id.profile_set_info);
            this.grid = itemView.findViewById(R.id.profile_set_layout);
        }
    }
}
