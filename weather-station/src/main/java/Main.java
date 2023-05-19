import station.IDGenerator;
import station.MockStation;
import station.Station;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world");
        Station station = new MockStation();
        station.SendMessage();
    }
}
