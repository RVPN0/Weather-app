import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

public class WeatherServiceNWS {
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private static final String BASE_URL = "https://api.weather.gov";

    // Fetch the forecast for a given grid point
    public String fetchGridForecast(String office, int gridX, int gridY) throws IOException, InterruptedException {
        String url = String.format("%s/gridpoints/%s/%d,%d/forecast", BASE_URL, office, gridX, gridY);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("User-Agent", "myweatherapp.com, contact@myweatherapp.com") // User-Agent as required
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    // Fetch active alerts for a specific area
    public String fetchActiveAlerts(String area) throws IOException, InterruptedException {
        String url = String.format("%s/alerts/active?area=%s", BASE_URL, area);
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("User-Agent", "myweatherapp.com, contact@myweatherapp.com") // Include User-Agent as required
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    // Example usage
    public static void main(String[] args) {
        WeatherServiceNWS service = new WeatherServiceNWS();
        try {
            String forecast = service.fetchGridForecast("TOP", 31, 80);
            System.out.println("Forecast: " + forecast);
            String alerts = service.fetchActiveAlerts("KS");
            System.out.println("Alerts: " + alerts);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
