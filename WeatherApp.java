import java.util.Scanner;

public class WeatherApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter an address:");
        String address = scanner.nextLine();
        scanner.close();

        GeocodeService geocodeService = new GeocodeService();
        double[] coordinates = geocodeService.getCoordinates(address);

        if (coordinates[0] != 0.0 && coordinates[1] != 0.0) {
            WeatherService weatherService = new WeatherService();
            weatherService.fetchWeather(coordinates[0], coordinates[1]);
            weatherService.getForecasts().forEach(System.out::println);
        } else {
            System.out.println("Failed to retrieve coordinates for the provided address.");
        }
    }
}