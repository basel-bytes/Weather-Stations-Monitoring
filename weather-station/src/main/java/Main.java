import station.Station;
import station.StationFactory;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Station station = StationFactory.generateStation(args);
        station.SendMessages();
    }
}
