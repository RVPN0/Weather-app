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

    @Override
    public String toString() {
        return String.format("%s: %s to %s - %s - %s", name, startTime, endTime, temperature, detailedForecast);
    }
}
