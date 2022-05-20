package com.team.android.geonames;

import android.app.ProgressDialog;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CountryClient {
    //http://api.geonames.org/countryInfoJSON?username=ngotruongkhai
    //http://api.geonames.org/countryInfoJSON?username=ngotruongkhai&country=VN
    private static final String API_BASE_URL = "http://api.geonames.org/countryInfoJSON";
    private AsyncHttpClient client;

    public CountryClient() {
        this.client = new AsyncHttpClient();
    }

    private String getApiUrl(String relativeUrl) {
        return API_BASE_URL + relativeUrl;
    }

    // Method for accessing the search API
    public void getCountries(final String username, JsonHttpResponseHandler handler) {
        String url = getApiUrl("?username=");
        client.get(url + username, handler);
        Log.e("Kiem tra Client", url + username);
    }

    // Method for accessing books API to get publisher and no. of pages in a book.
    public void getExtraCountryDetails(final String username, String countryCode, JsonHttpResponseHandler handler) {
        String url = getApiUrl("?username=");
        client.get(url + username + "&country=" + countryCode, handler);
    }
}
