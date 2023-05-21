package station_utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class OMHttpClient {
    private final Gson gson = new Gson();
    public String generateOMURI(float longitude, float latitude){
        Properties props = new Properties();
        try (InputStream is = OMHttpClient.class.getClassLoader().getResourceAsStream("application.properties")) {
            props.load(is);
            return props.getProperty("Open-Meteo-URI") + "latitude=" + latitude + "&longitude=" + longitude +
                    "&hourly=" + props.getProperty("hourly") + "&temperature_unit=" + props.getProperty("temperature_unit") +
                    "&timeformat=" + props.getProperty("timeformat") + "&forecast_days=" + props.getProperty("forecast_days");
        } catch (IOException e) {
            return "";
        }
    }

    public Map<String, Long> extractOMReadings(String URI){
        Map<String, Long> extractedReadings = new HashMap<>();
        try {
            HttpClient httpclient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(URI);
            String response = httpclient.execute(httpGet, r -> EntityUtils.toString(r.getEntity()));
            JsonObject hourlyJsonResponse = gson.fromJson(response, JsonObject.class).getAsJsonObject("hourly");

            long timeStamp = hourlyJsonResponse.getAsJsonArray("time").get(23).getAsLong();
            int temperature = hourlyJsonResponse.getAsJsonArray("temperature_2m").get(23).getAsInt();
            int humdity = hourlyJsonResponse.getAsJsonArray("relativehumidity_2m").get(23).getAsInt();
            int windSpeed = hourlyJsonResponse.getAsJsonArray("windspeed_10m").get(23).getAsInt();

            extractedReadings.put("timeStamp", timeStamp);
            extractedReadings.put("temperature", (long) temperature);
            extractedReadings.put("humidity", (long) humdity);
            extractedReadings.put("windSpeed", (long) windSpeed);
        }
        catch (Exception e){
            extractedReadings.put("timeStamp", 0L);
            extractedReadings.put("temperature", 0L);
            extractedReadings.put("humidity", 0L);
            extractedReadings.put("windSpeed", 0L);
        }
        return extractedReadings;
    }
}
