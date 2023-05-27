package bitcask;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
public class SegmentsWriter {
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    public static Long writeIntoFile(Long timestamp, String key, String value, HashMap<String, Pointer> hashTable, String segmentPath, Long byteOffset, boolean replicate) throws IOException {
        lock.writeLock().lock();
        RandomAccessFile fileWriter = new RandomAccessFile(segmentPath, "rw");
        fileWriter.seek(byteOffset);
        Long writtenBytes = 0L;
        byte[] keyBytes = key.getBytes("UTF-8");
        byte[] valueBytes = value.getBytes("UTF-8");
        int keySize = keyBytes.length;
        int valueSize = valueBytes.length;
        if(hashTable != null) {
            hashTable.put(key, new Pointer(segmentPath, byteOffset));
        }
        fileWriter.writeLong(timestamp);
        fileWriter.writeInt(keySize);
        fileWriter.writeInt(valueSize);
        fileWriter.write(keyBytes);
        fileWriter.write(valueBytes);
        writtenBytes += 16 + keySize + valueSize;
        if(replicate) {
            RandomAccessFile fileWriter_replica = new RandomAccessFile(segmentPath.substring(0, segmentPath.length() - 4) + "_replica.bin", "rw");
            fileWriter_replica.seek(byteOffset);
            fileWriter_replica.writeLong(timestamp);
            fileWriter_replica.writeInt(keySize);
            fileWriter_replica.writeInt(valueSize);
            fileWriter_replica.write(keyBytes);
            fileWriter_replica.write(valueBytes);
            fileWriter_replica.close();
        }
        lock.writeLock().unlock();
        return writtenBytes;
    }

}
