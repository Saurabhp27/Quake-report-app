/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.quakereport;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import java.util.ArrayList;
import java.util.List;



public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquakes>>, SharedPreferences.OnSharedPreferenceChangeListener {

//    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    private static String url_req_data = "https://earthquake.usgs.gov/fdsnws/event/1/query";

    private quakeadapter madapter;

    private static final int Loader_id = 1;

    private TextView memptextview;

    private ListView earthquakeListView;

    private ProgressBar loadingspinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        // Create a fake list of earthquake locations.
//      ArrayList earthquakes= QueryUtils.extractEarthquakes();
//        ArrayList earthquakes = QueryUtils.fetchearthquakedata(url_req_data);

//EarthquakeAsynctask task = new EarthquakeAsynctask();
//task.execute(url_req_data);
        /// replaced the above asynctask with asynctaskloader



        // Find a reference to the {@link ListView} in the layout
        earthquakeListView = (ListView) findViewById(R.id.list);

        memptextview = findViewById(R.id.emptyview);
        earthquakeListView.setEmptyView(memptextview);

        madapter = new quakeadapter(this, new ArrayList<Earthquakes>());
        earthquakeListView.setAdapter(madapter);

        loadingspinner = findViewById(R.id.loading_spinner);



        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Earthquakes currentearthquake = madapter.getItem(position);

                Intent linkto = new Intent(Intent.ACTION_VIEW);
                linkto.setData(Uri.parse(currentearthquake.geturl()));
                startActivity(linkto);


            }

        });

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nwinfo = cm.getActiveNetworkInfo();

        if (nwinfo != null && nwinfo.isConnected()){
            getSupportLoaderManager().initLoader(Loader_id,null ,this);
        }
        else{
            loadingspinner.setVisibility(View.GONE);
            memptextview.setText("No Internet Connection");

            }


        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // And register to be notified of preference changes
        // So we know when the user has adjusted the query settings
        prefs.registerOnSharedPreferenceChangeListener(this);

        }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
        if (key.equals(getString(R.string.settings_min_magnitude_key)) ||
                key.equals(getString(R.string.settings_order_by_key))){
            // Clear the ListView as a new query will be kicked off
            madapter.clear();

            // Hide the empty state text view as the loading indicator will be displayed
            memptextview.setVisibility(View.GONE);

            // Show the loading indicator while new data is being fetched
            View loadingIndicator = findViewById(R.id.loading_spinner);
            loadingIndicator.setVisibility(View.VISIBLE);

            // Restart the loader to requery the USGS as the query settings have been updated
            getSupportLoaderManager().restartLoader(Loader_id, null, this);
        }
    }


    @NonNull
    @Override
    public Loader<List<Earthquakes>> onCreateLoader(int id, @Nullable Bundle args) {

        SharedPreferences sharedpref = PreferenceManager.getDefaultSharedPreferences(this);
        String minmagnitude = sharedpref.getString(getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedpref.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        Uri baseuri = Uri.parse(url_req_data);
        Uri.Builder uribuilder= baseuri.buildUpon();

        uribuilder.appendQueryParameter("format", "geojson");
        uribuilder.appendQueryParameter("limit", "10");
        uribuilder.appendQueryParameter("minmag", minmagnitude);
        uribuilder.appendQueryParameter("orderby", orderBy);


        return new EarthquakeLoader(this, uribuilder.toString());
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Earthquakes>> loader, List<Earthquakes> earthquakes) {
        loadingspinner.setVisibility(View.GONE);
        memptextview.setText("No Earthquakes Found");

        madapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (earthquakes != null && !earthquakes.isEmpty()) {
            madapter.addAll(earthquakes);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Earthquakes>> loader) {
        madapter.clear();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}


//    private class EarthquakeAsynctask extends AsyncTask<String, Void, List<Earthquakes>> {
//
//        @Override
//        protected List<Earthquakes> doInBackground(String... urls) {
//
//            if (urls.length < 1 || urls[0] == null) {
//                return null;
//            }
//
//            List<Earthquakes> result = QueryUtils.fetchearthquakedata(urls[0]);
//            return result;
//        }
//
//        @Override
//        protected void onPostExecute(List<Earthquakes> result) {
//
//            madapter.addAll(result);
//        }
//    }
//}

