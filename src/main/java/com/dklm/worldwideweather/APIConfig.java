package com.dklm.worldwideweather;

import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class APIConfig {

    static final String GEOCODE_API_KEY = "6605a52f94949045597836mxe984877";
    static final String GEOCODE_BASE_URL = "https://geocode.maps.co";
    static final String NWS_BASE_URL = "https://api.weather.gov";
    static final String USER_AGENT = "MyWeatherApp (contact@myweatherapp.com)";
    static final int CONNECTION_TIMEOUT = 10000; // Timeout in milliseconds
    static final int READ_TIMEOUT = 10000; // Read timeout in milliseconds

    static void useApiKey() {
        System.out.println("Using API Key: " + GEOCODE_API_KEY);
        System.out.println("API Key used successfully.");
    }

    public static String callGeocodeAPI(String address, String apiKey) {
        String encodedAddress = address.replace(" ", "+");
        String geoURL = GEOCODE_BASE_URL + "/search?q=" + encodedAddress + "&api_key=" + apiKey;
        System.out.println("Calling Geocode API with URL: " + geoURL);
        return geoHttpRequest(geoURL);
    }

    public static String callNWSAPI(double latitude, double longitude) {
        String nwsURL = NWS_BASE_URL + "/points/" + latitude + "," + longitude;
        return nwsHttpRequest(nwsURL);
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
            System.err.println("Error during API call: " + e.getMessage());
            return null; // or appropriate error handling
        }
    }

    public static double[] parseCoordinates(String jsonResponse) {
        try (JsonReader reader = Json.createReader(new StringReader(jsonResponse))) {
            JsonObject rootObject = reader.readObject();
            JsonArray results = rootObject.getJsonArray("results");
            if (results != null && !results.isEmpty()) {
                JsonObject firstResult = results.getJsonObject(0);
                JsonObject geometry = firstResult.getJsonObject("geometry");
                if (geometry != null) {
                    JsonObject location = geometry.getJsonObject("location");
                    if (location != null) {
                        BigDecimal latitude = location.getJsonNumber("lat").bigDecimalValue().setScale(5, BigDecimal.ROUND_HALF_UP);
                        BigDecimal longitude = location.getJsonNumber("lon").bigDecimalValue().setScale(5, BigDecimal.ROUND_HALF_UP);
                        return new double[]{latitude.doubleValue(), longitude.doubleValue()};
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("General error parsing geocoding response: " + e.getMessage());
        }
        return new double[]{0.0, 0.0};
    }

    public static String parseAddress(String jsonResponse) {
        try (JsonReader reader = Json.createReader(new StringReader(jsonResponse))) {
            JsonArray jsonArray = reader.readArray();
            if (jsonArray != null && jsonArray.size() > 0) {
                JsonObject jsonObject = jsonArray.getJsonObject(0);
                return jsonObject.getString("formattedAddress");
            }
        } catch (Exception e) {
            System.err.println("General error parsing reverse geocoding response: " + e.getMessage());
        }
        return "Unknown Location";
    }
}
