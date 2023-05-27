package message_utils.message;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Setter
@Getter
@ToString
public class Message {
    @SerializedName("station_id")
    private long stationID;
    @SerializedName("s_no")
    private long sNo;
    @SerializedName("battery_status")
    private BatteryStatus batteryStatus;
    @SerializedName("status_timestamp")
    private long statusTimestamp;
    Weather weather;
}
