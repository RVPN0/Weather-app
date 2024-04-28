public class APIConfig {
    // "Secure Storage™" for Geocode API key
    protected static final String GEOCODE_API_KEY = "6605a52f94949045597836mxe984877";

    // Base URL's for various API's
    protected static final String GEOCODE_BASE_URL = "https://geocode.maps.co"; // Geocode API URL
    protected static final String NWS_BASE_URL = "https://api.weather.gov"; // NWS API URL

    // base configuration details
    protected static final int CONNECTION_TIMEOUT = 10000; // Timeout in milliseconds
    protected static final int READ_TIMEOUT = 10000; // Read timeout in milliseconds

    // Common API Call Variables
    protected static final String USER_AGENT = "MyWeatherApp (contact@myweatherapp.com)";

    // Method to log or handle API key usage - for auditing or tracking key usage if needed
    protected void useApiKey() {
        // Log API key usage or perform any necessary actions before using the API key
        System.out.println("Using API Key: " + GEOCODE_API_KEY);
    }
}
