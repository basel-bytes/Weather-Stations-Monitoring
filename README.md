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
- implemented in [kafka-processing service](./kafka-processor/)
- There are two types of processing following **router integration pattern**:
  
| Processing Type | Description |
| --- | --- |
| Dropping Messages | Processes messages by some probabilistic sampling, then throw some of them away |
| Raining Areas | Processes messages and detects rain according humidity, then throw messages from rainy areas to some topic. We use Kstream and filters to do such processors |

- All these processing produce undropped messages to weather_topic And all records have humidity >= 70 go to raining topic


<a name="-data-processing-and-archiving"></a> 
## Data Processing And Archiving

<a name="-data-indexing"></a> 
## Data Indexing



<a name="-how-to-run"></a> 
## How To Run


