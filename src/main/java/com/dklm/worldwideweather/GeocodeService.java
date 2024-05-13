package com.dklm.worldwideweather;

public class GeocodeService extends APIConfig {

    public static CoordinateData forwardGeocode(String address) {

        String jsonResponse = callGeocodeAPI(address, GEOCODE_API_KEY);
        CoordinateData coordinateData = new CoordinateData();
        if (jsonResponse != null) {
            boolean parseSuccess = coordinateData.parseJsonResponse(jsonResponse);
            if (parseSuccess) {
                return coordinateData; // Return the CoordinateData object directly
            } else {
                System.out.println("Error: Failed to retrieve coordinates.");
                return new CoordinateData("0.0", "0.0"); // Return default coordinates in case of parsing failure
            }
        } else {
            System.out.println("Error: Failed to retrieve geocode data.");
            return new CoordinateData("0.0", "0.0"); // Return default coordinates in case of API failure
        }
    }
}
