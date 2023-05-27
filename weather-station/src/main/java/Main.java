import station.Station;
import station.StationFactory;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        args[0] = System.getenv("stationType");
        args[1] = System.getenv("longitudeOM");
        args[2] = System.getenv("latitudeOM");
        Station station = StationFactory.generateStation(args);
        station.SendMessages();
    }

}
