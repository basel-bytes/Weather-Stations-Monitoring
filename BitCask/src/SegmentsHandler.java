import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SegmentsHandler {
    public void flushLSMTree(LSMTree<String, String> lsmTree, String segmentsPath, int segment_no, HashMap<String, Pointer> hashTable) throws IOException {
        String segmentPath = segmentsPath + "/segment" + segment_no + ".txt";
        FileOutputStream fos = new FileOutputStream(segmentPath, true);
        DataOutputStream dos = new DataOutputStream(fos);

        ArrayList<Map.Entry<String, String>> inorder_data = lsmTree.inorder();

        Long byteOffset = 0L;

        for(var entry: inorder_data){
            //Convert key, value to bytes arrays
            byte[] keyBytes = entry.getKey().getBytes("UTF-8");
            byte[] valueBytes = entry.getValue().getBytes("UTF-8");
            int keySize = keyBytes.length;
            int valueSize = valueBytes.length;
            System.out.println(entry.getKey());
            System.out.println(entry.getValue());
            //Update hashTable, make it point to last occurence of key, at the start, points to the start of key_size
            hashTable.put(entry.getKey(), new Pointer(segmentPath, byteOffset));
            byteOffset += 8 + keySize + valueSize;

            dos.writeInt(keySize);
            dos.writeInt(valueSize);
            dos.write(keyBytes);
            dos.write(valueBytes);
        }

        dos.close();
    }

    public String getMeThisValue(Pointer pointer) throws IOException {
        //Access segment in RandomAccess
        RandomAccessFile file = new RandomAccessFile(pointer.getFilePath(), "rw");

        //Seek to byte offset
        file.seek(pointer.getByteOffset());
        int key_size = file.readInt();
        int value_size = file.readInt();

        //Seek to value [key_size(int 4 bytes)|val_size(int 4 bytes)|key|val]
        file.seek(8 + key_size);
        byte[] buffer = new byte[value_size];
        file.readFully(buffer);
        return new String(buffer, StandardCharsets.UTF_8);
    }
}
