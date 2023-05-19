package message_utils.message;

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
    private int windSpeed;
}
