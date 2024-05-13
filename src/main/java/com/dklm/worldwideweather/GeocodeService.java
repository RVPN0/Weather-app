package com.dklm.worldwideweather;

public class GeocodeService extends APIConfig {                                             // Class to handle Geocode API calls

    public static CoordinateData getGeocodeCoordinates(String address) {                    // Method to get coordinates from the Geocode API

        String jsonResponse = callGeocodeAPI(address, GEOCODE_API_KEY);             // Call the Geocode API with the given address and API key
        CoordinateData coordinateData = new CoordinateData();
        if (jsonResponse != null) {
            boolean parseSuccess = coordinateData.parseGeocodeResponse(jsonResponse);       // If the API call was successful, parse the JSON response
            if (parseSuccess) {
                return coordinateData;                                                      // If parsing was successful, return the coordinates
            } else {
                System.out.println("Error: Failed to retrieve coordinates.");
                return new CoordinateData("0.0", "0.0");               //  In case of API failure, return default coordinates and error message
            }
        } else {
            System.out.println("Error: Failed to retrieve geocode data.");               // Log an error message if the API call failed and return default coordinates
            return new CoordinateData("0.0", "0.0");
        }
    }
}
