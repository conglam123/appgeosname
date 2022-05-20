package com.team.android.geonames;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import pl.droidsonroids.gif.GifImageView;

public class CountryActivity extends AppCompatActivity {

    private static final String GEONAME_API_KEY= "ngotruongkhai";
    private ProgressBar progressBar;
    private GifImageView ivNation;
    private TextView tvNameCountry;
    private TextView tvCapital;
    private TextView tvArea;
    private TextView tvPopulation;
    private GifImageView ivFlag;
    private CountryClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_country);

        // Fetch views
        ivNation = (GifImageView) findViewById(R.id.ivNation);
        tvNameCountry = (TextView) findViewById(R.id.tvCoutryName);
        tvCapital = (TextView) findViewById(R.id.tvCapital);
        tvArea = (TextView) findViewById(R.id.tvArea);
        tvPopulation = (TextView) findViewById(R.id.tvPopulation);
        ivFlag = (GifImageView) findViewById(R.id.ivFlag);

        String coutryCode = getIntent().getStringExtra(MainActivity.COUNTRY_DETAIL_KEY);
        Log.e("Kiem tra Country Activity", coutryCode);
        loadCountry(coutryCode);

    }

    // Populate data for the book
    private void loadCountry(String countryCode) {

        // fetch extra book data from books API
        client = new CountryClient();
        client.getExtraCountryDetails(GEONAME_API_KEY, countryCode, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                try {
                    JSONArray docs = null;
                    if(response.has("geonames")) {
                        Log.e("Kiem tra Country Activity", response.toString());
                        // Get the docs json array
                        docs = response.getJSONArray("geonames");
                        // Parse json array into array of model objects
                        final ArrayList<Country> countries = Country.fromJson(docs);

                        //change activity title
                        Log.e("Kiem tra Country Activity", countries.get(0).getCountryName());
                        CountryActivity.this.setTitle(countries.get(0).getCountryName());
                        // Populate data
                        String nationUrl = "https://img.geonames.org/img/country/250/"+ countries.get(0).getCountryCode().toUpperCase() + ".png";
                        Log.e("Kiem tra Country Activity", nationUrl);
                        Picasso.with(getApplicationContext())
                                .load(nationUrl)
                                .placeholder(R.drawable.loading)
                                .error(R.drawable.stub)
                                .into(ivNation);
                        tvNameCountry.setText(countries.get(0).getCountryName());
                        tvArea.setText(countries.get(0).getAreaInSqKm() + " km²");
                        tvCapital.setText(countries.get(0).getCapital());
                        tvPopulation.setText(Integer.toString(countries.get(0).getPopulation()) + " people");
                        String flagUrl = "https://img.geonames.org/flags/x/"+ countries.get(0).getCountryCode().toLowerCase() + ".gif";
                        Log.e("Kiem tra Country Activity", flagUrl);
                        Picasso.with(getApplicationContext())
                                .load(flagUrl)
                                .placeholder(R.drawable.loading)
                                .error(R.drawable.stub)
                                .into(ivFlag);
                    }
                } catch (JSONException e) {
                    // Invalid JSON format, show appropriate error.
                    e.printStackTrace();
                }
            }
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getApplicationContext(), "Load Failure", Toast.LENGTH_SHORT).show();
            }
        });


    }
}