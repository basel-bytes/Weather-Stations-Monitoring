package message_utils.message_builder;

import message_utils.message.BatteryStatus;
import message_utils.message.Message;
import message_utils.message.Weather;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class Builder {
    protected long stationID;
    protected long lastMessageID;
    protected Message message;
    protected Weather weather;
    protected final EnumeratedDistribution<BatteryStatus> batteryStateRandomizer;

    public Builder(long stationID) {
        this.stationID = stationID;
        this.lastMessageID = 0;

        List<Pair<BatteryStatus, Double>> batteryStates = new ArrayList<>();
        batteryStates.add(new Pair<>(BatteryStatus.Low, 0.3));
        batteryStates.add(new Pair<>(BatteryStatus.Medium, 0.4));
        batteryStates.add(new Pair<>(BatteryStatus.High, 0.3));
        this.batteryStateRandomizer = new EnumeratedDistribution<>(batteryStates);
    }

    protected void reset() throws IOException {
        this.message = new Message();
        this.weather = new Weather();
    }
    protected void setID(){this.message.setStationID(this.stationID);}
    protected void setSerialNumber(){this.message.setSNo(++lastMessageID);}
    protected void setBatteryStatus() {this.message.setBatteryStatus(this.batteryStateRandomizer.sample());}
    protected abstract void setStatusTimeStamp();
    protected abstract void setHumidity();
    protected abstract void setTemperature();
    protected abstract void setWindSpeed();

    public Message getMessage() {
        this.message.setWeather(weather);
        return message;
    }
}
