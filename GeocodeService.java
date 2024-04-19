import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class GeocodeService extends APIConfig {
    private static final String API_KEY = "6605a52f94949045597836mxe984877";
    private static final String BASE_URL = "https://geocode.maps.co/search";

    public static double[] getCoordinates(String address) {
        String apiUrl = BASE_URL + "?q=" + address.replaceAll(" ", "+") + "&api_key=" + API_KEY;
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", ACCEPT_JSON);
            connection.setRequestProperty("User-Agent", USER_AGENT);

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String jsonResponse = reader.readLine();
                reader.close();
                return parseCoordinates(jsonResponse);
            } else {
                System.out.println("Error: Failed to geocode address. HTTP Error Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new double[] {0.0, 0.0}; // Default coordinates on error
    }

    private static double[] parseCoordinates(String jsonResponse) {
        JsonReader jsonReader = Json.createReader(new StringReader(jsonResponse));
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();

        if (jsonObject.containsKey("lat") && jsonObject.containsKey("lon")) {
            double latitude = Double.parseDouble(jsonObject.getString("lat"));
            double longitude = Double.parseDouble(jsonObject.getString("lon"));
            return new double[] {latitude, longitude};
        }
        return new double[] {0.0, 0.0};
    }
}

