package com.dklm.worldwideweather;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

public class APIConfig {

    static final String GEOCODE_API_KEY = "6605a52f94949045597836mxe984877";
    static final String GEOCODE_BASE_URL = "https://geocode.maps.co";
    static final String NWS_BASE_URL = "https://api.weather.gov";
    static final String USER_AGENT = "MyWeatherApp (contact@myweatherapp.com)";
    static final int CONNECTION_TIMEOUT = 10000; // Timeout in milliseconds

    static void useGeoApiKey() {
        System.out.println("Using API Key: " + GEOCODE_API_KEY);
        System.out.println("API Key used successfully.");
    }

    public static String callGeocodeAPI(String address, String apiKey) {
        String encodedAddress = address.replace(" ", "+");
        String geoURL = GEOCODE_BASE_URL + "/search?q=" + encodedAddress + "&api_key=" + apiKey;
        System.out.println("Calling Geocode API with URL: " + geoURL);
        System.out.println("");
        return geoHttpRequest(geoURL);
    }

    private static String geoHttpRequest(String geoURL) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(geoURL))
                .header("User-Agent", USER_AGENT)
                .timeout(java.time.Duration.ofMillis(CONNECTION_TIMEOUT))
                .build();
        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            System.err.println("Error during API call: " + e.getMessage());
            return null; // or appropriate error handling
        }
    }

    static void useNWSApi() {
        System.out.println("Using the National Weather Service API");
        System.out.println("API Key used successfully.");
    }

    public static String callNWSAPI(double latitude, double longitude) {
        String nwsURL = NWS_BASE_URL + "/points/" + latitude + "," + longitude;
        System.out.println("Calling NWS API with URL: " + nwsURL);
        System.out.println("");
        return nwsHttpRequest(nwsURL);
    }

    private static String nwsHttpRequest(String nwsURL) {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(nwsURL))
                .header("User-Agent", USER_AGENT)
                .timeout(java.time.Duration.ofMillis(CONNECTION_TIMEOUT))
                .build();
        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            System.err.println("Error during NWS API call: " + e.getMessage());
            return null; // or appropriate error handling
        }
    }

// create a class that takes the url given by callNWSAPI and calls it to get the forecast data
    public static String getForecastData(String forecastUrl) throws IOException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(forecastUrl))
                .header("User-Agent", USER_AGENT)
                .timeout(java.time.Duration.ofMillis(CONNECTION_TIMEOUT))
                .build();
        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            System.err.println("Error during NWS API call: " + e.getMessage());
            return null; // or appropriate error handling
        }
    }

}
