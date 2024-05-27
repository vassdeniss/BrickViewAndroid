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

public class SetAdapter extends RecyclerView.Adapter<SetAdapter.ViewHolder> {
    private List<SetData> data;
    private Context context;

    public SetAdapter(Context context, List<SetData> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.set, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SetAdapter.ViewHolder holder, int position) {
        SetData data = this.data.get(position);

        Glide.with(this.context)
                .load(data.getImageUrl())
                .into(holder.setImage);

        String text = data.getName();
        if (text.length() > 24) {
            text = text.substring(0, 23) + "...";
        }

        holder.title.setText(text);
        holder.info.setText("Review by " + data.getUsername() + "\n" + data.getDate());
        holder.grid.setOnClickListener(view -> Toast.makeText(view.getContext(), "click on item: " + data.getName(),Toast.LENGTH_LONG).show());

        final String image = data.getUserImage();
        final String pureBase64Encoded = image.substring(image.indexOf(",")  + 1);
        final byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
        final Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
        holder.authorImage.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return this.data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView setImage;
        public TextView title;
        public TextView info;
        public GridLayout grid;
        public ImageView authorImage;

        public ViewHolder(View itemView) {
            super(itemView);

            this.setImage = itemView.findViewById(R.id.set_image);
            this.title = itemView.findViewById(R.id.set_name);
            this.info = itemView.findViewById(R.id.set_author);
            this.grid = itemView.findViewById(R.id.set_layout);
            this.authorImage = itemView.findViewById(R.id.set_author_image);
        }
    }
}