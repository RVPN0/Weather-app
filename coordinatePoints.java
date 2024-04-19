
/* 
   
   https://geocode.maps.co/  
   API KEY: 6605a52f94949045597836mxe984877
   Forward Geocode (Convert human-readable address to coordinates):
   https://geocode.maps.co/search?q=address&api_key=6605a52f94949045597836mxe984877
   
   Reverse Geocode (Convert coordinates to human-readable address):
   https://geocode.maps.co/reverse?lat=latitude&lon=longitude&api_key=6605a52f94949045597836mxe984877
   
   Replace {address}, {latitude} and {longitude} with the values to geocode.
   
   example: https://geocode.maps.co/search?q=5509+Lakeford+Ln+Bowie+MD&api_key=6605a52f94949045597836mxe984877
   
   https://dzone.com/articles/how-to-parse-json-data-from-a-rest-api-using-simpl
   */

//package Weather-app; 



import java.util.Scanner;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;


public class coordinatePoints {

    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);

        System.out.println("Enter an address: ");
        String address = scnr.next();
        String jsonResponse = getGeocodeData(address);
        double[] latLong = readLatLong(jsonResponse);
        System.out.println("Latitude: " + latLong[0] + ", Longitude: " + latLong[1]);
        
    }

    private static String getGeocodeData(String address) {
        String apiKey = "6605a52f94949045597836mxe984877";
        String apiUrl = "https://geocode.maps.co/search?q=" + address + "&api_key=" + apiKey;

           while (true) {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            } else {
                System.out.println("Error: " + responseCode);
            }
        } 
       
    }

    private static double[] readLatLong(String jsonResponse) {
        JsonReader jsonReader = Json.createReader(new StringReader(jsonResponse));
        JsonArray jsonArray = jsonReader.readArray();
        jsonReader.close();

        if (jsonArray.size() > 0) {
            JsonObject location = jsonArray.getJsonObject(0);
            double latitude = Double.parseDouble(location.getString("lat"));
            double longitude = Double.parseDouble(location.getString("lon"));
            return new double[] { latitude, longitude };
        }
        // Return a default location if no results were found
        return new double[] { 0.0, 0.0 };
    }
}
