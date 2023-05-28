package bitcask;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class BitCask {
    private final Long threshold_size = 10000L; //threshold size is (Tree size = 1 MB)
    //Segments
    private SegmentsReader segmentsReader;
    private String segmentsPath;
    private int segment_no = 0;
    //HashTable
    private HashMap<String, Pointer> hashTable;

    public   ActiveSegment activeSegment;

    private CompactionTask compactionTask;


    public BitCask(String segmentsPath) throws Exception {
        this.segmentsPath = segmentsPath;
        this.segmentsReader = new SegmentsReader();
        this.hashTable = new HashMap<>();
        this.activeSegment = null;
        this.segment_no = recoverMeIfYouCan() + 1;
        for(var entry : hashTable.entrySet()){
            System.out.println(entry.getKey() + " " + entry.getValue());
        }
        this.compactionTask = new CompactionTask(segmentsPath);
    }

    public void write(String key, String value) throws IOException {
        if(activeSegment == null){
            activeSegment = new ActiveSegment(segmentsPath ,"segment" + segment_no);
        }
        if(activeSegment.reachedMaxSize()){
            activeSegment.wrapItUpandClose();
            segment_no++;
            activeSegment = new ActiveSegment(segmentsPath, "segment" + segment_no);
        }
        activeSegment.write(key, value, hashTable);
    }

    public String read(String key) throws IOException {
        if(!hashTable.containsKey(key)){
            return null;
        }else{
            return segmentsReader.getMeThisValue(hashTable.get(key));
        }
    }

    public int recoverMeIfYouCan() throws Exception {
        List<String> hintFiles = getHintFiles();
        String activeFileName = getActiveSegment();
        int nextActiveNumber = -1;
        if(activeFileName != null){
            nextActiveNumber = extractNumber(activeFileName);
            System.out.println("\n\n\n\n\n" + nextActiveNumber + "\n\n\n\n\n");
            SegmentsReader.readActiveFile_recovery(segmentsPath + "/" + activeFileName, this.hashTable);
        }
        if(hintFiles != null){
            for(String hintFileName : hintFiles){
                SegmentsReader.readAHintFile_recovery(segmentsPath + "/" + hintFileName, this.hashTable);
            }
        }
        return nextActiveNumber;
    }

    private List<String> getHintFiles() throws Exception {
        File directory = new File(this.segmentsPath);
        // Check if the specified path is a directory
        if (directory.isDirectory()) {
            // Get all files and directories within the specified directory
            File[] files = directory.listFiles();
            List<String> filtered_files = Arrays.stream(files).filter(file -> file.getName()
                            .contains("hint")).map(file -> file.getName())
                            .collect(Collectors.toList());
            filtered_files.sort(Comparator.comparingInt(BitCask::extractNumber).reversed());
            return filtered_files.isEmpty() ? null : filtered_files;
        } else {
            throw new Exception("The specified path is not a directory.");
        }
    }

    private String getActiveSegment() throws Exception {
        System.out.println("get Active Segment");
        File directory = new File(this.segmentsPath);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            List<String> filtered_files = Arrays.stream(files).filter(file -> file.getName()
                            .contains("segment")).map(file -> file.getName())
                    .collect(Collectors.toList());
            filtered_files.removeIf(file -> file.contains("hint") || file.contains("replica")); 
            filtered_files.sort(Comparator.comparingInt(BitCask::extractNumber).reversed());
            return filtered_files.isEmpty() ? null : filtered_files.get(0);
        } else {
            throw new Exception("The specified path is not a directory.");
        }
    }


    private static int extractNumber(String fileName) {
        System.out.println("extract number from fileName " + fileName);
        int startIndex = fileName.indexOf("segment") + "segment".length();
        int endIndex = fileName.indexOf(".bin");

        String numberString = fileName.substring(startIndex, endIndex);
        System.out.println(numberString);
        return Integer.parseInt(numberString);
    }



}
