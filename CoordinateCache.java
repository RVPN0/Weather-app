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

    // LinkedList to store coordinate entries
    private static LinkedList {
         <CoordinateEntry> coordinateCache = new LinkedList<>();
    }
    // Method to add coordinates to the cache
    public static void addCoordinatesToCache(String address, double latitude, double longitude) {
        for (CoordinateEntry entry : coordinateCache) {
            if (entry.address.equalsIgnoreCase(address)) {
                // Update existing entry if address matches
                entry.coordinates[0] = latitude;
                entry.coordinates[1] = longitude;
                return;
            }
        }
        // If not found, add a new entry
        coordinateCache.add(new CoordinateEntry(address, latitude, longitude));
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
}
