from elasticsearch import Elasticsearch

# Connect to Elasticsearch
es = Elasticsearch('http://localhost:9200')

# Get a list of all indexes
response = es.indices.get_alias(index='*')

# Print the index names
for index_name in response:
    print(index_name)
