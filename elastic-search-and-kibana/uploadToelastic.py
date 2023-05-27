from elasticsearch import Elasticsearch
import pyarrow.parquet as pq
from confluent_kafka import Consumer, KafkaException
import requests

# Create Kafka consumer configuration
conf = {
    'bootstrap.servers': 'http://localhost:9092',
    'group.id': 'your_consumer_group_id',
    'auto.offset.reset': 'earliest'  # Start consuming from the beginning of the topic
}

# Create Kafka consumer instance
consumer = Consumer(conf)

# Subscribe to the Kafka topic(s) you want to consume from
consumer.subscribe(['parquet'])

try:
    while True:
        # Poll for new messages
        msg = consumer.poll(timeout=1.0)  # Set timeout to control the frequency of consuming

        if msg is None:
            continue  # No new messages, continue to the next iteration

        if msg.error():
            if msg.error().code() == KafkaException._PARTITION_EOF:
                continue  # Reached end of partition, continue to the next iteration
            else:
                print(f"Error occurred: {msg.error().str()}")
                break

        # Process the consumed message
        parquet_file = msg.value().decode('utf-8')  # Assuming the message value is a string
        # Read Parquet file
        parquet_table = pq.read_table(parquet_file)

        # Convert Parquet table to Pandas DataFrame
        data_frame = parquet_table.to_pandas()

        # Convert DataFrame rows to Elasticsearch-compatible JSON documents
        documents = data_frame.to_dict(orient='records')

        print(len(documents))
        # Create Elasticsearch connection
        es = Elasticsearch(['http://localhost:9200'])

        # Bulk index documents into Elasticsearch
        bulk_data = []
        for doc in documents:
            bulk_data.append({'index': {'_index': 'test'}})
            bulk_data.append(doc)

        es.bulk(index='test', body=bulk_data)


except KeyboardInterrupt:
    pass

finally:
    # Close the Kafka consumer to release resources
    consumer.close()