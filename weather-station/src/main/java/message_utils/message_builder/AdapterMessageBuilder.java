package message_utils.message_builder;

import station_utils.OMHttpClient;

import java.io.IOException;
import java.util.Map;

public class AdapterMessageBuilder extends Builder{
    private final String URI;
    private final OMHttpClient omHttpClient;
    Map<String, Long> extractedReadings;
    public AdapterMessageBuilder(long stationID, float longitude, float latitude){
        super(stationID);
        this.omHttpClient = new OMHttpClient();
        this.URI = this.omHttpClient.generateOMURI(longitude, latitude);
    }

    @Override
    protected void reset() throws IOException {
        super.reset();
        this.extractedReadings = this.omHttpClient.extractOMReadings(URI);
    }

    @Override
    protected void setStatusTimeStamp() {super.message.setStatusTimestamp(this.extractedReadings.get("timeStamp"));}
    @Override
    protected void setHumidity() {super.weather.setHumidity(this.extractedReadings.get("humidity").intValue());}

    @Override
    protected void setTemperature() {super.weather.setTemperature(this.extractedReadings.get("temperature").intValue());}

    @Override
    protected void setWindSpeed() {super.weather.setWindSpeed(this.extractedReadings.get("windSpeed").intValue());}
}
