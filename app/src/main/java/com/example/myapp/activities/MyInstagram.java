package com.example.myapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.myapp.adapters.InstagramPhotosAdapter;
import com.example.myapp.myinstagram.R;
import com.example.myapp.models.InstagramPhoto;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MyInstagram extends Activity {

    public static final String CLIENT_ID= "4d3f3c0229dc49e98f82e73ac05f3c13";
    private ArrayList<InstagramPhoto> photos;
    private InstagramPhotosAdapter aPhotos;
    private SwipeRefreshLayout swipeContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_instagram);
        fetchPopularPhotos();

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
                //fetchPopularPhotos();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    public void fetchTimelineAsync(int page) {
        Log.d("DEBUG", "Testing");
        fetchPopularPhotos();
    }

    private void fetchPopularPhotos() {
        photos = new ArrayList<InstagramPhoto>(); // initialize arraylist
        // Create adapter bind it to the data in arraylist

        aPhotos = new InstagramPhotosAdapter(this, photos);

        // populater the data into the listView
        ListView lvPhotos = (ListView)findViewById(R.id.lvPhotos);

        // set the adapter to the listview (population of items)

        lvPhotos.setAdapter(aPhotos);



        //https://api.instagram.com/v1/media/popular?client_id=4d3f3c0229dc49e98f82e73ac05f3c13
        //set up popular url endpoint
        String popularUrl = "https://api.instagram.com/v1/media/popular?client_id=4d3f3c0229dc49e98f82e73ac05f3c13";
        // Create the network client
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.get(popularUrl, new JsonHttpResponseHandler() {
            // define success and failure callbacks.
            // handle the successful response (popular photos

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                // ...the data has come back, finish populating listview...
                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);

                // fired once the successful response back
                // response is == popular photo json
                //super.onSuccess(statusCode, headers, response);
                //Log.i("INFO", response.toString());
                // url, height, username, caption
                JSONArray photosJSON;
                try {
                    photos.clear();
                    photosJSON = response.getJSONArray("data");
                    for (int i = 0; i < photosJSON.length(); i++) {
                        JSONObject photoJSON = photosJSON.getJSONObject(i);
                        InstagramPhoto photo = new InstagramPhoto();
                        photo.username = photoJSON.getJSONObject("user").getString("username");
                        if ( !photoJSON.isNull("caption")) {
                            photo.caption = photoJSON.getJSONObject("caption").getString("text");
                        }
                        if ( !photoJSON.isNull("profile_picture")) {
                            photo.profile_picture = photoJSON.getJSONObject("user").getString("profile_picture");
                        }

                        photo.imageUrl = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getString("url");
                        photo.imageHeight = photoJSON.getJSONObject("images").getJSONObject("standard_resolution").getInt("height");
                        photo.likes_count = photoJSON.getJSONObject("likes").getInt("count");
                        //Log.i("DEBUG", photo.toString());
                        photos.add(photo);
                    }

                    // Notified the adapter that it should populate the listview
                    aPhotos.notifyDataSetChanged();
                } catch (JSONException e) {
                    // Fire if things fail , json parsing is invalid
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
        // Trigger the network request

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_instagram, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
