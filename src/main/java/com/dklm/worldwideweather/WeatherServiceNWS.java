package com.dklm.worldwideweather;

import java.io.IOException;

public class WeatherServiceNWS extends APIConfig {

    // Fetch the Data for a given latitude and longitude
    public static String[] forwardNWS(double latitude, double longitude) throws IOException, InterruptedException {
        useNWSApi(); // Log API usage

        String jsonResponse = callNWSAPI(latitude, longitude);
        System.out.println("JSON Response: " + jsonResponse);
        ForecastData forecastData = new ForecastData();

        if (jsonResponse != null) {
            forecastData.setJsonResponse(jsonResponse);
            String forecastUrl = forecastData.getForecastUrl();

            return new String[]{jsonResponse, getForecastData(forecastUrl)};
        } else {
            return null; // or appropriate error handling
        }
    }

}
