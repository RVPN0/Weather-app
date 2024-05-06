
import javax.json.JsonObject;

public class ForecastData {

    private String name;
    private String startTime;
    private String endTime;
    private String temperature;
    private String detailedForecast;

    public ForecastData(String name, String startTime, String endTime, String temperature, String detailedForecast) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.temperature = temperature;
        this.detailedForecast = detailedForecast;
    }

    // Static method to parse JSON into ForecastData
    public static ForecastData fromJson(JsonObject jsonObject) {
        String name = jsonObject.getString("name", "No Name");
        String startTime = jsonObject.getString("startTime", "No Start Time");
        String endTime = jsonObject.getString("endTime", "No End Time");
        String temperature = jsonObject.getString("temperature", "No Temperature") + jsonObject.getString("temperatureUnit", "");
        String detailedForecast = jsonObject.getString("detailedForecast", "No Detailed Forecast");

        return new ForecastData(name, startTime, endTime, temperature, detailedForecast);
    }

    @Override
    public String toString() {
        return String.format("%s: %s to %s - %s - %s", name, startTime, endTime, temperature, detailedForecast);
    }
}
