package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class quakeadapter extends ArrayAdapter <Earthquakes> {


    public quakeadapter(@NonNull Context context, @NonNull List<Earthquakes> objects) {
        super(context, 0, objects);
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    private int getMagnitudeColor (double magnitude){
        int color;
        int magnitudeint = (int) Math.floor(magnitude);
        switch (magnitudeint){
            case 1  : color = R.color.magnitude1;
            break;
            case 2  : color = R.color.magnitude2;
                break;
            case 3  : color = R.color.magnitude3;
                break;
            case 4  : color = R.color.magnitude4;
                break;
            case 5  : color = R.color.magnitude5;
                break;
            case 6  : color = R.color.magnitude6;
                break;
            case 7  : color = R.color.magnitude2;
                break;
            case 8  : color = R.color.magnitude8;
                break;
            case 9  : color = R.color.magnitude9;
                break;
            case 10  : color = R.color.magnitude10plus;
                break;
            default:  color = R.color.magnitude1;
                break;
        }
        return ContextCompat.getColor(getContext(), color);

    }




    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        return super.getView(position, convertView, parent);

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.earthquake_listitem, parent, false);
        }
        Earthquakes currentitem = getItem(position);

        DecimalFormat formatter = new DecimalFormat("0.0");
        String decmagnitude = formatter.format(currentitem.getmagnitute());

        TextView magnitudeview = (TextView) listItemView.findViewById(R.id.magnitude);
        magnitudeview.setText(decmagnitude);
String Cityname;
String nearthe;
        if (currentitem.getcity().contains("of")){
            String [] parts = currentitem.getcity().split("of");
            nearthe = parts [0] + " of";
            Cityname = parts [1];
        }
        else {
            nearthe = getContext().getString(R.string.near_the);
            Cityname= currentitem.getcity();
        }
        TextView near = listItemView.findViewById(R.id.nearcity);
        near.setText(nearthe);

        TextView city = (TextView) listItemView.findViewById(R.id.city);
        city.setText(Cityname);

        Date dateobj = new Date(currentitem.getdate());

        TextView date = (TextView) listItemView.findViewById(R.id.date);
        date.setText(formatDate(dateobj));

        TextView time = listItemView.findViewById(R.id.time);
        time.setText(formatTime(dateobj));


        // code for adding magnitude circlebackground

        GradientDrawable magnitudecircle = (GradientDrawable) magnitudeview.getBackground();

        int magnitudecolor = getMagnitudeColor(currentitem.getmagnitute());
        magnitudecircle.setColor(magnitudecolor);




        return listItemView;
    }



}
