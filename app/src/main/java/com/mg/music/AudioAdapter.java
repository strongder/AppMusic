package com.mg.music;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.shape.ShapeAppearanceModel;

import java.util.List;

public class AudioAdapter extends RecyclerView.Adapter<AudioAdapter.ViewHolder> {
    public List<AudioFile> audioFiles;
    private OnItemClickListener onItemClickListener;
    private OnDeleteClickListener onDeleteClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(AudioFile audioFile, int position);
    }

    public AudioAdapter(List<AudioFile> audioFiles) {
        this.audioFiles = audioFiles;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView artistTextView;
        TextView duration;
        ShapeableImageView artImg;
        ImageView btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            duration = itemView.findViewById(R.id.duration);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            artistTextView = itemView.findViewById(R.id.artistTextView);
            artImg = itemView.findViewById(R.id.artImgView);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            ShapeAppearanceModel shapeAppearanceModel = new
                    ShapeAppearanceModel()
                    .toBuilder()
                    .setAllCorners(CornerFamily.ROUNDED, 25) // Set radius for rounded corners
                    .build();
            artImg.setShapeAppearanceModel(shapeAppearanceModel);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_audio, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        AudioFile audioFile = audioFiles.get(position);
        String timerString = "";
        long milliseconds = audioFile.getDuration();
        int hours = (int) (milliseconds / (1000 * 3600));
        int minutes = (int) (milliseconds % (1000 * 3600)) / (1000 * 60);
        int seconds = (int) (milliseconds % (1000 * 60)) / 1000;
        if (hours > 0) {
            timerString = String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            timerString = String.format("%02d:%02d", minutes, seconds);
        }

        holder.titleTextView.setText(audioFile.getTitle());
        holder.artistTextView.setText(audioFile.getArtist());
        holder.duration.setText(timerString);
        loadAlbumArt(holder.artImg, audioFile.getAlbumArtUri());

        holder.itemView.setOnClickListener(view -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, position);
            }
        });

        holder.btnDelete.setOnClickListener(view -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(audioFile, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return audioFiles.size();
    }

    public void loadAlbumArt(ImageView imageView, Uri filePath) {
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.musicbutton) // Placeholder image while loading
                .error(R.drawable.musicbutton); // Error image if loading fails
        Glide.with(imageView.getContext())
                .load(filePath)
                .apply(requestOptions)
                .thumbnail(Glide.with(imageView.getContext()).load(filePath))
                .into(imageView);
    }
}
