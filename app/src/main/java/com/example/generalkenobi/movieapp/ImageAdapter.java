package com.example.generalkenobi.movieapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsoluteLayout;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by GeneralKenobi on 24.06.2016.
 */
public class ImageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<MovieItem> movieList;
    private LayoutInflater inflater;
    private View rootView;
    private LruCache<String, Bitmap> mMemoryCache;




    public ImageAdapter(Context c, ArrayList<MovieItem> itemsList)
    {

        Log.e("imagead", "in imgAdapter " + itemsList.size());
        context = c;
        movieList = itemsList;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }



    //---returns the number of images---
    public int getCount() {
        return movieList.size();
    }

    //---returns the ID of an item---
    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public void clear(){
        movieList.clear();
    }

    public void add(MovieItem item){
        movieList.add(item);
        //notifyDataSetChanged();
    }
    //---returns an ImageView view---
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        Log.e("inImgAd", "getView");
        ImageView imageView;
        final MovieItem currentMovieItem = movieList.get(position);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.movie_item, parent, false);
            //imageView.setImageResource(R.drawable.photo);
            //imageView.setLayoutParams(new GridView.LayoutParams(185, 185));
            //imageView.setPadding(15, 15, 15, 15);
            //Log.e("posterPath final", movieList.get(position).getPosterPath() + "\n");
            //new ImageDownloader(imageView).execute("https://image.tmdb.org/t/p/w300" + currentMovieItem.getPosterPath());
            Picasso.with(convertView.getContext()).load("https://image.tmdb.org/t/p/w500" + currentMovieItem.getPosterPath()).into((ImageView) convertView);
//            ImageLoader imageLoader = ImageLoader.getInstance();
//            DisplayImageOptions options = new DisplayImageOptions.Builder()
//                    .cacheInMemory(true)
//                    .build();
//            imageLoader.displayImage("https://image.tmdb.org/t/p/w500" + currentMovieItem.getPosterPath(), imageView, options);
            //loadBitmap("https://image.tmdb.org/t/p/w300" + currentMovieItem.getPosterPath(), (ImageView) convertView, currentMovieItem);
            Log.e("posterpathLoading", "https://image.tmdb.org/t/p/w300" + currentMovieItem.getPosterPath());
//        } else {
//            rootView = (LinearLayout) convertView;
//            Log.e("posterpathLoading", "convertView");
//        }
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent detailIntent = new Intent(context.getApplicationContext(), DetailActivity.class);
                //Log.e("path4", currentMovieItem.bgPosterPath + "");
                //Log.e("path5", movieList.get(position).bgPosterPath);
                detailIntent.putExtra("bgPosterPath", currentMovieItem.bgPosterPath);
                detailIntent.putExtra("av_rate", currentMovieItem.VoteAverage);
                detailIntent.putExtra("title", currentMovieItem.Title);
                detailIntent.putExtra("releaseDate", currentMovieItem.ReleaseDate);
                detailIntent.putExtra("descr", currentMovieItem.OverView);
                context.startActivity(detailIntent);
            }
        });
        Log.e("posterpath", "https://image.tmdb.org/t/p/w300" + movieList.get(position).getPosterPath());
        //imageLoader.DisplayImage("https://image.tmdb.org/t/p/w300" + movieList.get(position).getPosterPath(),  imageView);
        return convertView;
    }



}