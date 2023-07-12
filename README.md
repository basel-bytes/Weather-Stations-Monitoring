# Weather-Stations-Monitoring
DDIA course Project
<p align="center">
  <img src="https://github-production-user-asset-6210df.s3.amazonaws.com/95547833/252765129-51d43a3d-e529-41c4-9cec-aedf19528acf.png?X-Amz-Algorithm=AWS4-HMAC-SHA256&X-Amz-Credential=AKIAIWNJYAX4CSVEH53A%2F20230711%2Fus-east-1%2Fs3%2Faws4_request&X-Amz-Date=20230711T185958Z&X-Amz-Expires=300&X-Amz-Signature=eb12a22d7d023a44ce8d9c235d4b9af028e6d0640aed95c6fd291f59a99f6755&X-Amz-SignedHeaders=host&actor_id=95547833&key_id=0&repo_id=641528715" alt="Description of image" style="width: 50%; height: auto;" />
</p>

## Agenda
- [System Architecture](#system-architecture)
- [How to Run](#how-to-run)

<a name="-system-architecture"></a> 
## System Architecture
There are 3 major components implemented using 6 microservices. </br>
The 3 major components are:
1) [Data Acquisition](#data-acquisition).
2) [Data Processing & Archiving](#data-processing-and-archiving).
3) [Data Indexing](#data-indexing).

<a name="-data-acquisition"></a> 
## Data Acquisition
multiple weather stations that feed a queueing service (Kafka) with their readings.</br>
### A) Weather Stations
- implemented in [weather-station service](./weather-station/)
- We have 2 types of stations: **Mock Stations**, and **Adapter Stations**.
- Mock Stations are required to randomise its weather readings.
- Adapter Stations get results from Open-Meteo according to a latitude and longitude given at the beginning
- Both types will have the same battery distribution and dropping percentages.
- We use a **station factory design pattern** to indicate which type of station we would like to build.
- We use a **builder design pattern** to build the message to be sent.
- Messages coming from Open Meteo are brought according to the **Adapter and filter integration pattern**.
![image](https://github.com/basel-bytes/Weather-Stations-Monitoring/assets/95547833/dec216b6-d091-4a40-9c65-0dc6095293e2)

### B) Kafka Processors
- Implemented in [kafka-processing service](./kafka-processor/)
- There are two types of processing following **router integration pattern**:
  
| Processing Type | Description |
| --- | --- |
| Dropping Messages | Processes messages by some probabilistic sampling, then throw some of them away |
| Raining Areas | Processes messages and detects rain according humidity, then throw messages from rainy areas to some topic. We use Kstream and filters to do such processors |

- All these processing produce undropped messages to **weather_topic** And all records have **humidity >= 70** go to **raining topic**.


<a name="-data-processing-and-archiving"></a> 
## Data Processing And Archiving
- Data is first consumed from kafka topics then written in batches of **10k messages** in the form of **parquet files** to support efficient analytical queries. 
- This is implemented in [base-central-station service](./base-central-station/).
- Massege partiitioning is done via a **Chronological job** that is configured to run every 24 hours at midnight .
- Such Chronological job is scheduled with the help of **k8s**.
- the Chronological job is implemented in **scala** using **apache spark** to partition messages in parquet files by **both day and station_id**.
- see the Chronological job implementation in [sparky-file-partition service](./sparky-file-partition/).
  ![image](https://github.com/basel-bytes/Weather-Stations-Monitoring/assets/95547833/6c78a981-bee9-4202-9866-64ed193e124d)

  
<a name="-data-indexing"></a> 
## Data Indexing
This is composed of two parts: </br>
a) [BitCask Storage](#bitcask-storage) </br>
b) [Elastic Search and Kibana](#elastic-search-and-kibana) </br>

<a name="-bitcask-storage"></a> 
## Bitcask Storage
To implement, we should **maintain a key value** store of the station's statuses. To do this efficiently, we implemented the BitCask Riak LSM to maintain an updated store of each station status. The implementation should be as discussed in lectures with some notes:
- **Hint files** implementation to help in rehash for recovery.
- **Scheduling compaction** over segment files to avoid disrupting active readers.
- **No checksums implemented** to detect errors
- **No tombstones implemented** for deletions as there is no point in deleting some weather station ID entry.
- **Assumption:** </br>
   Replica files are the only files deleted in Compaction.
- 

<a name="-elastic-search-and-kibana"></a> 
## Elastic Search and Kibana


<a name="-how-to-run"></a> 
## How To Run


