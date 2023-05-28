from elasticsearch import Elasticsearch
import pyarrow.parquet as pq
from confluent_kafka import Consumer, KafkaException

# Create Kafka consumer configuration
conf = {
    'bootstrap.servers': 'kafka-service:9092',
    'group.id': 'weather_group',
    'auto.offset.reset': 'earliest'  # Start consuming from the beginning of the topic
}

# Create Kafka consumer instance
consumer = Consumer(conf)

# Subscribe to the Kafka topic(s) you want to consume from
consumer.subscribe(['paths_topic'])

try:
    print('start listensing')
    while True:
        # Poll for new messages
        msg = consumer.poll(timeout=1.0)  # Set timeout to control the frequency of consuming

        if msg is None:
            continue  # No new messages, continue to the next iteration

        # if msg.error():
        #     if msg.error().code() == KafkaError._PARTITION_EOF:
        #         # Reached end of partition, continue to the next iteration
        #         continue
        #     else:
        #         print(f"Error occurred: {msg.error().str()}")
        #         break

        print('get message from kafka topic: ', msg)
        # Process the consumed message
        parquet_file = msg.value().decode('utf-8')  # Assuming the message value is a string
        if not os.path.isfile(parquet_file):
            print(f"File not found: {parquet_file}")
            continue  # Skip to the next iteration if file not found
        # Read Parquet file
        parquet_table = pq.read_table(parquet_file)

        # Convert Parquet table to Pandas DataFrame
        data_frame = parquet_table.to_pandas()

        # Convert DataFrame rows to Elasticsearch-compatible JSON documents
        documents = data_frame.to_dict(orient='records')

        print(len(documents))
        # Create Elasticsearch connection
        es = Elasticsearch(['http://elastic-service:9200'], timeout=30)

        # Bulk index documents into Elasticsearch
        bulk_data = []
        for doc in documents:
            bulk_data.append({'index': {'_index': 'weather_topic'}})
            bulk_data.append(doc)

        es.bulk(index='weather_topic', body=bulk_data)
        print('upload parquet file')


except KeyboardInterrupt:
    pass

finally:
    # Close the Kafka consumer to release resources
    consumer.close()