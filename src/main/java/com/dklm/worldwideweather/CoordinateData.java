package com.dklm.worldwideweather;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class CoordinateData extends APIConfig {

    private double latitude;
    private double longitude;
    private long placeId;
    private String displayName;
    private String objectClass;
    private String type;
    private String jsonResponse;

    public CoordinateData(String jsonResponse) {
        this.jsonResponse = jsonResponse;
        parseJsonResponse();
    }

    private void parseJsonResponse() {
        try (JsonReader reader = Json.createReader(new StringReader(jsonResponse))) {
            JsonObject jsonObject = reader.readArray().getJsonObject(0);

            this.placeId = jsonObject.getJsonNumber("place_id").longValue();
            this.latitude = jsonObject.getJsonNumber("lat").doubleValue();
            this.longitude = jsonObject.getJsonNumber("lon").doubleValue();
            this.displayName = jsonObject.getString("display_name");
            this.objectClass = jsonObject.getString("class");
            this.type = jsonObject.getString("type");
        } catch (Exception e) {
            System.err.println("Error parsing JSON response: " + e.getMessage());
            this.latitude = 0.0;
            this.longitude = 0.0;
        }
    }

    // Setter methods
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setPlaceId(long placeId) {
        this.placeId = placeId;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setObjectClass(String objectClass) {
        this.objectClass = objectClass;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setJsonResponse(String jsonResponse) {
        this.jsonResponse = jsonResponse;
    }

    // Getter methods
    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public long getPlaceId() {
        return placeId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getObjectClass() {
        return objectClass;
    }

    public String getType() {
        return type;
    }

    public String getJsonResponse() {
        return jsonResponse;
    }
}
