package com.example.ahmed.popularmovies;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class DetailsFragment extends Fragment {

    private String id, title, poster, cover, overview, vote, date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        Bundle bundle = getArguments();
        id = bundle.getString("Id");
        title = bundle.getString("Title");
        cover = bundle.getString("Cover");
        poster = bundle.getString("Poster");
        overview = bundle.getString("Overview");
        vote = bundle.getString("Vote");
        date = bundle.getString("Date");

        setDataToView(view);

        return view;
    }

    public void setDataToView(View view) {

        ImageView movieImageView = view.findViewById(R.id.details_image);

        int orientation = getActivity().getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT)
            Picasso.with(getActivity()).load("https://image.tmdb.org/t/p/w500" + cover).into(movieImageView);

        else
            Picasso.with(getActivity()).load("https://image.tmdb.org/t/p/w320" + poster).into(movieImageView);

        TextView titleText = view.findViewById(R.id.details_title);
        titleText.setText(title);

        TextView dateText = view.findViewById(R.id.details_date);
        dateText.setText(date);

        TextView voteText = view.findViewById(R.id.details_vote);
        if (vote == null || vote.equals("0"))
            voteText.setText(getText(R.string.no_vote_text));
        else
            voteText.setText("Rate : " + vote + "/10");

        TextView overviewText = view.findViewById(R.id.details_overview);
        overviewText.setText(overview);
    }

}
