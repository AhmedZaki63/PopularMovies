package com.example.ahmed.popularmovies.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.ahmed.popularmovies.Models.TrailerResponse.Trailer;
import com.example.ahmed.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Trailer> trailerArrayList;

    public TrailerAdapter(Context context, ArrayList<Trailer> trailerArrayList) {
        this.trailerArrayList = trailerArrayList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.video_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Picasso.with(context)
                .load("https://img.youtube.com/vi/" + trailerArrayList.get(position).getKey() + "/mqdefault.jpg")
                .placeholder(R.drawable.video_placeholder)
                .into(holder.imageView);

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String videoLink = "https://www.youtube.com/watch?v=" + trailerArrayList.get(holder.getAdapterPosition()).getKey();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(videoLink));
                intent.putExtra("force_fullscreen", true);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return trailerArrayList.size();
    }

    public void addAll(ArrayList<Trailer> videos) {
        trailerArrayList = videos;
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.list_item_video)
        ImageView imageView;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
