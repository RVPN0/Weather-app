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
import java.util.ArrayList;
import java.util.List;

public class GeocodeService {
    private static final String BASE_URL = "https://geocode.xyz";
    private static List<AddressCoordinate> coordinatesCache = new ArrayList<>();

    /**
     * Stores address and its coordinates for caching.
     */
    private static class AddressCoordinate {
        String address;
        double latitude;
        double longitude;

        AddressCoordinate(String address, double latitude, double longitude) {
            this.address = address;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }

    /**
     * Retrieves geographic coordinates for a given address using the Geocode.xyz API, with caching.
     *
     * @param address The address to geocode.
     * @param apiKey  The API key for authenticating with the Geocode.xyz service.
     * @return An array of doubles where index 0 is latitude and index 1 is longitude.
     */
    public static double[] getCoordinates(String address, String apiKey) {
        // First check if the address has already been geocoded and cached.
        for (AddressCoordinate cachedCoordinate : coordinatesCache) {
            if (cachedCoordinate.address.equalsIgnoreCase(address)) {
                return new double[]{cachedCoordinate.latitude, cachedCoordinate.longitude};
            }
        }

        try {
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
            String apiUrl = BASE_URL + "?locate=" + encodedAddress + "&auth=" + apiKey;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl))
                    .header("Accept", "application/json")
                    .header("User-Agent", "Java 11 HttpClient Bot")
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                double[] coordinates = parseCoordinates(response.body());
                // Cache the new coordinates.
                coordinatesCache.add(new AddressCoordinate(address, coordinates[0], coordinates[1]));
                return coordinates;
            } else {
                System.out.println("Error: Failed to geocode address. HTTP Error Code: " + response.statusCode());
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
            return new double[]{0.0, 0.0}; // Default coordinates on error
        }
    }
}
