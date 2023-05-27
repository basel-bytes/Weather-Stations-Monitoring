package bitcask;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Pointer {
    private String filePath;
    private Long byteOffset;

    public Pointer() {
    }

    public Pointer(String filePath, Long byteOffset) {
        this.filePath = filePath;
        this.byteOffset = byteOffset;
    }

    public String getFilePath() {
        return filePath;
    }

    public Long getByteOffset() {
        return byteOffset;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setByteOffset(Long byteOffset) {
        this.byteOffset = byteOffset;
    }

    @Override
    public String toString() {
        return "{" +
                "\"filePath\":\"" + filePath + "\"," +
                "\"byteOffset\":" + byteOffset +
                '}';
    }

    public static Pointer pointerFromString(String pointerStr) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Pointer pointer = mapper.readValue(pointerStr, Pointer.class);
        return pointer;
    }
}
