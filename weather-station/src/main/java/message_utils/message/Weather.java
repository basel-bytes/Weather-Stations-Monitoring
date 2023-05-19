package message_utils.message;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class Weather {
    private int humidity;
    private int temperature;
    @SerializedName("wind_speed")
    private int windSpeed;
}
