package com.dklm.worldwideweather;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class CoordinateData {

    private String latitude;
    private String longitude;
    private String placeId;
    private String displayName;
    private String geoJsonResponse;

    public CoordinateData() {
    }

    // Parses the JSON response
    private void parseJsonResponse(String geoJsonResponse) throws IllegalArgumentException {
        try (JsonReader reader = Json.createReader(new StringReader(geoJsonResponse))) {
            JsonObject geoData = reader.readArray().getJsonObject(0);

            this.placeId = Integer.toString(geoData.getInt("place_id"));
            this.latitude = geoData.getString("lat");
            this.longitude = geoData.getString("lon");
            this.displayName = geoData.getString("display_name");
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing Geocode API JSON response: " + e.getMessage());
        }
    }

    // Setter methods
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public static String formatCoordinate(double coordinate) {
        return String.format("%.4f", coordinate);
    }

    public void setJsonResponse(String geoJsonResponse) {
        this.geoJsonResponse = geoJsonResponse;
        System.out.println("Before: ");
        System.out.println(geoJsonResponse);
        parseJsonResponse(geoJsonResponse); // Automatically parse new JSON response
        System.out.println();
        System.out.println(" ---------------- ");
        System.out.println();

        System.out.println("After: ");
        System.out.println("Display Name: " + displayName);
        System.out.println("Place ID: " + placeId);
        coordinates();
        System.out.println(" ---------------- ");
        System.out.println("Latitude: " + latitude);
        System.out.println("Longitude: " + longitude);
    }

    // Getter methods
    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void coordinates() {
        System.out.println("Coordinates: " + latitude + ", " + longitude);
    }

    public String getJsonResponse() {
        return geoJsonResponse;
    }
}
