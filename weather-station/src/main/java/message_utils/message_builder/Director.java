package message_utils.message_builder;

import lombok.AllArgsConstructor;

import java.io.IOException;

@AllArgsConstructor
public class Director {
    private Builder builder;

    public void changeBuilder(Builder newBuilder){this.builder = newBuilder;}
    public void BuildMessage() throws IOException {
        this.builder.reset();
        this.builder.setID();
        this.builder.setSerialNumber();
        this.builder.setBatteryStatus();
        this.builder.setHumidity();
        this.builder.setTemperature();
        this.builder.setWindSpeed();
        this.builder.setStatusTimeStamp();
    }
}
