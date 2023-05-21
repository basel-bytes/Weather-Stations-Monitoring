package org.example;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.*;
import org.apache.kafka.streams.kstream.KStream;
import java.util.Properties;
import java.util.function.Function;

import org.apache.kafka.streams.StreamsConfig;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
public class processor {

    static Function<String, Integer> splitter = (json) -> {
        // Perform some logic on the input String
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
        return jsonObject.get("x").getAsInt();   //humidity
    };
    public static void main(String[] args) throws InterruptedException {

        Properties config = new Properties();
        config.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-processor");
        config.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        StreamsBuilder builder = new StreamsBuilder();

        KStream<String, String> processor = builder.stream("my-first");


        KStream<String, String> filteredStream = processor.filter((key, value) -> splitter.apply(value) > 70);

        filteredStream.foreach((key, value) -> {
            filteredStream.to("special");
        });

        // Build the Kafka Streams application
        KafkaStreams streams = new KafkaStreams(builder.build(), config);

        // Start the Kafka Streams application
        streams.start();

        // Wait indefinitely
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Clean up resources
        streams.cleanUp();
    }
}
