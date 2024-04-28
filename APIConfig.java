public class APIConfig {
    // API key for Geocode API - securely store and manage this key
    protected static final String GEOCODE_API_KEY = "6605a52f94949045597836mxe984877";

    // Base URL for the Geocode API
    protected static final String GEOCODE_BASE_URL = "https://geocode.maps.co";

    // You could also add other configuration details here, such as timeouts or additional headers
    protected static final int CONNECTION_TIMEOUT = 10000; // Timeout in milliseconds
    protected static final int READ_TIMEOUT = 10000; // Read timeout in milliseconds

    // Optionally, include headers that are common across various API calls
    protected static final String USER_AGENT = "MyWeatherApp (contact@myweatherapp.com)";

    // Method to log or handle API key usage - for auditing or tracking key usage if needed
    protected void useApiKey() {
        // Log API key usage or perform any necessary actions before using the API key
        System.out.println("Using API Key: " + GEOCODE_API_KEY);
    }
}
