import station.Station;
import station.StationFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Station station = StationFactory.generateStation(args);
        station.SendMessages();
    }

}
