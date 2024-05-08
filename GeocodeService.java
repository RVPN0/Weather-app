package com.dklm.worldwideweather;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class GeocodeService extends APIConfig {

    private static final HttpClient client = HttpClient.newHttpClient();

    public static double[] forwardGeocode(String address) {
        useApiKey();  // Log API key usage
        double[] cachedCoordinates = CoordinateCache.getCoordinatesFromCache(address);
        if (cachedCoordinates != null) {
            return cachedCoordinates;
        }

        try {
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
            String url = String.format("%s/search?q=%s&api_key=%s", GEOCODE_BASE_URL, encodedAddress, GEOCODE_API_KEY);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", USER_AGENT)
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                double[] coordinates = parseCoordinates(response.body());
                CoordinateCache.addCoordinatesToCache(address, coordinates[0], coordinates[1]);
                return coordinates;
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error during forward geocoding: " + e.getMessage());
        }
        return new double[]{0.0, 0.0};  // Default return value in case of failure
    }

    public static String reverseGeocode(double latitude, double longitude) {
        try {
            String url = String.format("%s/reverse?lat=%f&lon=%f&api_key=%s", GEOCODE_BASE_URL, latitude, longitude, GEOCODE_API_KEY);
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .header("User-Agent", USER_AGENT)
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return parseAddress(response.body());
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error during reverse geocoding: " + e.getMessage());
        }
        return "Unknown Location"; // Default return value in case of failure
    }

    private static double[] parseCoordinates(String jsonResponse) {
        try (JsonReader reader = Json.createReader(new StringReader(jsonResponse))) {
            JsonArray results = reader.readArray();
            if (results.size() > 0) {
                JsonObject jsonObject = results.getJsonObject(0);
                double latitude = jsonObject.getJsonNumber("lat").doubleValue();
                double longitude = jsonObject.getJsonNumber("lon").doubleValue();
                return new double[]{latitude, longitude};
            }
        } catch (Exception e) {
            System.out.println("Error parsing geocoding response: " + e.getMessage());
        }
        return new double[]{0.0, 0.0};
    }

    private static String parseAddress(String jsonResponse) {
        try (JsonReader reader = Json.createReader(new StringReader(jsonResponse))) {
            JsonObject jsonObject = reader.readObject().getJsonObject("address");
            return jsonObject.getString("formattedAddress");
        } catch (Exception e) {
            System.out.println("Error parsing reverse geocoding response: " + e.getMessage());
        }
        return "Unknown Location";
    }
}
