package com.dklm.worldwideweather;

import java.io.IOException;
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
            scnr.nextLine();  // consume the newline left by nextInt()

            switch (option) {
                case 1:
                    handleNewAddress();
                    break;
                case 2:
                    selectAndDisplayForecastFromFavorites();
                    break;
                case 3:
                    scnr.close();
                    System.exit(0);  // Properly exit the application
                default:
                    System.out.println("Invalid option. Please choose 1, 2, or 3.");
                    break;
            }
        }
    }

    private static void handleNewAddress() {
        System.out.println("Enter an address:");
        String address = scnr.nextLine();
        String[] coordinates = GeocodeService.forwardGeocode(address);
        if (coordinates == null || coordinates.length < 2) {
            System.out.println("Failed to retrieve coordinates for the provided address.");
            return;
        }

        double latitude = formatCoordinate(Double.parseDouble(coordinates[0]));
        double longitude = formatCoordinate(Double.parseDouble(coordinates[1]));
        try {
            String[] detailedForecast = WeatherServiceNWS.forwardNWS(latitude, longitude);
            if (detailedForecast != null && detailedForecast.length > 1) {
                System.out.println("Weather forecast for " + address + ":\n" + detailedForecast[1]);
                promptSaveFavorite(address, latitude, longitude);
            } else {
                System.out.println("Failed to retrieve detailed forecast.");
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Error retrieving weather data: " + e.getMessage());
        }
    }

    private static void promptSaveFavorite(String address, double latitude, double longitude) {
        System.out.println("Do you want to save this address to your favorites? (yes/no)");
        String response = scnr.nextLine().trim().toLowerCase();
        if ("yes".equals(response)) {
            CoordinateCache.addCoordinatesToCache(address, latitude, longitude);
            System.out.println("Address saved to favorites.");
        }
    }

    private static void selectAndDisplayForecastFromFavorites() {
        LinkedList<CoordinateCache.CoordinateEntry> favorites = CoordinateCache.getAllFavorites();
        if (favorites.isEmpty()) {
            System.out.println("No favorite addresses stored.");
            return;
        }

        System.out.println("Select an address from your favorites:");
        for (int i = 0; i < favorites.size(); i++) {
            System.out.printf("%d. %s (%f, %f)\n", i + 1, favorites.get(i).address, favorites.get(i).coordinates[0], favorites.get(i).coordinates[1]);
        }

        int choice = scnr.nextInt() - 1;
        scnr.nextLine();  // consume the newline left by nextInt()

        if (choice >= 0 && choice < favorites.size()) {
            CoordinateCache.CoordinateEntry selectedEntry = favorites.get(choice);
            try {
                String[] forecast = WeatherServiceNWS.forwardNWS(selectedEntry.coordinates[0], selectedEntry.coordinates[1]);
                System.out.println("Weather forecast for " + selectedEntry.address + ":\n" + forecast[1]);
            } catch (IOException | InterruptedException e) {
                System.out.println("Error retrieving weather data: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid selection.");
        }
    }

    private static double formatCoordinate(double coordinate) {
        return Math.round(coordinate * 10000.0) / 10000.0;
    }

}
