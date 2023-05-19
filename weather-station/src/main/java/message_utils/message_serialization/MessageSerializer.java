package message_utils.message_serialization;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import message_utils.message.Message;

public class MessageSerializer {
    public static String Serialize(Message message){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(message);
    }
}
