package com.vassdeniss.brickview.data.adapter;

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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vassdeniss.brickview.R;
import com.vassdeniss.brickview.data.model.Set;

import java.util.List;

public class SetAdapter extends RecyclerView.Adapter<SetAdapter.ViewHolder> {
    private final List<Set> data;
    private final Context context;
    private final OnItemClickListener listener;

    public SetAdapter(final Context context,
                      final List<Set> data,
                      final OnItemClickListener listener) {
        this.context = context;
        this.data = data;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.set, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SetAdapter.ViewHolder holder, int position) {
        final Set set = this.data.get(position);

        Glide.with(this.context)
                .load(set.getImage())
                .into(holder.setImage);

        String text = set.getName();
        if (text.length() > 24) {
            text = text.substring(0, 23) + "...";
        }

        holder.title.setText(text);

        final String info = String.format(this.context.getString(R.string.home_review_info), set.getUsername(), set.getDate());
        holder.info.setText(info);
        holder.grid.setOnClickListener(view -> {
            if (this.listener != null) {
                this.listener.onItemClick(set.getId());
            }
        });

        final String image = set.getUserImage();
        if (image != null) {
            final String pureBase64Encoded = image.substring(image.indexOf(",")  + 1);
            final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
            final Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            holder.authorImage.setImageBitmap(bitmap);
        }
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final ImageView setImage;
        public final TextView title;
        public final TextView info;
        public final GridLayout grid;
        public final ImageView authorImage;

        public ViewHolder(final View itemView) {
            super(itemView);

            this.setImage = itemView.findViewById(R.id.set_image);
            this.title = itemView.findViewById(R.id.set_name);
            this.info = itemView.findViewById(R.id.set_author);
            this.grid = itemView.findViewById(R.id.set_layout);
            this.authorImage = itemView.findViewById(R.id.set_author_image);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(String id);
    }
}
