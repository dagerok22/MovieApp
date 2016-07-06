package com.example.generalkenobi.movieapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    public DetailActivityFragment() {
    }

    TextView descriptionTextView;
    TextView rate;
    TextView title;
    TextView releaseDate;
    ImageView backgroundPosterView;
    LinearLayout linearLayout;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        linearLayout = (LinearLayout) rootView.findViewById(R.id.linLayout);
        descriptionTextView = (TextView) rootView.findViewById(R.id.descriptionTextView);
        backgroundPosterView = (ImageView) rootView.findViewById(R.id.background_poster);
        rate = (TextView) rootView.findViewById(R.id.vote_average);
        title = (TextView) rootView.findViewById(R.id.movieTitle);
        releaseDate = (TextView) rootView.findViewById(R.id.release_date);

        descriptionTextView.setText(intent.getStringExtra("descr"));
        releaseDate.setText(intent.getStringExtra("releaseDate"));
        title.setText(intent.getStringExtra("title"));
        rate.setText(intent.getDoubleExtra("av_rate", 0) + "");
        //new ImageDownloader(backgroundPosterView).execute("https://image.tmdb.org/t/p/w600" + intent.getStringExtra("bgPosterPath"));
        Picasso.with(getContext()).load("https://image.tmdb.org/t/p/w600" + intent.getStringExtra("bgPosterPath")).into(backgroundPosterView);
        Log.e("bgPath", "https://image.tmdb.org/t/p/w600" + intent.getStringExtra("bgPosterPath"));

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float scWidth = outMetrics.widthPixels;
        backgroundPosterView.getLayoutParams().width = (int) scWidth;
        backgroundPosterView.getLayoutParams().height = (int) (scWidth * 0.6f);
        //linearLayout.setMovementMethod(new ScrollingMovementMethod());
        return rootView;

    }
}
