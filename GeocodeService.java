import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

public class GeocodeService {
    private static final String BASE_URL = "https://geocode.xyz";

    public static double[] getCoordinates(String address, String apiKey) {
        String apiUrl = BASE_URL + "?locate=" + address.replaceAll(" ", "+") + "&auth=" + apiKey;
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("User-Agent", "Mozilla/5.0");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder jsonResponse = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    jsonResponse.append(line);
                }
                reader.close();
                return parseCoordinates(jsonResponse.toString());
            } else {
                System.out.println("Error: Failed to geocode address. HTTP Error Code: " + responseCode);
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return new double[]{0.0, 0.0}; // Default coordinates on error
    }

    private static double[] parseCoordinates(String jsonResponse) {
        try (JsonReader reader = Json.createReader(new StringReader(jsonResponse))) {
            JsonObject jsonObject = reader.readObject();
            double latitude = Double.parseDouble(jsonObject.getString("latt"));
            double longitude = Double.parseDouble(jsonObject.getString("longt"));
            return new double[]{latitude, longitude};
        } catch (Exception e) {
            System.out.println("Error parsing JSON response: " + e.getMessage());
        }
        return new double[]{0.0, 0.0}; // Default coordinates on error
    }
}



