import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class GeocodeService {
    private static final String BASE_URL = "https://geocode.maps.co/search";

    public static double[] getCoordinates(String address, String apiKey) {
        String apiUrl = BASE_URL + "?q=" + address.replaceAll(" ", "+") + "&api_key=" + apiKey;
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
            e.printStackTrace();
        }
        return new double[] {0.0, 0.0}; // Default coordinates on error
    }

    private static double[] parseCoordinates(String jsonResponse) {
        JsonObject jsonObject = Json.createReader(new StringReader(jsonResponse)).readObject();
        JsonObject location = jsonObject.getJsonObject("location");

        if (location != null && location.containsKey("lat") && location.containsKey("lon")) {
            double latitude = location.getJsonNumber("lat").doubleValue();
            double longitude = location.getJsonNumber("lon").doubleValue();
            return new double[] {latitude, longitude};
        }
        return new double[] {0.0, 0.0};
    }
}


