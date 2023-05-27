package bitcask;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class ActiveSegment {
    private final Long maxSize = 1000000L;
    private String fileName;
    private String segmentsDirectoryPath;
    private Long byteOffset;
    private boolean locked;
    private static final ReentrantReadWriteLock lock = new ReentrantReadWriteLock(true);

    public ActiveSegment(String segmentsDirectoryPath, String fileName) {
        this.segmentsDirectoryPath = segmentsDirectoryPath;
        this.fileName = fileName;
        this.byteOffset = 0L;
        this.locked = false;
    }

    public String getFileName() {
        return fileName;
    }

    public Long getByteOffset() {
        return byteOffset;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setFilePath(String fileName) {
        this.fileName = fileName;
    }

    public void setByteOffset(Long byteOffset) {
        this.byteOffset = byteOffset;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean reachedMaxSize() {
        return this.byteOffset > maxSize;
    }

    public void write(String key, String value, HashMap<String, Pointer> hashTable) throws IOException {
        this.byteOffset += SegmentsWriter.writeIntoFile(new Timestamp(System.currentTimeMillis()).getTime(), key, value, hashTable, segmentsDirectoryPath + "/" + fileName + ".bin", this.byteOffset, true);
    }

    public void wrapItUpandClose() throws IOException {
        String hintFilePath = this.segmentsDirectoryPath + "/hint_" + this.fileName + ".bin";
        String segmentPath = this.segmentsDirectoryPath + "/" + fileName + ".bin";
        HashMap<String, Pointer> keys_vals = new HashMap<>();
        RandomAccessFile segmentReader = new RandomAccessFile(segmentPath, "rw");
        Long len = segmentReader.length();
        Long read_bytes = 0L;
        while (read_bytes < len) {
            Long timestamp = segmentReader.readLong();
            int key_size = segmentReader.readInt();
            int value_size = segmentReader.readInt();
            segmentReader.seek(read_bytes + 16);
            byte[] buffer_key = new byte[key_size];
            segmentReader.readFully(buffer_key);
            segmentReader.seek(read_bytes + 16 + key_size);
            byte[] buffer_value = new byte[value_size];
            segmentReader.readFully(buffer_value);
            String key = new String(buffer_key, StandardCharsets.UTF_8);
            String value = new String(buffer_value, StandardCharsets.UTF_8);
            keys_vals.put(key, new Pointer(segmentPath, read_bytes));
            read_bytes += key_size + value_size + 16;
        }
        Long hintByteOffset = 0L, returnedhintByteOffset = 0L;
        for (var entry : keys_vals.entrySet()) {
            returnedhintByteOffset = SegmentsWriter.writeIntoFile(new Timestamp(System.currentTimeMillis()).getTime(), entry.getKey(), entry.getValue().toString(), null, hintFilePath, hintByteOffset, false);
            hintByteOffset += returnedhintByteOffset;
        }
        segmentReader.close();
    }


}
