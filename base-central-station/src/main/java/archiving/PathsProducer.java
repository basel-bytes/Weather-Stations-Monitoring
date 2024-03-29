package archiving;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class PathsProducer {
    private static final KafkaProducer<String, String> producer = new KafkaProducer<>(getProps());

    public static Properties getProps() {
        Properties pros = new Properties();
        pros.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-service:9092");
        pros.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        pros.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        return pros;
    }

    public static void produce(String message) {
        System.out.println("\u001B[34m" + "parquet file path : " + message + "\u001B[0m");
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>("paths_topic", message);
        producer.send(producerRecord);
    }

}
