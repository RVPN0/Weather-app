package com.dklm.worldwideweather;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class ForecastData {

    private String forecastUrl;
    private String nwsJsonResponse;

    public ForecastData() {

    }

    private void parseJsonResponse(String jsonResponse) throws IllegalArgumentException {
        if (jsonResponse == null || jsonResponse.isEmpty()) {
            throw new IllegalArgumentException("Empty or null JSON response.");
        }
        try (JsonReader reader = Json.createReader(new StringReader(jsonResponse))) {
            JsonObject properties = reader.readObject().getJsonObject("properties");
            this.forecastUrl = properties.getString("forecast", null);
            if (this.forecastUrl == null) {
                throw new IllegalArgumentException("Forecast URL not found in JSON.");
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error parsing JSON response: " + e.getMessage(), e);
        }
    }

    // Setters
    public void setForecastUrl(String forecastUrl) {
        this.forecastUrl = forecastUrl;
    }

    public void setJsonResponse(String nwsJsonResponse) {
        this.nwsJsonResponse = nwsJsonResponse;
        System.out.println("Before: ");
        System.out.println(nwsJsonResponse);
        parseJsonResponse(nwsJsonResponse); // Automatically parse new JSON response
        System.out.println();
        System.out.println("Forecast URL: " + forecastUrl);
        System.out.println();
        System.out.println("After: ");
        System.out.println();
        System.out.println();

    }

    // Getters
    public String getForecastUrl() {
        return forecastUrl;
    }

    public String getJsonResponse() {
        return nwsJsonResponse;
    }
}
