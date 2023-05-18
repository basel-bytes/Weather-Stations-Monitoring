import java.io.IOException;
import java.util.HashMap;

public class BitCask {

    //LSM Tree
    private LSMTree lsmTree;

    private Long tree_size_in_bytes = 0L;

    public Long getTree_size_in_bytes() {
        return tree_size_in_bytes;
    }

    private final Long threshold_size = 1000000L; //threshold size is (Tree size = 1 MB)
    //Segments
    private SegmentsHandler segmentsHandler;
    private String segmentsPath;
    private int segment_no = 0;
    //HashTable
    private HashMap<String, Pointer> hashTable;


    public BitCask(String segmentsPath) {
        this.segmentsPath = segmentsPath;
        this.lsmTree = new LSMTree();
        this.segmentsHandler = new SegmentsHandler();
        this.hashTable = new HashMap<>();
    }


    boolean reachedMaxSize(){
        return tree_size_in_bytes > threshold_size;
    }

    public void write(String key, String value) throws IOException {
        lsmTree.insert(key, value);
        tree_size_in_bytes += key.length() + value.length();
        if(tree_size_in_bytes > threshold_size){
            System.out.println("((((((((((((((((Flushing))))))))))))))))");
            segmentsHandler.flushLSMTree(lsmTree, segmentsPath, segment_no, hashTable);
            segment_no++;
            lsmTree = new LSMTree<String, String>();
            tree_size_in_bytes = 0L;
        }
    }

    public String read(String key) throws IOException {
        String val = (String) lsmTree.search(key);
        if(val != null){
            return val;
        }else{
            if(hashTable.containsKey(key)){
                return segmentsHandler.getMeThisValue(hashTable.get(key));
            }else{
                return null;
            }
        }
    }
}
