package com.example.android.quakereport;

public class Earthquakes {

    Double magnitudes;
    String cities;
    Long  dates;
    String urls;
    public Earthquakes(Double magnitude, String city, Long date, String url){
        magnitudes = magnitude;
        cities = city;
        dates = date;
        urls =url;
    }

    public Double getmagnitute(){
        return magnitudes;
    }

    public String getcity(){
        return cities;
    }
    public String geturl(){
        return urls;
    }

    public Long getdate(){
        return dates;
    }
}
