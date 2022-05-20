package com.team.android.geonames;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;

public class CountriesAdapter extends ArrayAdapter<Country> {

    private LayoutInflater layoutInflater;
    private ArrayList<Country> mCountries;

    public CountriesAdapter(@NonNull Context context, ArrayList<Country> mCountries) {
        super(context, android.R.layout.simple_list_item_1, mCountries);
        this.layoutInflater = LayoutInflater.from(context);
        this.mCountries = mCountries;
    }

    // View lookup cache
    private static class ViewHolder{
        public GifImageView ivFlag;
        public TextView tvNameCountry;

    }

    // Translates a particular `Book` given a position
    // into a relevant row within an AdapterView
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = layoutInflater.inflate(R.layout.country_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvNameCountry = convertView.findViewById(R.id.tvNameCountry);
            viewHolder.ivFlag = convertView.findViewById(R.id.ivFlagCover);
            convertView.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Get the data item for this position
        final Country country = getItem(position);

        // Populate the data into the template view using the data object
        viewHolder.tvNameCountry.setText(country.getCountryName());

        String flagUrl = "https://img.geonames.org/flags/x/"+ country.getCountryCode().toLowerCase() + ".gif";
        Log.e("Kiem tra", flagUrl);
//        Glide.with(getContext())
//                .load(flagUrl)
//                .placeholder(R.drawable.loading__)
//                .into(viewHolder.ivFlag);
        Picasso.with(getContext())
                .load(flagUrl)
                .placeholder(R.drawable.loading)
                .error(R.drawable.stub)
                .into(viewHolder.ivFlag);

        // Return the completed view to render on screen
        return convertView;
    }
}
