package station_utils;

import com.github.f4b6a3.ulid.Ulid;
import com.github.f4b6a3.ulid.UlidCreator;

public class IDGenerator {
    public static long generateStationID(){
        Ulid ulid = UlidCreator.getUlid();
        return Math.abs(ulid.getMostSignificantBits() | ulid.getLeastSignificantBits());
    }
}
