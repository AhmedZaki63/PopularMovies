package com.example.ahmed.popularmovies.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ahmed.popularmovies.Models.ReviewResponse.Review;
import com.example.ahmed.popularmovies.R;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {
    private ArrayList<Review> reviews;
    private Context context;

    public ReviewAdapter(ArrayList<Review> reviews, Context context) {
        this.reviews = reviews;
        this.context = context;
    }

    @Override
    public ReviewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_item, parent, false);
        return new ReviewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.MyViewHolder holder, int position) {
        holder.author.setText(reviews.get(holder.getAdapterPosition()).getAuthor());
        holder.content.setText(reviews.get(holder.getAdapterPosition()).getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView author, content;

        MyViewHolder(View view) {
            super(view);
            author = view.findViewById(R.id.author);
            content = view.findViewById(R.id.review);
        }
    }
}
