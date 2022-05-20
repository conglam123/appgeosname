package com.team.android.geonames;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Country {
    private String capital;
    private int population;
    private String areaInSqKm;
    private String countryCode;
    private String countryName;
    private String continentName;

    public Country() {
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        this.capital = capital;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public String getAreaInSqKm() {
        return areaInSqKm;
    }

    public void setAreaInSqKm(String areaInSqKm) {
        this.areaInSqKm = areaInSqKm;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getContinentName() {
        return continentName;
    }

    public void setContinentName(String continentName) {
        this.continentName = continentName;
    }

    // Decodes array of book json results into business model objects
    public static ArrayList<Country> fromJson(JSONArray jsonArray) {
        ArrayList<Country> countries = new ArrayList<Country>(jsonArray.length());
        // Process each result in json array, decode and convert to business
        // object
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject countriesJson = null;
            try {
                countriesJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            Country country = Country.fromJson(countriesJson);
            if (country != null) {
                countries.add(country);
            }
        }
        return countries;
    }

    // Returns a Book given the expected JSON
    public static Country fromJson(JSONObject jsonObject) {
        Country country = new Country();
        try {
            // Deserialize json into object fields
            // Check if a cover edition is available
            country.setCapital(jsonObject.getString("capital"));
            country.setPopulation(jsonObject.getInt("population"));
            country.setAreaInSqKm(jsonObject.getString("areaInSqKm"));
            country.setCountryCode(jsonObject.getString("countryCode"));
            country.setCountryName(jsonObject.getString("countryName"));

            //Log.e("Kiem tra", country.getCountryName());

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        // Return new object
        return country;
    }

}
