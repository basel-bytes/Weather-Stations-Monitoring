import java.sql.Timestamp;
import java.time.LocalDateTime;

public class TimeStamps {
    //Number of millis since 1970 till 2023
    private static final Long baseMillis = Timestamp.valueOf(LocalDateTime.of(2023, 5, 1, 0, 0, 0)).getTime();
    public static Long getTimeStamp(){
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return timestamp.getTime() - baseMillis;
    }
}
