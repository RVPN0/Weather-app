import java.util.LinkedList;

public class CoordinateCache {
    // Inner class to store coordinate data along with the associated address
    private static class CoordinateEntry {
        String address;
        double[] coordinates; // Store coordinates as an array [latitude, longitude]

        CoordinateEntry(String address, double latitude, double longitude) {
            this.address = address;
            this.coordinates = new double[]{latitude, longitude};
        }
    }

    // LinkedList to store coordinate entries, specifying the generic type
    private static LinkedList <CoordinateEntry> coordinateCache = new LinkedList<>();
    private static final int MAX_CACHE_SIZE = 10;  // Maximum number of entries in the cache

    // Method to add coordinates to the cache
    public static void addCoordinatesToCache(String address, double latitude, double longitude) {
        // First, check if the address already exists in the cache
        for (CoordinateEntry entry : coordinateCache) {
            if (entry.address.equalsIgnoreCase(address)) {
                // Update existing entry if address matches
                entry.coordinates[0] = latitude;
                entry.coordinates[1] = longitude;
                return;
            }
        }

        // Check if the cache has reached its maximum size
        if (coordinateCache.size() >= MAX_CACHE_SIZE) {
            // Remove the oldest entry to make space for the new one
            coordinateCache.removeFirst();
        }

        // Add the new entry
        coordinateCache.addLast(new CoordinateEntry(address, latitude, longitude));
    }

    // Method to retrieve coordinates from the cache
    public static double[] getCoordinatesFromCache(String address) {
        for (CoordinateEntry entry : coordinateCache) {
            if (entry.address.equalsIgnoreCase(address)) {
                return entry.coordinates;
            }
        }
        return null; // Return null if no entry is found
    }

    // Method to get all entries in the cache for display as favorites
    public static LinkedList <CoordinateEntry> getAllFavorites() {
        return new LinkedList<>(coordinateCache);
    }
}

        
