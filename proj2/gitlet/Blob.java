package gitlet;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

import static gitlet.Utils.*;

public class Blob implements Serializable {
    /** The name of this blob. */
    private String fileName;
    /** The content of this blob. */
    private byte[] fileContent;

    /**Initialize a gitlet blob.
     * @param fileName the name of this blob
     * @param fileContent the content of this blob
     * */
    public Blob(String fileName, byte[] fileContent) {
        this.fileName = fileName;
        this.fileContent = fileContent;
    }

    public String getFileName() {
        return this.fileName;
    }

    public byte[] getFileContent() {
        return this.fileContent;
    }

    public String getFileContentAsString() {
        return new String(this.fileContent, StandardCharsets.UTF_8);
    }

    public String getBlobSHA() {
        return sha1(this.fileName, this.fileContent);
    }
}
