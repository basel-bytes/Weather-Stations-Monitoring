package org.example;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.util.Pair;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.KStream;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

public class Main {
    static Function<String, ShortMessage> splitter = (json) -> {
        // Perform some logic on the input String
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(json, JsonObject.class).get("weather").getAsJsonObject();
        return new ShortMessage(jsonObject.get("humidity").getAsInt());
    };

    public static void main(String[] args) throws IOException {
        System.out.println("Hello from Kafka Processor :)");
        Properties config = loadProperties();

        List<Pair<Boolean, Double>> dropPercentages = new ArrayList<>();
        dropPercentages.add(new Pair<>(true, 0.1));
        dropPercentages.add(new Pair<>(false, 0.9));
        EnumeratedDistribution<Boolean> dropDecider = new EnumeratedDistribution<>(dropPercentages);

        NewTopic droopedTopic = new NewTopic("dropped", 1, (short) 1);
        NewTopic weatherTopic = new NewTopic("weather_topic", 1, (short) 1);
        NewTopic rainingTopic = new NewTopic("raining", 1, (short) 1);

        StreamsBuilder builder = new StreamsBuilder();
        KStream<String, String> inputStream = builder.stream("processor_topic");

        inputStream.peek((key, value) -> System.out.println("key: " + key + " value: " + value))
                   .filter((key, value) -> dropDecider.sample())
                   .peek((key, value) -> System.out.println("Dropped: " + " value: " + value))
                   .to("dropped");

        inputStream.peek((key, value) -> System.out.println("key: " + key + " value: " + value))
                   .filterNot((key, value) -> dropDecider.sample())
                   .peek((key, value) -> System.out.println("Not Dropped: " + " value: " + value))
                   .to("weather_topic");

        inputStream.peek((key, value) -> System.out.println("key: " + key + " value: " + value))
                   .filterNot((key, value) -> dropDecider.sample())
                   .filter((key, value) -> splitter.apply(value).humidity > 70)
                   .peek((key, value) -> System.out.println("Raining at " + value))
                   .to("raining");

        KafkaStreams streams = new KafkaStreams(builder.build(), config);
        streams.start();

        try {Thread.sleep(Long.MAX_VALUE);}
        catch (InterruptedException e) {e.printStackTrace();}
        streams.cleanUp();
    }

    public static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream("config.properties")) {
            properties.load(input);
        }
        return properties;
    }

}
