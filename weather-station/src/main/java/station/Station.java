package station;

import lombok.extern.slf4j.Slf4j;
import message_utils.message.Message;
import message_utils.message_builder.Builder;
import message_utils.message_builder.Director;
import message_utils.message_serialization.MessageSerializer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Slf4j
public class Station {
    private final Director director;
    private final Builder builder;
    private static Station station;
    private final long sleepingTime;
    private final String topicName;
    private final KafkaProducer<String, String> kafkaProducer;

    public static Station getInstance(Director director, Builder builder, long sleepingTime) throws IOException {
        if (station == null) return station = new Station(director, builder, sleepingTime);
        return station;
    }

    private Station(Director director, Builder builder, long sleepingTime) throws IOException {
        this.director = director;
        this.builder = builder;
        this.sleepingTime = sleepingTime;

        Properties props = new Properties();
        InputStream kafkaIS = Station.class.getClassLoader().getResourceAsStream("kafkaConfig.properties");
        props.load(kafkaIS);

        this.topicName = props.get("topic").toString();
        props.remove("topic");
        this.kafkaProducer = new KafkaProducer<>(props);
    }
    public void SendMessages() throws IOException, InterruptedException {
        while (true){
            this.director.BuildMessage();
            Message message = this.builder.getMessage();
            ProducerRecord<String, String> record = new ProducerRecord<>(topicName, MessageSerializer.Serialize(message));
            this.kafkaProducer.send(record);
            Thread.sleep(sleepingTime);
        }
    }
}
