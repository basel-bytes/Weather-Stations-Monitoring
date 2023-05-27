package message_utils.message_builder;

import java.security.SecureRandom;

public class MockMessageBuilder extends Builder{
    private final SecureRandom tempRandomizer;
    private final SecureRandom humidityRandomizer;
    private final SecureRandom windSpeedRandomizer;

    public MockMessageBuilder(long stationID) {
        super(stationID);

        this.tempRandomizer = new SecureRandom();
        this.windSpeedRandomizer = new SecureRandom();
        this.humidityRandomizer = new SecureRandom();
    }

    @Override
    protected void setStatusTimeStamp() {
        super.message.setStatusTimestamp(System.currentTimeMillis() / 1000L);
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
