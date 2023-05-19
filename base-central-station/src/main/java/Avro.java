import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.json.simple.JSONObject;

public class Avro {
    public static GenericRecord getRecord(JSONObject obj, Schema schema) {
        GenericRecord record = new GenericData.Record(schema);
        record.put("station_id", obj.get("station_id"));
        record.put("s_no", obj.get("s_no"));
        record.put("battery_status", obj.get("battery_status"));
        record.put("status_timestamp", obj.get("status_timestamp"));
        GenericData.Record weatherRecord = new GenericData.Record(schema.getField("weather").schema());
        JSONObject weatherObj = (JSONObject) obj.get("weather");
        weatherRecord.put("humidity", weatherObj.get("humidity"));
        weatherRecord.put("temperature", weatherObj.get("temperature"));
        weatherRecord.put("wind_speed", weatherObj.get("wind_speed"));
        record.put("weather", weatherRecord);
        return record;
    }
}
