package com.example.fotnewsapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SlideAdapter extends RecyclerView.Adapter<SlideAdapter.SlideViewHolder> {

    private final List<Integer> slideImages;

    public SlideAdapter(List<Integer> slideImages) {
        this.slideImages = slideImages;
    }

    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_slide, parent, false);
        return new SlideViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        holder.bind(slideImages.get(position));
    }

    @Override
    public int getItemCount() {
        return slideImages.size();
    }

    static class SlideViewHolder extends RecyclerView.ViewHolder {
        private final ImageView slideImage;

        public SlideViewHolder(@NonNull View itemView) {
            super(itemView);
            slideImage = itemView.findViewById(R.id.slideImage);
        }

        void bind(int imageRes) {
            slideImage.setImageResource(imageRes);
        }
    }
}