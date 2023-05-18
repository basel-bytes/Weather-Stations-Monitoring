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
}
