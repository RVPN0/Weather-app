
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import javax.json.Json;
import javax.json.JsonObject;

public class WeatherServiceNWS extends APIConfig {

    private final HttpClient httpClient = HttpClient.newHttpClient();

    // Fetch the forecast for a given grid point
    public String fetchGridForecast(String office, int gridX, int gridY) throws IOException, InterruptedException {
        String url = String.format("%s/gridpoints/%s/%d,%d/forecast", NWS_BASE_URL, office, gridX, gridY);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", USER_AGENT)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    // Fetch active alerts for a specific area
    public String fetchActiveAlerts(String area) throws IOException, InterruptedException {
        String url = String.format("%s/alerts/active?area=%s", NWS_BASE_URL, area);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", USER_AGENT)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    // Fetch the forecast using an address
    public static String fetchForecastByAddress(String address) throws IOException, InterruptedException {
        double[] coordinates = CoordinateCache.getCoordinatesFromCache(address);
        if (coordinates != null) {
            String pointsUrl = String.format("%s/points/%f,%f", NWS_BASE_URL, coordinates[0], coordinates[1]);
            HttpRequest pointsRequest = HttpRequest.newBuilder()
                    .uri(URI.create(pointsUrl))
                    .header("User-Agent", USER_AGENT)
                    .build();

            HttpResponse<String> pointsResponse = httpClient.send(pointsRequest, HttpResponse.BodyHandlers.ofString());
            if (pointsResponse.statusCode() == 200) {
                String gridForecastUrl = extractForecastUrl(pointsResponse.body());
                HttpRequest forecastRequest = HttpRequest.newBuilder()
                        .uri(URI.create(gridForecastUrl))
                        .header("User-Agent", USER_AGENT)
                        .build();

                HttpResponse<String> forecastResponse = httpClient.send(forecastRequest, HttpResponse.BodyHandlers.ofString());
                return forecastResponse.body();
            } else {
                return "Error retrieving grid point data";
            }
        }
        return "No cached coordinates available for this address.";
    }

    // Extract the forecast URL from the points API response
    private String extractForecastUrl(String jsonResponse) {
        try {
            JsonObject jsonObject = Json.createReader(new StringReader(jsonResponse)).readObject();
            String forecastUrl = jsonObject.getJsonObject("properties").getString("forecast");
            return forecastUrl;
        } catch (Exception e) {
            System.out.println("Error extracting forecast URL: " + e.getMessage());
            return null;
        }
    }

    // Utilize ForecastData parsing
    public ForecastData getForecastData(String office, int gridX, int gridY) throws IOException, InterruptedException {
        String url = String.format("%s/gridpoints/%s/%d,%d/forecast", NWS_BASE_URL, office, gridX, gridY);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", USER_AGENT)
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            JsonObject jsonObject = Json.createReader(new StringReader(response.body())).readObject();
            return ForecastData.fromJson(jsonObject.getJsonObject("properties").getJsonArray("periods").getJsonObject(0));
        }
        return null;  // Handle error appropriately
    }
}
