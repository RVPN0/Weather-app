package com.dklm.worldwideweather;

public class GeocodeService extends APIConfig {

    public static String[] forwardGeocode(String address) {
        useGeoApiKey();  // Log API key usage

        String jsonResponse = callGeocodeAPI(address, GEOCODE_API_KEY);
        System.out.println("JSON Response: " + jsonResponse);
        CoordinateData coordinateData = new CoordinateData();
        if (jsonResponse != null) {
            coordinateData.setJsonResponse(jsonResponse);
            return new String[]{coordinateData.getLatitude(), coordinateData.getLongitude()};
        } else {
            System.out.println("error");
            return new String[]{"0.0", "0.0"}; // Default return value in case of failure
        }
    }

}
