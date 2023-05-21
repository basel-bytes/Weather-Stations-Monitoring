import station.Station;
import station.StationFactory;
import station_utils.OMHttpClient;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Station station = StationFactory.generateStation(new String[]{"OM", "22", "34"});
        station.SendMessages();
    }
}
