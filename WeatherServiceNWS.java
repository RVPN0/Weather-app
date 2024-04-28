import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

public class WeatherServiceNWS extends CoordinateCache {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private static final String BASE_URL = "https://api.weather.gov";

    // Fetch the forecast for a given grid point
    public String fetchGridForecast(String office, int gridX, int gridY) throws IOException, InterruptedException {
        String url = String.format("%s/gridpoints/%s/%d,%d/forecast", BASE_URL, office, gridX, gridY);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("User-Agent", "myweatherapp.com, contact@myweatherapp.com")
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    // Fetch active alerts for a specific area
    public String fetchActiveAlerts(String area) throws IOException, InterruptedException {
        String url = String.format("%s/alerts/active?area=%s", BASE_URL, area);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("User-Agent", "myweatherapp.com, contact@myweatherapp.com")
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    // Fetch the forecast using an address
    public String fetchForecastByAddress(String address) throws IOException, InterruptedException {
        double[] coordinates = getCoordinatesFromCache(address);
        if (coordinates != null) {
            // Fetch the points data to get the office/gridX/gridY
            String pointsUrl = String.format("%s/points/%f,%f", BASE_URL, coordinates[0], coordinates[1]);
            HttpRequest pointsRequest = HttpRequest.newBuilder()
                .uri(URI.create(pointsUrl))
                .header("User-Agent", "myweatherapp.com, contact@myweatherapp.com")
                .build();

            HttpResponse<String> pointsResponse = httpClient.send(pointsRequest, HttpResponse.BodyHandlers.ofString());
            if (pointsResponse.statusCode() == 200) {
                String gridForecastUrl = extractForecastUrl(pointsResponse.body());
                HttpRequest forecastRequest = HttpRequest.newBuilder()
                    .uri(URI.create(gridForecastUrl))
                    .header("User-Agent", "myweatherapp.com, contact@myweatherapp.com")
                    .build();

                HttpResponse<String> forecastResponse = httpClient.send(forecastRequest, HttpResponse.BodyHandlers.ofString());
                return forecastResponse.body();
            } else {
                return "Error retrieving grid point data";
            }
        }
        return "No cached coordinates available for this address.";
    }
    
    public ForecastData getForecastData(String office, int gridX, int gridY) throws IOException, InterruptedException {
        String url = String.format("%s/gridpoints/%s/%d,%d/forecast", BASE_URL, office, gridX, gridY);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("User-Agent", "myweatherapp.com, contact@myweatherapp.com")
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() == 200) {
            JsonObject jsonObject = Json.createReader(new StringReader(response.body())).readObject();
            return ForecastData.fromJson(jsonObject.getJsonObject("properties").getJsonArray("periods").getJsonObject(0));
        }
        return null;  // Or handle error appropriately
     }

    // Extract the forecast URL from the points API response
    private String extractForecastUrl(String jsonResponse) {
        try {
            // This method needs to parse the JSON to extract the forecast URL
            // Placeholder for JSON parsing logic
            return "https://api.weather.gov/gridpoints/TOP/31,80/forecast"; // Example URL
        } catch (Exception e) {
            System.out.println("Error extracting forecast URL: " + e.getMessage());
            return null;
        }
    }
