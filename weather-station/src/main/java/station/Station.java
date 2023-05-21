package station;

import message_utils.message.Message;
import message_utils.message_builder.Builder;
import message_utils.message_builder.Director;
import message_utils.message_serialization.MessageSerializer;

import java.io.IOException;

public class Station {
    private final Director director;
    private final Builder builder;
    public static Station station;

    long sleepingTime;

    public static Station getInstance(Director director, Builder builder, long sleepingTime) {
        if (station == null) return station = new Station(director, builder, sleepingTime);
        return station;
    }

    private Station(Director director, Builder builder, long sleepingTime) {
        this.director = director;
        this.builder = builder;
        this.sleepingTime = sleepingTime;
    }
    public void SendMessages() throws IOException, InterruptedException {
        while (true){
            this.director.BuildMessage();
            Message message = this.builder.getMessage();
            System.out.println(MessageSerializer.Serialize(message));
            Thread.sleep(sleepingTime);
        }
    }
}
