package gitlet;



import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TreeMap;

import static gitlet.Utils.*;

/** Represents a gitlet commit object.
 *
 *  does at a high level.
 *
 *  @author Zhu Jiayou
 */
public class Commit implements Serializable {
    /**
     *
     *
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** The message of this Commit. */
    private String message;
    /** The timestamp of this Commit. */
    private String timestamp;
    /** The parent of this Commit. */
    private Commit parent;
    /** The blobs of this Commit.
     * The key is the file address (SHA),
     * the value is the contents of the blob. */
    private TreeMap<String, File> blobs = new TreeMap<>();
    /* The parents of this Commit. */
    private List<Commit> parents;
    /* Whether this Commit is merged. */
    private boolean isMerged = false;


    /**Initialize a gitlet commit.*/
    public Commit() {
        this.message = "initial commit";
        this.timestamp = formatDate(new Date(0));
        this.parent = null;
        this.blobs = null;
        this.isMerged = false;
    }

    /** Initialize a gitlet commit.
     * @param message the message of this commit
     * @param parent the parent of this commit
     * @param blobs the blobs of this commit, the key is the file name, the value is the file itself
     * */
    public Commit(String message, Commit parent, TreeMap<String, File> blobs) {
        this.message = message;
        this.timestamp = formatDate(new Date());
        this.parent = parent;
        this.blobs = blobs;
        this.isMerged = false;
    }

    public Commit(String message, List<Commit> parents, TreeMap<String, File> blobs) {
        this.message = message;
        this.timestamp = formatDate(new Date());
        this.blobs = blobs;
        this.parents = parents;
        this.isMerged = true;
    }

    public TreeMap<String, File> getBlobs() {
        return this.blobs;
    }

    public String getMessage() {
        return this.message;
    }

    public String getSHA() {
        String parentSHA = this.parent == null ? "" : this.parent.getSHA();
        return sha1(this.message, this.timestamp, parentSHA);
    }

    public Commit getParent() {
        return this.parent;
    }

    @Override
    public String toString() {
        if (!this.isMerged) {
            return "===\n"
                    + "commit " + this.getSHA() + "\n"
                    + "Date: " + this.timestamp + "\n"
                    + this.message + "\n";
        } else {
            Commit firstParent = this.parents.get(0);
            Commit secondParent = this.parents.get(1);
            return "===\n"
                    + "commit " + this.getSHA() + "\n"
                    + "Merge: " + firstParent.getSHA().substring(0, 7) + " "
                                + secondParent.getSHA().substring(0, 7) + "\n"
                    + "Date: " + this.timestamp + "\n"
                    + this.message + "\n";
        }
    }

    private String formatDate(Date currentDate) {
        SimpleDateFormat dateFormat =
                new SimpleDateFormat("EEE MMM dd HH:mm:ss yyyy Z", Locale.ENGLISH);
        String formattedDate = dateFormat.format(currentDate);
        return formattedDate;
    }
}
