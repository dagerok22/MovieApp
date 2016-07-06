package com.example.generalkenobi.movieapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieFragment extends Fragment {

    final String LOG_TAG = "movieapp";
    public ArrayList<MovieItem> movieItemsArray;
    public ImageAdapter imgAdapter;
    private View rootView;
    static Boolean mUpdated = false;
    public SharedPreferences preferences;
    public com.example.generalkenobi.movieapp.FloatingActionButton mFab_btn_next;
    public com.example.generalkenobi.movieapp.FloatingActionButton mFab_btn_backt;
    public MovieFragment() {
    }

    @Override
    public void onStart() {
        super.onStart();

        if(!mUpdated)
            updateMovies();
    }
    private void updateMovies() {
        mUpdated = true;

        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        fetchMoviesTask.execute("dasdasd");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(getContext(),SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        movieItemsArray = new ArrayList<MovieItem>();
        FetchMoviesTask fetchMoviesTask = new FetchMoviesTask();
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());


        rootView = inflater.inflate(R.layout.fragment_main, container, false);

//        GridView movieGridView = (GridView) rootView.findViewById(R.id.movieGridView);
//
//        imgAdapter = new ImageAdapter(getContext(), movieItemsArray);
//        movieGridView.setAdapter(imgAdapter);


        com.example.generalkenobi.movieapp.FloatingActionButton mFab_btn_next = new com.example.generalkenobi.movieapp.FloatingActionButton.Builder(getActivity())
                .withDrawable(getResources().getDrawable(R.drawable.arrows))
                .withButtonColor(Color.WHITE)
                .withButtonSize(80)
                .withGravity(Gravity.BOTTOM | Gravity.RIGHT)
                .withMargins(0, 0, 5, 5)
                .create();

        final com.example.generalkenobi.movieapp.FloatingActionButton mFab_btn_back = new com.example.generalkenobi.movieapp.FloatingActionButton.Builder(getActivity())
                .withDrawable(getResources().getDrawable(R.drawable.back1))
                .withButtonColor(Color.WHITE)
                .withButtonSize(80)
                .withGravity(Gravity.BOTTOM | Gravity.LEFT)
                .withMargins(5, 0, 0, 5)
                .create();

//        btn_back = (FancyButton) .findViewById(R.id.btn_back);
//        btn_next = (FancyButton) container.findViewById(R.id.btn_next);
//
//        btn_back.setText("  <  ");
//        btn_next.setText("  >  ");


        if(preferences.getString("page", "1").equals("1"))
            mFab_btn_back.hideFloatingActionButton();
        mFab_btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(preferences.getString("page", "1").equals("1"))
                    mFab_btn_back.showFloatingActionButton();
                preferences.edit().putString("page", Integer.valueOf(preferences.getString("page", "1")) + 1 + "").commit();
                mUpdated = true;
                updateMovies();
            }
        });
        mFab_btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //preferences = PreferenceManager.getDefaultSharedPreferences(getContext());

                if(preferences.getString("page", "1").equals("2"))
                    mFab_btn_back.hideFloatingActionButton();
                if(Integer.valueOf(preferences.getString("page", "1")) > 1) {
                    preferences.edit().putString("page", Integer.valueOf(preferences.getString("page", "1")) - 1 + "").commit();
                    mUpdated = true;
                    updateMovies();
                }
            }
        });


        return rootView;
    }


    private void addItemsToList(MovieItem item) {
        movieItemsArray.add(item);
        imgAdapter.notifyDataSetChanged();
    }
//
//    private String getBackgroundFromJSON(String movieJsonStr) throws JSONException{
//        return new JSONObject(movieJsonStr).getString("backdrop_path");
//    }

    private ArrayList<MovieItem> getMovieListFromJson(String movieJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final int MOVIE_COUNT_NUMBER = 20;
        final String OWM_RESULTS = "results";
        final String OWM_POSTER_PATH = "poster_path";
        final String OWM_BG_POSTER = "backdrop_path";
        final String OWM_ORIGINAL_TITLE = "original_title";
        final String OWM_OVERVIEW = "overview";
        final String OWM_ID = "id";
        final String OWM_VOTE_AVERAGE = "vote_average";
        final String OWM_VOTE_COUNT = "vote_count";
        final String OWM_RELEASE_DATE = "release_date";

        ArrayList<MovieItem> itemsArray = new ArrayList<>();

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieResults = movieJson.getJSONArray(OWM_RESULTS);

        for (int i = 0;i < movieResults.length();i++){
            JSONObject movieItem = movieResults.getJSONObject(i);

//            if(movieItem.getString(OWM_BG_POSTER).equals("null")){
//                Log.e("Links test", movieItem.getString(OWM_BG_POSTER) + "");
//                GetBackground getBackgroundTask = new GetBackground();
//                getBackgroundTask.execute(movieItem.getInt(OWM_ID) + "", i + "");
//
//            }
            itemsArray.add(new MovieItem(movieItem.getString(OWM_OVERVIEW),
                    movieItem.getString(OWM_ORIGINAL_TITLE),
                    movieItem.getInt(OWM_ID),
                    movieItem.getDouble(OWM_VOTE_AVERAGE),
                    movieItem.getDouble(OWM_VOTE_COUNT),
                    movieItem.getString(OWM_RELEASE_DATE),
                    movieItem.getString(OWM_POSTER_PATH),
                    movieItem.getString(OWM_BG_POSTER)));



        }

        // OWM returns daily forecasts based upon the local time of the city that is being
        // asked for, which means that we need to know the GMT offset to translate this data
        // properly.

        // Since this data is also sent in-order and the first day is always the
        // current day, we're going to take advantage of that to get a nice
        // normalized UTC date for all of our weather.


        return itemsArray;
    }

//
//    public class GetBackground extends AsyncTask<String, Void, String> {
//        Integer index;
//        @Override
//        protected String doInBackground(String... params) {
//            index = Integer.valueOf(params[1]);
//            String LOG_TAG = this.getClass().getSimpleName();
//            // If there's no zip code, there's nothing to look up.  Verify size of params.
//            if (params.length == 0) {
//                HttpURLConnection urlConnection = null;
//                BufferedReader reader = null;
//
//                // Will contain the raw JSON response as a string.
//                String theMovieJsonString = null;
//
//
//                final String SORT_PARAM = "sort_by";
//                final String PAGE_PARAM = "page";
//                final String APPID_PARAM = "api_key";
//
//                try {
//                    // Construct the URL for the OpenWeatherMap query
//                    // Possible parameters are avaiable at OWM's forecast API page, at
//                    // http://openweathermap.org/API#forecast
//                    final String MOVIE_BASE_URL =
//                            "https://api.themoviedb.org/3/movie/" + params[0];
//
//                    Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
//                            .appendQueryParameter(APPID_PARAM, BuildConfig.THE_MOVIE_API_KEY)
//                            .build();
//                    Log.e("Links test", builtUri.toString());
//
//                    URL url = new URL(builtUri.toString());
//                    //URL url = new URL("https://api.themoviedb.org/3/movie/now_playing?sort_by=popularity.desc&page=1&api_key=b9c1479b159d88713875a39c5babecdd");
//
//                    // Create the request to OpenWeatherMap, and open the connection
//                    urlConnection = (HttpURLConnection) url.openConnection();
//                    urlConnection.setRequestMethod("GET");
//                    urlConnection.connect();
//
//                    // Read the input stream into a String
//                    InputStream inputStream = urlConnection.getInputStream();
//                    StringBuffer buffer = new StringBuffer();
//                    if (inputStream == null) {
//                        // Nothing to do.
//                        return null;
//                    }
//                    reader = new BufferedReader(new InputStreamReader(inputStream));
//
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
//                        // But it does make debugging a *lot* easier if you print out the completed
//                        // buffer for debugging.
//                        buffer.append(line + "\n");
//                    }
//
//                    if (buffer.length() == 0) {
//                        // Stream was empty.  No point in parsing.
//                        return null;
//                    }
//                    theMovieJsonString = buffer.toString();
//                } catch (IOException e) {
//
//                    // If the code didn't successfully get the weather data, there's no point in attemping
//                    // to parse it.
//                    return null;
//                } finally {
//                    if (urlConnection != null) {
//                        urlConnection.disconnect();
//                    }
//                    if (reader != null) {
//                        try {
//                            reader.close();
//                        } catch (final IOException e) {
//
//                        }
//                    }
//                }
//                try {
//                    return getBackgroundFromJSON(theMovieJsonString);
//                }
//                catch (JSONException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            movieItemsArray.get(index).bgPosterPath = s;
//            imgAdapter.notifyDataSetChanged();
//        }
//    }

    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<MovieItem>>{
        @Override
        protected ArrayList<MovieItem> doInBackground(String... params) {

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String theMovieJsonString = null;

            String format = "json";
            String units = "metric";
            int numDays = 7;

            final String SORT_PARAM = "sort_by";
            final String PAGE_PARAM = "page";
            final String APPID_PARAM = "api_key";

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                final String MOVIE_BASE_URL =
                        "https://api.themoviedb.org/3/movie/now_playing";

                Uri builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM, "popularity.desc")
                        .appendQueryParameter(PAGE_PARAM, preferences.getString("page", "1"))
                        .appendQueryParameter(APPID_PARAM, BuildConfig.THE_MOVIE_API_KEY)
                        .build();
                Log.e("Links test", builtUri.toString());

                URL url = new URL(builtUri.toString());
                //URL url = new URL("https://api.themoviedb.org/3/movie/now_playing?sort_by=popularity.desc&page=1&api_key=b9c1479b159d88713875a39c5babecdd");

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                theMovieJsonString = buffer.toString();
            } catch (IOException e) {

                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {

                    }
                }
            }

            try {
                return getMovieListFromJson(theMovieJsonString);
            } catch (JSONException e) {

                e.printStackTrace();
            }
            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<MovieItem> result) {
            if (result != null) {
//
//                imgAdapter.clear();
//                for(MovieItem movieItem : result) {
//                    imgAdapter.add(movieItem);
//                }
//
                movieItemsArray = result;
                GridView movieGridView = (GridView) rootView.findViewById(R.id.movieGridView);
                imgAdapter = new ImageAdapter(getActivity(), movieItemsArray);
                movieGridView.setAdapter(imgAdapter);
                //imgAdapter.notifyDataSetChanged();
                // New data is back from the server.  Hooray!
            }
        }
    }
    //        @Override
//        protected void onPostExecute(String[] result) {
////            if (result != null) {
////                mForecastAdapter.clear();
////                for(String dayForecastStr : result) {
////                    mForecastAdapter.add(dayForecastStr);
////                }
////                // New data is back from the server.  Hooray!
////            }
//        }




}
