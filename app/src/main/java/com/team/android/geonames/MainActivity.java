package com.team.android.geonames;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuItemImpl;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    public static final String COUNTRY_DETAIL_KEY = "country";
    public static final String LINK = "link";
    private ListView lvCountries;
    private ProgressBar progressBar;
    private CountriesAdapter countriesAdapter;
    private CountryClient client;
    private static final String GEONAME_API_KEY= "ngotruongkhai";
    private ArrayList<Country> mCountrys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvCountries = (ListView) findViewById(R.id.lvBooks);
        mCountrys = new ArrayList<Country>();

        // initialize the adapter
        countriesAdapter = new CountriesAdapter(this, mCountrys);

        // attach the adapter to the ListView
        lvCountries.setAdapter(countriesAdapter);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        setupCountrySelectedListener();

        fetchCountries(GEONAME_API_KEY);

    }

    public void setupCountrySelectedListener() {
        lvCountries.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Launch the detail view passing book as an extra
                Intent intent = new Intent(MainActivity.this, CountryActivity.class);
                intent.putExtra(COUNTRY_DETAIL_KEY, countriesAdapter.getItem(position).getCountryCode());
                startActivity(intent);
            }
        });
    }

    // Executes an API call to the OpenLibrary search endpoint, parses the results
    // Converts them into an array of book objects and adds them to the adapter
    private void fetchCountries(String username) {
        // Show progress bar before making network request
        progressBar.setVisibility(ProgressBar.VISIBLE);
        Toast.makeText(getApplicationContext(), "Loading", Toast.LENGTH_SHORT).show();
        client = new CountryClient();
        client.getCountries(username, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    // hide progress bar
                    progressBar.setVisibility(ProgressBar.GONE);
                    Toast.makeText(getApplicationContext(), "Load Success", Toast.LENGTH_SHORT).show();
                    JSONArray docs = null;
                    if(response != null) {
                        // Get the docs json array
                        docs = response.getJSONArray("geonames");
                        // Parse json array into array of model objects
                        final ArrayList<Country> countries = Country.fromJson(docs);
                        // Remove all books from the adapter
                        countriesAdapter.clear();
                        // Load model objects into the adapter
                        for (Country book : countries) {
                            countriesAdapter.add(book); // add book through the adapter
                        }
                        countriesAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    // Invalid JSON format, show appropriate error.
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressBar.setVisibility(ProgressBar.GONE);
                Toast.makeText(getApplicationContext(), "Load Failure", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                Log.e("Nomad", "onQueryTextSubmit");
                ArrayList<Country> fillerCountries = new ArrayList<Country>();

                for (Country country : mCountrys) {
                    if (country.getCountryName().toLowerCase().contains(s.toLowerCase())) {
                        fillerCountries.add(country);
                    }
                }

                countriesAdapter = new CountriesAdapter(getApplicationContext(), fillerCountries);
                lvCountries.setAdapter(countriesAdapter);

                searchView.clearFocus();
                searchItem.collapseActionView();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                Log.e("Nomad", "onQueryTextSubmit");
                ArrayList<Country> fillerCountries = new ArrayList<Country>();

                for (Country country : mCountrys) {
                    if (country.getCountryName().toLowerCase().contains(s.toLowerCase())) {
                        fillerCountries.add(country);
                    }
                }

                countriesAdapter = new CountriesAdapter(getApplicationContext(), fillerCountries);
                lvCountries.setAdapter(countriesAdapter);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}