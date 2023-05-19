package message_utils.message_builder;

import message_utils.message.Message;
import message_utils.message.Weather;

public abstract class Builder {
    protected long stationID;
    protected Message message;
    protected Weather weather;

    public Builder(long stationID) {this.stationID = stationID;}

    protected void reset(){
        this.message = new Message();
        this.weather = new Weather();
    }
    protected abstract void setID();
    protected abstract void setSerialNumber();
    protected abstract void setBatteryStatus();
    protected abstract void setStatusTimeStamp();
    protected abstract void setHumidity();
    protected abstract void setTemperature();
    protected abstract void setWindSpeed();

    public Message getMessage() {
        this.message.setWeather(weather);
        return message;
    }
}
