package bitcask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

public class CompactionTask {
    private Timer timer;

    private String segmentsPath;

    private final String STALE = "STALE";

    public CompactionTask(String segmentsPath) {
        timer = new Timer();
        this.segmentsPath = segmentsPath;
        timer.schedule(new Compact(), 60000, 60000);
        timer.schedule(new deleteStaleReplicas(), 100000, 100000);
    }

    private List<String> getReplicaFiles() throws Exception {
        File directory = new File(this.segmentsPath);

        // Check if the specified path is a directory
        if (directory.isDirectory()) {
            // Get all files and directories within the specified directory
            File[] files = directory.listFiles();
            List<String> replicaFiles = Arrays.stream(files).filter(file -> file.getName().contains("replica")).map(file -> file.getName()).collect(Collectors.toList());
            return replicaFiles.isEmpty() ? null : replicaFiles;
        } else {
            throw new Exception("The specified path is not a directory.");
        }
    }

    public void compactFile(String segmentsPath, String replicaFileName) throws IOException {
        int segment_no = extractNumber(replicaFileName);
        //If the file is active file, don't compact it
        File file = new File(segmentsPath + "/" + "hint_segment" + segment_no + ".bin");
        if(!file.exists()){
            return;
        }
        String compactedFilePath = this.segmentsPath + "/compacted_" + segment_no + ".bin";
        String replicaFile = segmentsPath + "/" + replicaFileName;
        HashMap<String, Pair<Long, String>> keys_vals = new HashMap<>();
        RandomAccessFile segmentReader = new RandomAccessFile(replicaFile, "rw");
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
            Pair<Long, String> pair = new Pair<>(timestamp, value);
            keys_vals.put(key, pair);
            read_bytes += 16 + key_size + value_size;
        }
        //Mark Replica file as STALE
        new RandomAccessFile(replicaFile, "rw").write(STALE.getBytes("UTF-8"));

        //Write into compacted file without replication
        Long compactedByteOffset = 0L, returnedhintByteOffset = 0L;
        for(var entry : keys_vals.entrySet()){
            Long timestamp = entry.getValue().getKey();
            String key = entry.getKey();
            String value = entry.getValue().getValue();
            returnedhintByteOffset  = SegmentsWriter.writeIntoFile(timestamp, key, value, null, compactedFilePath, compactedByteOffset, false);
            compactedByteOffset += returnedhintByteOffset;
        }
        System.out.println("\n==============Finished Compaction===============\n");
        segmentReader.close();
    }

    private class Compact extends TimerTask {
        @Override
        public void run() {
            System.out.println("\n---------------Compact-----------------\n");
            try {
                List<String> replicaFilesNames = getReplicaFiles();
                for(String replicaFileName : replicaFilesNames){
                    System.out.println(replicaFileName);
                    compactFile(segmentsPath, replicaFileName);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }

    private class deleteStaleReplicas extends TimerTask {
        @Override
        public void run() {
            System.out.println("\n---------------Delete Stale-----------------\n");
            try {
                List<String> replicaFilesNames = getReplicaFiles();
                for (String replicaFileName : replicaFilesNames) {
                    compactFile(segmentsPath, replicaFileName);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private int extractNumber(String fileName) {
        int startIndex = fileName.indexOf("segment") + "segment".length();
        int endIndex = fileName.indexOf("_replica");

        String numberString = fileName.substring(startIndex, endIndex);

        return Integer.parseInt(numberString);
    }

    public void deleteIfStale(String replicaFileName) throws IOException {
        String replicaFilePath = segmentsPath + "/" + replicaFileName;
        byte[] buffer_key = new byte[STALE.length()];
        new RandomAccessFile(replicaFilePath, "rw").readFully(buffer_key);
        String state = new String(buffer_key, StandardCharsets.UTF_8);
        if(state.equals(STALE)) {
            new File(replicaFilePath).delete();
        }
    }
}

