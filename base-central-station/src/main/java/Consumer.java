import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.hadoop.fs.Path;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class Consumer {

    private static final int BATCH_SIZE = 10000;
    private static final String schemaFilePath = "base-central-station/src/main/java/schema.avsc";
    public static Properties getProps() {
        Properties pros = new Properties();
        pros.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        pros.put(ConsumerConfig.GROUP_ID_CONFIG, "group1");
        pros.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        pros.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        return pros;
    }

    public static ParquetWriter createParquetWriter(Path path, Schema schema) throws IOException {
        return AvroParquetWriter.builder(path)
                .withSchema(schema)
                .build();
    }

    public static void main(String[] args) throws ParseException, IOException {
        KafkaConsumer<String, String>  consumer = new KafkaConsumer<>(getProps());
        consumer.subscribe(Collections.singleton("weather_topic"));

        JSONParser parser = new JSONParser();
        Schema schema = new Schema.Parser().parse(new File(schemaFilePath));

        String dir = "base-central-station/archive/";
        String subdir = "base-central-station/archive/";
        int countRecords = 0;
        ParquetWriter parquetWriter = null;

        try {
            LocalDate currentDay = LocalDate.of(2020, 9, 13);
            while (true) {
                if (!LocalDate.now().equals(currentDay)) {
                    currentDay = LocalDate.now();
                    subdir = dir + currentDay + "/";
                }
                ConsumerRecords<String, String> records = consumer.poll(1000 * 60);
                System.out.println(records.count());
                if (countRecords == 0) {
                    Path path = new Path(subdir + UUID.randomUUID() + ".parquet");
                    parquetWriter = createParquetWriter(path, schema);
                }
                countRecords += records.count();
                for (ConsumerRecord<String, String> r : records) {
                    JSONObject obj = (JSONObject) parser.parse(r.value());
                    GenericRecord record = Avro.getRecord(obj, schema);
                    System.out.println(record);
                    parquetWriter.write(record);
                }
                if (countRecords >= BATCH_SIZE) {
                    parquetWriter.close();
                    countRecords = 0;
                }
            }
        }
        finally {
            consumer.close();
        }
    }
}

//    SparkSession spark = SparkSession.builder()
//            .appName("Write JSON to Parquet")
//            .master("local")
//            .getOrCreate();
//    String[] partitionCols = new String[]{"station_id", "status_timestamp"};
//    Dataset<Row> dataset = spark.createDataset(genericRecordList, Encoders.bean(GenericRecord.class)).toDF();
//    dataset.write().partitionBy(partitionCols).parquet(dirPath);