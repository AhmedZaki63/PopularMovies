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

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {
    private ArrayList<Review> reviewArrayList;
    private Context context;

    public ReviewAdapter(ArrayList<Review> reviewArrayList, Context context) {
        this.reviewArrayList = reviewArrayList;
        this.context = context;
    }

    @Override
    public ReviewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.review_item, parent, false);
        return new ReviewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.MyViewHolder holder, int position) {
        holder.author.setText(reviewArrayList.get(holder.getAdapterPosition()).getAuthor());
        holder.content.setText(reviewArrayList.get(holder.getAdapterPosition()).getContent());
    }

    @Override
    public int getItemCount() {
        return reviewArrayList.size();
    }

    public void setData(ArrayList<Review> videos) {
        reviewArrayList = videos;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.author)
        TextView author;
        @BindView(R.id.review)
        TextView content;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
