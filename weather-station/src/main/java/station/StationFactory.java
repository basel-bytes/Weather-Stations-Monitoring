package station;

import message_utils.message_builder.AdapterMessageBuilder;
import message_utils.message_builder.Builder;
import message_utils.message_builder.Director;
import message_utils.message_builder.MockMessageBuilder;
import station_utils.IDGenerator;

import java.io.IOException;
import java.util.Objects;

public class StationFactory {
    public static Station generateStation(String[] parameters) throws IOException {
        long stationID = IDGenerator.generateStationID();
        System.out.println("Creating Station with ID: " + stationID);
        Builder builder;
        long sleepingTime;
        if(Objects.equals(parameters[0], "Mock")) {
            builder = new MockMessageBuilder(stationID);
            sleepingTime = 1000;
        }
        else{
            builder = new AdapterMessageBuilder(stationID, Float.parseFloat(parameters[1]), Float.parseFloat(parameters[2]));
            sleepingTime = 60000 * 60;
        }
        Director director = new Director(builder);
        return Station.getInstance(director, builder, sleepingTime);
    }
}
