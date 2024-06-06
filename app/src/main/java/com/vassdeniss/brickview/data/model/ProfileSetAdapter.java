package com.vassdeniss.brickview.data.model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vassdeniss.brickview.R;

import java.util.List;

public class ProfileSetAdapter extends RecyclerView.Adapter<ProfileSetAdapter.ViewHolder> {
    private final List<Set> data;
    private final Context context;

    public ProfileSetAdapter(final Context context, final List<Set> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_set, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileSetAdapter.ViewHolder holder, int position) {
        final Set set = this.data.get(position);

        Glide.with(this.context)
                .load(set.getImage())
                .into(holder.setImage);

        String text = set.getName();
        if (text.length() > 24) {
            text = text.substring(0, 23) + "...";
        }

        holder.title.setText(text);

        final String info = String.format(this.context.getString(R.string.profile_set_info), set.getYear(), set.getParts());
        holder.info.setText(info);
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public void removeItem(final int position) {
        this.data.remove(position);
        this.notifyItemRemoved(position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView setImage;
        public final TextView title;
        public final TextView info;
        public final GridLayout grid;

        public ViewHolder(View itemView) {
            super(itemView);

            this.setImage = itemView.findViewById(R.id.profile_set_image);
            this.title = itemView.findViewById(R.id.profile_set_name);
            this.info = itemView.findViewById(R.id.profile_set_info);
            this.grid = itemView.findViewById(R.id.profile_set_layout);
        }
    }
}
