import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

public class GeocodeService extends CoordinateCache {
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final String API_KEY = "6605a52f94949045597836mxe984877";  // Securely manage and store API keys

    public static double[] forwardGeocode(String address) {
        // First, check the cache
        double[] cachedCoordinates = getCoordinatesFromCache(address);
        if (cachedCoordinates != null) {
            return cachedCoordinates;
        }

        try {
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
            String url = String.format("https://geocode.maps.co/search?q=%s&api_key=%s", encodedAddress, API_KEY);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                double[] coordinates = parseCoordinates(response.body());
                addCoordinatesToCache(address, coordinates[0], coordinates[1]); // Add to cache
                return coordinates;
            }
        } catch (Exception e) {
            System.out.println("Error during forward geocoding: " + e.getMessage());
        }
        return new double[]{0.0, 0.0};  // Default return value in case of failure
    }

    public static String reverseGeocode(double latitude, double longitude) {
        // Reverse geocoding cache integration could be here if applicable
        try {
            String url = String.format("https://geocode.maps.co/reverse?lat=%f&lon=%f&api_key=%s", latitude, longitude, API_KEY);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return parseAddress(response.body());
            }
        } catch (Exception e) {
            System.out.println("Error during reverse geocoding: " + e.getMessage());
        }
        return "Unknown Location";  // Default return value in case of failure
    }

    private static double[] parseCoordinates(String jsonResponse) {
        try (JsonReader reader = Json.createReader(new StringReader(jsonResponse))) {
            JsonObject jsonObject = reader.readObject().getJsonObject("result");
            double latitude = Double.parseDouble(jsonObject.getString("latitude"));
            double longitude = Double.parseDouble(jsonObject.getString("longitude"));
            return new double[]{latitude, longitude};
        } catch (Exception e) {
            System.out.println("Error parsing geocoding response: " + e.getMessage());
        }
        return new double[]{0.0, 0.0};
    }

    private static String parseAddress(String jsonResponse) {
        try (JsonReader reader = Json.createReader(new StringReader(jsonResponse))) {
            JsonObject jsonObject = reader.readObject().getJsonObject("address");
            return jsonObject.getString("formattedAddress");
        } catch (Exception e) {
            System.out.println("Error parsing reverse geocoding response: " + e.getMessage());
        }
        return "Unknown Location";
    }
}
