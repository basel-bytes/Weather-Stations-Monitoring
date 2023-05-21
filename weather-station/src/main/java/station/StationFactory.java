package station;

import message_utils.message_builder.AdapterMessageBuilder;
import message_utils.message_builder.Builder;
import message_utils.message_builder.Director;
import message_utils.message_builder.MockMessageBuilder;
import station_utils.IDGenerator;

import java.util.Objects;

public class StationFactory {
    public static Station generateStation(String[] parameters){
        long stationID = IDGenerator.generateStationID();
        Builder builder;
        long sleepingTime;
        if(Objects.equals(parameters[0], "Mock")) {
            builder = new MockMessageBuilder(stationID);
            sleepingTime = 1000;
        }
        else{
            System.out.println("okk");
            builder = new AdapterMessageBuilder(stationID, Float.parseFloat(parameters[1]), Float.parseFloat(parameters[2]));
            sleepingTime = 60000;
        }

        Director director = new Director(builder);
        return Station.getInstance(director,builder, sleepingTime);
    }
}
