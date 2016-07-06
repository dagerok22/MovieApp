package com.example.generalkenobi.movieapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity {

    public static String MOVIE_API_KEY = "b9c1479b159d88713875a39c5babecdd";
    private LruCache<String, Bitmap> mMemoryCache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if(!preferences.contains("page"))
        {
            preferences.edit().clear().commit();
            preferences.edit().putString("page", "1").commit();
        }


        OkHttpClient picassoClient = new OkHttpClient();
        //picassoClient.interceptors().add(new OAuth2Interceptor());
        File cache = CacheUtils.createDefaultCacheDir(this);
        picassoClient.setCache(new com.squareup.okhttp.Cache(cache, CacheUtils.calculateDiskCacheSize(cache)));

        Picasso picasso = new Picasso.Builder(this)
                .defaultBitmapConfig(Bitmap.Config.RGB_565)
                .indicatorsEnabled(BuildConfig.DEBUG)
                .downloader(new OkHttpDownloader(picassoClient))
                .build();

        Picasso.setSingletonInstance(picasso);


        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MovieFragment())
//                    .add(R.id.container, new ButtonsFragment())
                    .commit();
        }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this,SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
