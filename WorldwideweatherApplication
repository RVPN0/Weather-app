package com.dklm.worldwideweather;

import java.util.LinkedList;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WorldwideweatherApplication {

    private static final Scanner scnr = new Scanner(System.in);

    public static void main(String[] args) {
        SpringApplication.run(WorldwideweatherApplication.class, args);
        while (true) {
            System.out.println("Enter an option:");
            System.out.println("1. Enter an address to get the weather");
            System.out.println("2. Select an address from favorites");
            System.out.println("3. Exit");

            int option = scnr.nextInt();
            scnr.nextLine(); // consume the newline left by nextInt()

            switch (option) {
                case 1:
                    handleNewAddress();
                    break;
                case 2:
                    selectAndDisplayForecastFromFavorites();
                    break;
                case 3:
                    scnr.close();
                    return;
                default:
                    System.out.println("Invalid option. Please choose 1, 2, or 3.");
                    break;
            }
        }
    }

    private static void handleNewAddress() {
        System.out.println("Enter an address:");
        String address = scnr.nextLine();

        double[] coordinates = GeocodeService.forwardGeocode(address);
        if (coordinates != null && coordinates.length == 2 && coordinates[0] != 0.0 && coordinates[1] != 0.0) {
            displayForecast(address);
        } else {
            System.out.println("Failed to retrieve coordinates for the provided address.");
        }
    }

    public static void selectAndDisplayForecastFromFavorites() {
        LinkedList<CoordinateCache.CoordinateEntry> favorites = CoordinateCache.getAllFavorites();
        if (favorites.isEmpty()) {
            System.out.println("No favorite addresses stored.");
        } else {
            System.out.println("Select an address from your favorites:");
            for (int i = 0; i < favorites.size(); i++) {
                CoordinateCache.CoordinateEntry entry = favorites.get(i);
                System.out.printf("%d. %s\n", i + 1, entry.address);
            }

            int choice = scnr.nextInt() - 1;
            scnr.nextLine(); // consume the newline left by nextInt()

            if (choice >= 0 && choice < favorites.size()) {
                String selectedAddress = favorites.get(choice).address;
                displayForecast(selectedAddress);
            } else {
                System.out.println("Invalid selection.");
            }
        }
    }

    private static void displayForecast(String address) {
        try {
            String forecast = WeatherServiceNWS.fetchForecastByAddress(address);
            if (forecast != null && !forecast.isEmpty()) {
                System.out.println("Weather forecast for " + address + ":\n" + forecast);
            } else {
                System.out.println("Failed to retrieve the weather forecast.");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving weather data: " + e.getMessage());
        }
    }
}
