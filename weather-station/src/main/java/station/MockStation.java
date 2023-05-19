package station;

import message_utils.message.Message;
import message_utils.message_builder.Director;
import message_utils.message_builder.MockMessageBuilder;
import message_utils.message_serialization.MessageSerializer;

public class MockStation implements Station{
    private final Director director;
    private final MockMessageBuilder builder;

    public MockStation() {
        long stationID = IDGenerator.generateStationID();
        System.out.println("Weather Station Created with: " + stationID);
        long lastMessageSent = 0;
        this.builder = new MockMessageBuilder(stationID, lastMessageSent);
        this.director = new Director(builder);
    }

    @Override
    public void SendMessage() {
        this.director.BuildMessage();
        Message message = this.builder.getMessage();
        System.out.println(MessageSerializer.Serialize(message));
    }
}
