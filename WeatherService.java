import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.LinkedList;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.StringReader;

public class WeatherService extends APIConfig {
    private LinkedList<ForecastData> forecasts = new LinkedList<>();

    public void fetchWeather(double latitude, double longitude) {
        try {
            String urlStr = String.format("https://api.weather.gov/gridpoints/MLB/%d,%d/forecast", (int) latitude, (int) longitude);
            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", ACCEPT_JSON);
            conn.setRequestProperty("User-Agent", USER_AGENT);

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String jsonResponse = br.readLine();
                br.close();

                updateForecasts(jsonResponse);
                conn.disconnect();
            } else {
                throw new RuntimeException("Failed: HTTP error code : " + conn.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateForecasts(String jsonResponse) {
        JsonReader jsonReader = Json.createReader(new StringReader(jsonResponse));
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();

        JsonObject properties = jsonObject.getJsonObject("properties");
        properties.getJsonArray("periods").forEach(item -> {
            JsonObject period = (JsonObject) item;
            ForecastData forecast = new ForecastData(
                period.getString("name"),
                period.getString("startTime"),
                period.getString("endTime"),
                period.getString("temperature") + period.getString("temperatureUnit"),
                period.getString("detailedForecast")
            );
            forecasts.add(forecast);
        });
    }

    public LinkedList<ForecastData> getForecasts() {
        return forecasts;
    }
}
