package message_utils.message_builder;

import message_utils.message.BatteryStatus;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

public class MockMessageBuilder extends Builder{
    private long lastMessageID;
    private final SecureRandom tempRandomizer;
    private final SecureRandom humidityRandomizer;
    private final SecureRandom windSpeedRandomizer;
    private final EnumeratedDistribution<BatteryStatus> batteryStateRandomizer;

    public MockMessageBuilder(long stationID, long lastMessageID) {
        super(stationID);
        this.lastMessageID = lastMessageID;

        this.tempRandomizer = new SecureRandom();
        this.windSpeedRandomizer = new SecureRandom();
        this.humidityRandomizer = new SecureRandom();

        List<Pair<BatteryStatus, Double>> batteryStates = new ArrayList<>();
        batteryStates.add(new Pair<>(BatteryStatus.Low, 0.3));
        batteryStates.add(new Pair<>(BatteryStatus.Medium, 0.4));
        batteryStates.add(new Pair<>(BatteryStatus.High, 0.3));
        this.batteryStateRandomizer = new EnumeratedDistribution<>(batteryStates);
    }


    @Override
    protected void setID() {super.message.setStationID(super.stationID);}

    @Override
    protected void setSerialNumber() {super.message.setSNo(++lastMessageID);}

    @Override
    protected void setBatteryStatus() {
        super.message.setBatteryStatus(batteryStateRandomizer.sample());
    }

    @Override
    protected void setStatusTimeStamp() {
        super.message.setStatusTimestamp(System.currentTimeMillis());
    }

    @Override
    protected void setHumidity() {
        super.weather.setHumidity(humidityRandomizer.nextInt(100));
    }

    @Override
    protected void setTemperature() {
        super.weather.setTemperature(tempRandomizer.nextInt(212 - 32) + 32);
    }

    @Override
    protected void setWindSpeed() {
        super.weather.setWindSpeed(windSpeedRandomizer.nextInt(408));
    }
}
