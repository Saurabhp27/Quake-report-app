package com.example.android.quakereport;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import java.util.List;


public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquakes>> {
private String murl;
    public EarthquakeLoader(@NonNull Context context, String url) {
        super(context);
        murl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<Earthquakes> loadInBackground() {
        if (murl == null) {
            return null;
        }

        List<Earthquakes> earthquakes = QueryUtils.fetchearthquakedata(murl);
        return earthquakes;
    }
}
