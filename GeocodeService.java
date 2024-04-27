import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

public class GeocodeService {
    // Constants for the base URL of the Geocode API and HTTP headers
    private static final String BASE_URL = "https://geocode.xyz";

    
    public static double[] getCoordinates(String address, String apiKey) {
        try {
            // URL-encode the address to ensure it is safe for transmission over the Internet.
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
            // Construct the full API URL with the encoded address and the API key.
            String apiUrl = BASE_URL + "?locate=" + encodedAddress + "&auth=" + apiKey;

            // Create an HttpClient object for sending requests.
            HttpClient client = HttpClient.newHttpClient();
            // Build the HTTP request with the appropriate headers for JSON and browser-like user-agent.
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Accept", "application/json")
                    .header("User-Agent", "Java 11 HttpClient Bot")
                    .build();

            // Send the HTTP request and get the response as a String.
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check if the response status code is HTTP OK (200); if so, parse the coordinates.
            if (response.statusCode() == 200) {
                return parseCoordinates(response.body());
            } else {
                // Log an error message if the HTTP status code is not 200.
                System.out.println("Error: Failed to geocode address. HTTP Error Code: " + response.statusCode());
            }
        } catch (Exception e) {
            // Log any exceptions that occur during the request or parsing.
            System.out.println("Error: " + e.getMessage());
        }
        // Return default coordinates if an error occurs.
        return new double[]{0.0, 0.0};
    }

    
    private static double[] parseCoordinates(String jsonResponse) {
        try (JsonReader reader = Json.createReader(new StringReader(jsonResponse))) {
            // Convert the JSON string into a JsonObject.
            JsonObject jsonObject = reader.readObject();
            // Extract latitude and longitude from the JSON object.
            double latitude = Double.parseDouble(jsonObject.getString("latt"));
            double longitude = Double.parseDouble(jsonObject.getString("longt"));
            return new double[]{latitude, longitude};
        } catch (Exception e) {
            // Log any exceptions that occur during JSON parsing.
            System.out.println("Error parsing JSON response: " + e.getMessage());
            return new double[]{0.0, 0.0}; // Return default coordinates if parsing fails.
        }
    }
}
