package Weather-app;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class callNwsApi {
    public static void main(String[] args) {
        try {
            // Define the URL of the NWS API endpoint you want to connect to
            URL url = new URL("https://api.weather.gov/gridpoints/MLB/25,69/forecast");
            
            // Open a connection to the URL
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            // Set the request properties. NWS API requires setting a User-Agent
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("User-Agent", "My Java Application");

            // Check if the response code is HTTP OK
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed: HTTP error code: " + conn.getResponseCode());
            }

            // Read the response
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            StringBuilder jsonResponse = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                jsonResponse.append(output);
            }

            // Parse the JSON response
            JsonReader jsonReader = Json.createReader(new StringReader(jsonResponse.toString()));
            JsonObject jsonObject = jsonReader.readObject();
            jsonReader.close();

            // Extract the detailed forecast from the first period
            String detailedForecast = jsonObject.getJsonObject("properties")
                                                .getJsonArray("periods")
                                                .getJsonObject(0)
                                                .getString("detailedForecast");
            
            // Print the detailed forecast
            System.out.println("Detailed Forecast: " + detailedForecast);
            
            // Close the connection
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
