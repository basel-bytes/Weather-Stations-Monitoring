package message_utils.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class Message {
    private long stationID;
    private long sNo;
    private BatteryStatus batteryStatus;
    private long statusTimestamp;
    Weather weather;
}
