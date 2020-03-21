package com.example.testweatherapi;

import android.net.Uri;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {
    // Base URL for Weather API.
    private static final String BOOK_BASE_URL = "http://api.openweathermap.org/data/2.5/weather?";
    // Parameter for the search string.
    private static final String QUERY_PARAM = "q";
    // App ID parameter
    private static final String APPID = "appid";

    static String getBookInfo(String queryString) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String weatherJSONString = null;

        try {
            Uri builtURI = Uri.parse(BOOK_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, queryString)
                    .appendQueryParameter(APPID, "2ff92a93d52829bb522480069b6ab479")
                    .build();

            URL requestURL = new URL(builtURI.toString());


            urlConnection = (HttpURLConnection) requestURL.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Get the InputStream.
            InputStream inputStream = urlConnection.getInputStream();

            // Create a buffered reader from that input stream.
            reader = new BufferedReader(new InputStreamReader(inputStream));

            // Use a StringBuilder to hold the incoming response.
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                // Since it's JSON, adding a newline isn't necessary (it won't
                // affect parsing) but it does make debugging a *lot* easier
                // if you print out the completed buffer for debugging.
                builder.append("\n");
            }

            if (builder.length() == 0) {
                // Stream was empty. No point in parsing.
                return null;
            }

            weatherJSONString = builder.toString();


        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return weatherJSONString;
    }
}
