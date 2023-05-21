import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import station.Station;
import station.StationFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Station station = StationFactory.generateStation(new String[]{"Mock"});
        station.SendMessages();

//        Properties props = new Properties();
//        InputStream kafkaIS = Main.class.getClassLoader().getResourceAsStream("kafkaConfig.properties");
//        props.load(kafkaIS);
//
//        String topicName = props.get("topic").toString();
//        props.remove("topic");
//        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(props);
//        ProducerRecord<String, String> record = new ProducerRecord<>(topicName, "Heeeeeeeelllllo");
//        kafkaProducer.send(record);
//        kafkaProducer.close();
    }
}
