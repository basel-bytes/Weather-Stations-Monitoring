package bitcask;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class SegmentsReader {
    public static String getMeThisValue(Pointer pointer) throws IOException {
        //Access segment in RandomAccess
        RandomAccessFile file = new RandomAccessFile(pointer.getFilePath(), "rw");
        Long byteOffset = pointer.getByteOffset();
        //Seek to byte offset
        file.seek(byteOffset);
        Long timstamp = file.readLong();
        int key_size = file.readInt();
        int value_size = file.readInt();
        //Seek to value [Timestamp(long 8 bytes)|key_size(int 4 bytes)|val_size(int 4 bytes)|key|val]
        file.seek(byteOffset + 16 + key_size);
        byte[] buffer = new byte[value_size];
        file.readFully(buffer);
        String val = new String(buffer, StandardCharsets.UTF_8);
        return val;
    }

    public static void readAHintFile_recovery(String hintFilePath, HashMap<String, Pointer> hashTable) throws IOException {
        RandomAccessFile file = new RandomAccessFile(hintFilePath, "rw");
        Long len = file.length();
        Long read_bytes = 0L;
        HashMap<String, Pointer> tmp_table = new HashMap<>();
        while (read_bytes < len) {
            file.seek(read_bytes);
            Long timestamp = file.readLong();
            int key_size = file.readInt();
            int pointer_size = file.readInt();

            file.seek(read_bytes + 16);

            byte[] buffer_key = new byte[key_size];
            file.readFully(buffer_key);
            String key = new String(buffer_key, StandardCharsets.UTF_8);

            if (hashTable.containsKey(key)) {
                read_bytes += 16 + key_size + pointer_size;
                return;
            }
            file.seek(read_bytes + 16 + key_size);
            byte[] buffer_pointer = new byte[pointer_size];
            file.readFully(buffer_pointer);
            String pointerStr = new String(buffer_pointer, StandardCharsets.UTF_8);
            Pointer pointer = Pointer.pointerFromString(pointerStr);

            tmp_table.put(key, pointer);
            read_bytes += key_size + pointer_size + 16;
        }
        for(var keyptr: tmp_table.entrySet()){
            hashTable.put(keyptr.getKey(), keyptr.getValue());
        }
        file.close();
    }

    public static void readActiveFile_recovery(String activeFilePath, HashMap<String, Pointer> hashTable) throws IOException {
        RandomAccessFile file = new RandomAccessFile(activeFilePath, "rw");
        Long len = file.length();
        Long read_bytes = 0L;
        while (read_bytes < len) {
            //Read tmstmp
            file.readLong();
            //Read key size, val size
            int key_size = file.readInt();
            int val_size = file.readInt();
            //This condition was put to handle if the last key,val was cut before complete writing
            //Happens in case of crash recovery
            if(read_bytes + 16 + key_size + val_size > len){
                break;
            }
            //seek to key
            file.seek(read_bytes + 16);
            //read key
            byte[] buffer_key = new byte[key_size];
            file.readFully(buffer_key);
            String key = new String(buffer_key, StandardCharsets.UTF_8);
            //store in map
            Pointer pointer = new Pointer(activeFilePath, read_bytes);
            hashTable.put(key, pointer);
            //add to end 16 + keysize + valsize
            read_bytes += 16 + key_size + val_size;
        }
        file.close();
    }
}
