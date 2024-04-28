import java.util.LinkedList;
import java.util.Scanner;

public class WeatherApp {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("Enter an option:");
            System.out.println("1. Enter an address to get the weather");
            System.out.println("2. Select an address from favorites");
            System.out.println("3. Exit");

            int option = scanner.nextInt();
            scanner.nextLine(); // consume the newline left by nextInt()

            switch (option) {
                case 1:
                    handleNewAddress();
                    break;
                case 2:
                    selectAndDisplayForecastFromFavorites();
                    break;
                case 3:
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid option. Please choose 1, 2, or 3.");
                    break;
            }
        }
    }

    private static void handleNewAddress() {
        System.out.println("Enter an address:");
        String address = scanner.nextLine();

        double[] coordinates = GeocodeService.forwardGeocode(address);
        if (coordinates != null && coordinates.length == 2 && coordinates[0] != 0.0 && coordinates[1] != 0.0) {
            displayForecast(address);
        } else {
            System.out.println("Failed to retrieve coordinates for the provided address.");
        }
    }

    private static void selectAndDisplayForecastFromFavorites() {
        LinkedList <
