package gitlet;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static gitlet.Utils.*;


/** Represents a gitlet repository.
 *  does at a high level.
 *
 *  @author Zhu Jiayou
 */
public class Repository implements Serializable {
    /*
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");
    /** The exception. */
    public static final GitletException EXCEPTION = new GitletException();
    /** The commit directory. */
    public static final File COMMIT_DIR = join(GITLET_DIR, "objects", "commits");
    /** The blob directory. */
    public static final File BLOB_DIR = join(GITLET_DIR, "objects", "blobs");
    /** The add directory. */
    public static final File ADD_DIR = join(GITLET_DIR, "addstage");
    /** The rm directory. */
    public static final File RM_DIR = join(GITLET_DIR, "rmstage");
    /** The headS directory. */
    public static final File HEAD_DIR = join(GITLET_DIR, "heads");

    public static final File LOG_DIR = join(GITLET_DIR, "logs");

    /**Initialize a gitlet repository.*/
    public static void init() {
        /** File structure to be initialized:
         *  +.gitlet
         *      +objects
         *          +commits
         *          +blobs
         *      +heads
         *      +addstage
         *      +rmstage
         *      +logs
         *      -HEAD
         * */
        if (GITLET_DIR.exists()) {
            EXCEPTION.gitletExistedException();
        }
        GITLET_DIR.mkdir();
        try {
            join(GITLET_DIR, "HEAD").createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        HEAD_DIR.mkdir();
        try {
            join(HEAD_DIR, "master").createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        join(GITLET_DIR, "objects").mkdir();
        COMMIT_DIR.mkdir();
        BLOB_DIR.mkdir();
        ADD_DIR.mkdir();
        RM_DIR.mkdir();
        LOG_DIR.mkdir();
        try {
            join(LOG_DIR, "master").createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Commit initialCommit = new Commit();
        String initialCommitSHA = initialCommit.getSHA();
        try {
            join(COMMIT_DIR, initialCommitSHA).createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writeObject(join(COMMIT_DIR, initialCommitSHA), initialCommit);
        writeContents(join(LOG_DIR, "master"), initialCommit.toString());
        writeContents(join(HEAD_DIR, "master"), initialCommitSHA);
        writeContents(join(GITLET_DIR, "HEAD"), join(HEAD_DIR, "master").getPath());
    }

    /** Add a file to the addstage.
     * @param fileName the name of the file to be added
     * */
    public static void add(String fileName) {
        /* if the file does not exist, throw an exception */
        if (!join(CWD, fileName).exists()) {
            EXCEPTION.notExistFileException();
        }
        Blob blob = new Blob(fileName, readContents(join(CWD, fileName)));
        String blobSHA = blob.getBlobSHA();
        /* if the file is not in the current commit, add it to the addstage
         * if the file is in the current commit and the addstage, remove it from the addstage
         * if the file is in the rmstage, remove it from the rmstage */
        String currentCommitPath = readContentsAsString(join(GITLET_DIR, "HEAD"));
        String currentCommitSHA = readContentsAsString(new File(currentCommitPath));
        Commit currentCommit = readObject(join(COMMIT_DIR, currentCommitSHA), Commit.class);
        TreeMap<String, File> currentTrackedBlobs = currentCommit.getBlobs();
        if (currentTrackedBlobs != null) {
            if (!currentTrackedBlobs.containsValue(join(BLOB_DIR, blobSHA))) {
                if (!join(ADD_DIR, blobSHA).exists()) {
                    try {
                        join(ADD_DIR, blobSHA).createNewFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    writeObject(join(ADD_DIR, blobSHA), blob);
                }
            } else if (join(ADD_DIR, blobSHA).exists()) {
                join(ADD_DIR, blobSHA).delete();
            } else if (join(RM_DIR, blobSHA).exists()) {
                join(RM_DIR, blobSHA).delete();
            }
        } else {
            if (join(RM_DIR, blobSHA).exists()) {
                join(RM_DIR, blobSHA).delete();
            } else {
                try {
                    join(ADD_DIR, blobSHA).createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                writeObject(join(ADD_DIR, blobSHA), blob);
            }
        }
    }

    /** Commit the files in the stage area to the current commit.
     * @param message the message of this commit
     * */
    public static void commit(String message) {
        /* if there is no file in the addstage and rmstage, throw an exception */
        boolean hasAdd = ADD_DIR.list().length != 0;
        boolean hasRm = RM_DIR.list().length != 0;
        if (!hasAdd && !hasRm) {
            EXCEPTION.noChangesException();
        }
        String parentCommitBranch = readContentsAsString(join(GITLET_DIR, "HEAD"));
        String parentCommitSHA = readContentsAsString(new File(parentCommitBranch));
        Commit parentCommit = readObject(join(COMMIT_DIR, parentCommitSHA), Commit.class);
        TreeMap<String, File> blobs;
        if (parentCommit.getBlobs() == null) {
            blobs = null;
        } else {
            blobs = new TreeMap<>(parentCommit.getBlobs());
        }
        /* if the file is in the addstage, add it to the blobs
         * if the file is in the rmstage, untrack it in the new commit
         * delete the file in the addstage and rmstage after */
        if (hasAdd) {
            for (File f : ADD_DIR.listFiles()) {
                Blob b = readObject(f, Blob.class);
                File blobFile = join(BLOB_DIR, b.getBlobSHA());
                try {
                    blobFile.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                writeObject(blobFile, b);
                /* track only the latest version of the file */
                if (blobs == null) {
                    blobs = new TreeMap<>();
                    blobs.put(b.getFileName(), blobFile);
                } else {
                    blobs.put(b.getFileName(), blobFile);
                }
                f.delete();
            }
        }
        if (hasRm) {
            for (File f : RM_DIR.listFiles()) {
                Blob b = readObject(f, Blob.class);
                blobs.remove(b.getFileName());
                f.delete();
            }
        }
        /* create a new commit and write it to the commit directory */
        Commit newCommit = new Commit(message, parentCommit, blobs);
        String newCommitSHA = newCommit.getSHA();
        try {
            join(COMMIT_DIR, newCommitSHA).createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writeObject(join(COMMIT_DIR, newCommitSHA), newCommit);
        /* update the head commit of this branch and the log of this branch */
        String branchPath = readContentsAsString(join(GITLET_DIR, "HEAD"));
        writeContents(new File(branchPath), newCommitSHA);
        String branchName = branchPath.substring(branchPath.lastIndexOf(File.separator) + 1);
        String oldLog = readContentsAsString(join(LOG_DIR, branchName));
        writeContents(join(LOG_DIR, branchName), newCommit.toString() + "\n" + oldLog);
    }

    /** Remove a file from the current commit.
     * @param fileName the name of the file to be removed
     * */
    public static void rm(String fileName) {
        String currentCommitPath = readContentsAsString(join(GITLET_DIR, "HEAD"));
        String currentCommitSHA = readContentsAsString(new File(currentCommitPath));
        Commit currentCommit = readObject(join(COMMIT_DIR, currentCommitSHA), Commit.class);
        TreeMap<String, File> currentTrackedBlobs = currentCommit.getBlobs();
        List<String> fileNameOfBlobs = plainFilenamesIn(ADD_DIR);
        List<String> shaOfBlobs = new ArrayList<>(fileNameOfBlobs);
        fileNameOfBlobs.replaceAll(s -> { // replace the SHA to the file name
            Blob b = readObject(join(ADD_DIR, s), Blob.class);
            return b.getFileName();
        });
        /* if the file is in the addstage, remove it from the addstage
         * if the file is in the current commit, add it to the rmstage,
         * if the file is in the CWD, delete it
         * if the file is not in the addstage and the current commit, throw an exception */
        if (fileNameOfBlobs.contains(fileName)) {
            File addBlobFile = join(ADD_DIR, shaOfBlobs.get(fileNameOfBlobs.indexOf(fileName)));
            addBlobFile.delete();
        } else if (currentTrackedBlobs == null || !currentTrackedBlobs.containsKey(fileName)) {
            EXCEPTION.noNeedToRemoveException();
        } else if (currentTrackedBlobs.containsKey(fileName)) {
            Blob b = readObject(currentTrackedBlobs.get(fileName), Blob.class);
            try {
                join(RM_DIR, b.getBlobSHA()).createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            writeObject(join(RM_DIR, b.getBlobSHA()), b);
            if (join(CWD, fileName).exists()) {
                join(CWD, fileName).delete();
            }
        }
    }

    /** Print the log of this branch. */
    public static void log() {
        /* find the current commit and the current branch */
        String currentCommitSHA = readContentsAsString(
                new File(readContentsAsString(join(GITLET_DIR, "HEAD"))));
        String branchPath = readContentsAsString(join(GITLET_DIR, "HEAD"));
        String branchName = branchPath.substring(
                branchPath.lastIndexOf(File.separator) + 1);
        /* print the log of this branch */
        Path branchLogPath = Paths.get(join(LOG_DIR, branchName).getPath());
        BufferedReader reader = null;
        try {
            reader = Files.newBufferedReader(branchLogPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String line;
        String prevLine = null;
        while (true) {
            try {
                if (!((line = reader.readLine()) != null)) {
                    break;
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            } // read the whole log
            if (line.contains(currentCommitSHA)) {
                // print the log from the current commit to the initial commit
                if (prevLine != null) {
                    System.out.println(prevLine);
                }
                System.out.println(line);
                while (true) {
                    try {
                        if (!((line = reader.readLine()) != null)) {
                            break;
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    System.out.println(line);
                }
                break;
            }
            prevLine = line;
        }
        try {
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /** Print the global log of this repository. */
    public static void globalLog() {
        for (File logFile : LOG_DIR.listFiles()) {
            System.out.println(readContentsAsString(logFile));
        }
    }

    /** Find the commit with the given message.
     * @param message the message of the commit to be found
     * */
    public static void find(String message) {
        boolean hasThisCommit = false;
        List<String> commits = plainFilenamesIn(COMMIT_DIR);
        if (commits != null) {
            for (File commitFile : COMMIT_DIR.listFiles()) {
                Commit commit = readObject(commitFile, Commit.class);
                if (commit.getMessage().equals(message)) {
                    System.out.println(commitFile.getName());
                    hasThisCommit = true;
                }
            }
        }
        if (!hasThisCommit) {
            EXCEPTION.notExistCommitMessageException();
        }
    }

    /** Print the status of this repository. */
    public static void status() {
        /* Print the branch files */
        System.out.println("=== Branches ===");
        String branchPath = readContentsAsString(join(GITLET_DIR, "HEAD"));
        String currentbranchName = branchPath.substring(branchPath.lastIndexOf(File.separator) + 1);
        File[] branches = HEAD_DIR.listFiles();
        Arrays.sort(branches);
        for (File branchFile : branches) {
            if (branchFile.getName().equals(currentbranchName)) {
                System.out.println("*" + currentbranchName);
            } else {
                System.out.println(branchFile.getName());
            }
        }
        System.out.println();
        /* Print the staged files*/
        System.out.println("=== Staged Files ===");
        File[] addFiles = ADD_DIR.listFiles();
        if (addFiles != null) {
            Arrays.sort(addFiles);
            for (File addFile : addFiles) {
                Blob b = readObject(addFile, Blob.class);
                System.out.println(b.getFileName());
            }
        }
        System.out.println();
        /* Print the removed files*/
        System.out.println("=== Removed Files ===");
        File[] rmFiles = RM_DIR.listFiles();
        if (rmFiles != null) {
            Arrays.sort(rmFiles);
            for (File rmFile : rmFiles) {
                Blob b = readObject(rmFile, Blob.class);
                System.out.println(b.getFileName());
            }
        }
        System.out.println();
        /* Print the modifications not staged for commit */
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        /* Print the untracked files */
        System.out.println("=== Untracked Files ===");
        System.out.println();
    }

    /** Checkout a branch or a file or a commit.
     * @param args the arguments
     * */
    public static void checkout(String[] args) {
        if (args.length == 2) {
            checkoutBranch(args[1]);
        } else if (args.length == 3) {
            if (args[1].equals("--")) {
                checkoutFile(args[2]);
            } else {
                EXCEPTION.wrongCommentsException();
            }
        } else if (args.length == 4) {
            if (args[2].equals("--")) {
                checkoutCommit(args[1], args[3]);
            } else {
                EXCEPTION.wrongCommentsException();
            }
        }
    }

    /** Checkout a branch with the given name.
     * @param branchName the name of the branch to be checked out
     * */
    public static void checkoutBranch(String branchName) {
        /* if the branch does not exist, throw an exception */
        File branchFile = join(HEAD_DIR, branchName);
        if (!branchFile.exists()) {
            EXCEPTION.notExistBranchException();
        }
        /* if the branch is the current branch, throw an exception */
        String currentBranchPath = readContentsAsString(join(GITLET_DIR, "HEAD"));
        String currentBranchName = currentBranchPath.substring(
                currentBranchPath.lastIndexOf(File.separator) + 1);
        if (currentBranchName.equals(branchName)) {
            EXCEPTION.inCurrentBranchException();
        }
        /* find the current commit and the branch commit */
        String currentCommitSHA = readContentsAsString(new File(currentBranchPath));
        Commit currentCommit = readObject(join(COMMIT_DIR, currentCommitSHA), Commit.class);
        TreeMap<String, File> currentTrackedBlobs = currentCommit.getBlobs();
        String branchCommitSHA = readContentsAsString(branchFile);
        Commit branchCommit = readObject(join(COMMIT_DIR, branchCommitSHA), Commit.class);
        TreeMap<String, File> branchTrackedBlobs = branchCommit.getBlobs();
        /* iterate through the branch commit */
        if (!currentCommitSHA.equals(branchCommitSHA)) {
            checkoutBranchHelper(branchTrackedBlobs, currentTrackedBlobs);
        }
        /* clear the addstage and rmstage */
        clearStage();
        /* update the current branch in the head file */
        writeContents(join(GITLET_DIR, "HEAD"), branchFile.getPath());
    }

    /** Helper function for checkoutBranch.
     * @param branchTrackedBlobs the tracked blobs of the branch commit
     * @param currentTrackedBlobs the tracked blobs of the current commit
     * */
    private static void checkoutBranchHelper(TreeMap<String, File> branchTrackedBlobs,
                                             TreeMap<String, File> currentTrackedBlobs) {
        if (branchTrackedBlobs != null) {
            for (String fileName : branchTrackedBlobs.keySet()) {
                Blob b = readObject(branchTrackedBlobs.get(fileName), Blob.class);
                /* if the file is in the CWD and tracked by current commit, update it.
                 * if the file is in the CWD but not tracked by current commit,
                 * throw an exception.
                 * if the file is not in the CWD, create it. */
                if (join(CWD, fileName).exists()
                        && (currentTrackedBlobs == null
                        || !currentTrackedBlobs.containsKey(fileName))) {
                    EXCEPTION.untrackedFileException();
                } else if (join(CWD, fileName).exists()
                        && currentTrackedBlobs.containsKey(fileName)) {
                    writeContents(join(CWD, fileName), b.getFileContentAsString());
                } else {
                    try {
                        join(CWD, fileName).createNewFile();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    writeContents(join(CWD, fileName), b.getFileContentAsString());
                }
            }
        }
        /* iterate through the CWD */
        List<String> cwdFiles = plainFilenamesIn(CWD);
        if (cwdFiles != null) {
            for (String fileName : cwdFiles) {
                /* if the file is in the CWD but not tracked by branch commit, delete it. */
                if (branchTrackedBlobs == null || !branchTrackedBlobs.containsKey(fileName)) {
                    restrictedDelete(join(CWD, fileName));
                }
            }
        }
    }

    /** Clear the addstage and rmstage. */
    private static void clearStage() {
        /* clear the addstage and rmstage */
        if (ADD_DIR.listFiles() != null) {
            for (File addFile : ADD_DIR.listFiles()) {
                addFile.delete();
            }
        }
        if (RM_DIR.listFiles() != null) {
            for (File rmFile : RM_DIR.listFiles()) {
                rmFile.delete();
            }
        }
    }

    /** Checkout a file with the given name.
     * @param fileName the name of the file to be checked out
     * */
    public static void checkoutFile(String fileName) {
        String currentBranchPath = readContentsAsString(join(GITLET_DIR, "HEAD"));
        String currentCommitSHA = readContentsAsString(new File(currentBranchPath));
        Commit currentCommit = readObject(join(COMMIT_DIR, currentCommitSHA), Commit.class);
        checkoutCommitHelper(currentCommit, fileName);
    }

    /** Checkout a file with the given name in the given commit.
     * @param commitID the ID of the commit
     * @param fileName the name of the file to be checked out
     * */
    public static void checkoutCommit(String commitID, String fileName) {
        /* check if the commit exists, return the commit if it exists*/
        Commit commitToCheckOut = getCommit(commitID);
        checkoutCommitHelper(commitToCheckOut, fileName);
    }

    /** Helper function for checkoutCommit.
     * @param commitToCheckOut the commit to be checked out
     * @param fileName the name of the file to be checked out
     * */
    private static void checkoutCommitHelper(Commit commitToCheckOut, String fileName) {
        TreeMap<String, File> checkOutCommitBlobs = commitToCheckOut.getBlobs();
        /* check if the file is tracked by the commit */
        if (!checkOutCommitBlobs.containsKey(fileName)) {
            EXCEPTION.notExistFileInCommitException();
        }
        Blob b = readObject(checkOutCommitBlobs.get(fileName), Blob.class);
        /* if the file is in the CWD, update it.
         * if the file is not in the CWD, create it. */
        if (join(CWD, fileName).exists()) {
            writeContents(join(CWD, fileName), b.getFileContentAsString());
        } else {
            try {
                join(CWD, fileName).createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            writeContents(join(CWD, fileName), b.getFileContentAsString());
        }
    }

    /** Get the commit with the given ID.
     * @param commitID the ID of the commit
     * @return the commit with the given ID
     * */
    private static Commit getCommit(String commitID) {
        int lengthOfCommitID = commitID.length();
        List<String> prefixOfCommitIDs = plainFilenamesIn(COMMIT_DIR);
        List<String> rawCommitIDs = new ArrayList<>(prefixOfCommitIDs);
        prefixOfCommitIDs.replaceAll(s -> s.substring(0, lengthOfCommitID));
        if (!prefixOfCommitIDs.contains(commitID)) {
            EXCEPTION.notExistCommitIdException();
        }
        String rawCommitID = rawCommitIDs.get(prefixOfCommitIDs.indexOf(commitID));
        Commit commitToCheckOut = readObject(join(COMMIT_DIR, rawCommitID), Commit.class);
        return commitToCheckOut;
    }

    /** Create a new branch with the given name.
     * @param branchName the name of the branch to be created
     * */
    public static void branch(String branchName) {
        /* if the branch already exists, throw an exception */
        if (join(HEAD_DIR, branchName).exists()) {
            EXCEPTION.branchExistedException();
        } else {
            try {
                join(HEAD_DIR, branchName).createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        /* find the current commit and write it to the new branch */
        String currentCommitPath = readContentsAsString(join(GITLET_DIR, "HEAD"));
        String currentCommitSHA = readContentsAsString(new File(currentCommitPath));
        writeContents(join(HEAD_DIR, branchName), currentCommitSHA);
        /* create a new log for this branch and inherit the log of the current branch */
        try {
            join(LOG_DIR, branchName).createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String currentBranchLog = currentCommitPath.substring(
                currentCommitPath.lastIndexOf(File.separator) + 1);
        writeContents(join(LOG_DIR, branchName),
                readContentsAsString(join(LOG_DIR, currentBranchLog)));
    }

    /** Remove the branch with the given name.
     * @param branchName the name of the branch to be removed
     * */
    public static void rmBranch(String branchName) {
        /* if the branch does not exist, throw an exception */
        if (!join(HEAD_DIR, branchName).exists()) {
            EXCEPTION.notExistBranchNameException();
        }
        String currentBranchPath = readContentsAsString(join(GITLET_DIR, "HEAD"));
        String currentBranchName = currentBranchPath.substring(
                currentBranchPath.lastIndexOf(File.separator) + 1);
        /* if the branch is the current branch, throw an exception */
        if (currentBranchName.equals(branchName)) {
            EXCEPTION.cannotRemoveCurrentBranchException();
        }
        /* delete the branch */
        join(HEAD_DIR, branchName).delete();
    }

    /** Reset the current branch to the commit with the given ID.
     * @param commitID the ID of the commit to be reset to
     * */
    public static void reset(String commitID) {
        Commit commitToReset = getCommit(commitID);
        String shaOfCommitToReset = commitToReset.getSHA();
        String currentBranchPath = readContentsAsString(join(GITLET_DIR, "HEAD"));
        String shaOfCurrentCommit = readContentsAsString(new File(currentBranchPath));
        Commit currentCommit = getCommit(shaOfCurrentCommit);
        writeContents(new File(currentBranchPath), shaOfCommitToReset);
        TreeMap<String, File> blobsOfCommitToReset = commitToReset.getBlobs();
        TreeMap<String, File> blobsOfCurrentCommit = currentCommit.getBlobs();
        checkoutBranchHelper(blobsOfCommitToReset, blobsOfCurrentCommit);
        clearStage();
    }

    /** Merge the branch with the given name to the current branch.
     * @param branchName the name of the branch to be merged
     * */
    public static void merge(String branchName) {
        checkBasicMergeFailures(branchName);
        String currentBranchPath = readContentsAsString(join(GITLET_DIR, "HEAD"));
        String currentCommitSHA = readContentsAsString(new File(currentBranchPath));
        Commit currentCommit = readObject(join(COMMIT_DIR, currentCommitSHA), Commit.class);
        String branchCommitSHA = readContentsAsString(join(HEAD_DIR, branchName));
        Commit branchCommit = readObject(join(COMMIT_DIR, branchCommitSHA), Commit.class);
        /* get the split point of the current commit and the branch commit */
        Commit splitPoint = getSplitPoint(currentCommit, branchCommit);
        String splitPointSHA = splitPoint.getSHA();
        /* if the split point is the same as the branch commit, do nothing.
         * if the split point is the same as the current commit, checkout the branch.*/
        if (splitPointSHA.equals(branchCommitSHA)) {
            System.out.println("Given branch is an ancestor of the current branch.");
            return;
        } else if (splitPointSHA.equals(currentCommitSHA)) {
            checkoutBranch(branchName);
            System.out.println("Current branch fast-forwarded.");
            return;
        }
        TreeMap<String, File> splitPointBlobs = splitPoint.getBlobs();
        TreeMap<String, File> currentCommitBlobs = currentCommit.getBlobs();
        TreeMap<String, File> branchCommitBlobs = branchCommit.getBlobs();
        TreeMap<String, File> mergedBlobs =
                mergeBlobs(splitPointBlobs, currentCommitBlobs, branchCommitBlobs);
        createMergedFiles(mergedBlobs);
        String currentBranchName = currentBranchPath.substring(
                currentBranchPath.lastIndexOf(File.separator) + 1);
        String mergedMessage = "Merged " + branchName + " into " + currentBranchName + ".";
        List<Commit> parents = new ArrayList<>();
        parents.add(currentCommit);
        parents.add(branchCommit);
        Commit mergedCommit = new Commit(mergedMessage, parents, mergedBlobs);
        try {
            join(COMMIT_DIR, mergedCommit.getSHA()).createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        /* write the merged commit to the commit directory */
        writeObject(join(COMMIT_DIR, mergedCommit.getSHA()), mergedCommit);
        /* update the head commit of this branch and the log of this branch */
        writeContents(new File(currentBranchPath), mergedCommit.getSHA());
        String oldLog = readContentsAsString(join(LOG_DIR, currentBranchName));
        String newLog = mergedCommit.toString() + "\n" + oldLog;
        writeContents(join(LOG_DIR, currentBranchName), newLog);
    }

    private static void checkBasicMergeFailures(String branchName) {
        if (ADD_DIR.list().length != 0 || RM_DIR.list().length != 0) {
            EXCEPTION.uncommitedChangesException();
        }
        List<String> branches = plainFilenamesIn(HEAD_DIR);
        if (branches == null || !branches.contains(branchName)) {
            EXCEPTION.notExistBranchNameException();
        }
        String currentBranchPath = readContentsAsString(join(GITLET_DIR, "HEAD"));
        String currentBranchName = currentBranchPath.substring(
                currentBranchPath.lastIndexOf(File.separator) + 1);
        if (currentBranchName.equals(branchName)) {
            EXCEPTION.cannotMergeWithItselfException();
        }
    }

    private static Commit getSplitPoint(Commit currentCommit, Commit branchCommit) {
        Set<String> commitSHAOfCurrentBranch = new HashSet<>();

        // Add all the commit SHA of the current branch to the set
        Commit tempCurrent = currentCommit;
        while (tempCurrent != null) {
            commitSHAOfCurrentBranch.add(tempCurrent.getSHA());
            tempCurrent = tempCurrent.getParent();
        }

        // Find the split point
        while (branchCommit != null) {
            String commitSHAOfBranch = branchCommit.getSHA();
            if (commitSHAOfCurrentBranch.contains(commitSHAOfBranch)) {
                return branchCommit;
            }
            branchCommit = branchCommit.getParent();
        }

        return null; // If no split point is found
    }

    private static TreeMap<String, File> mergeBlobs(TreeMap<String, File> splitPointBlobs,
                                                    TreeMap<String, File> currentCommitBlobs,
                                                    TreeMap<String, File> branchCommitBlobs) {
        TreeMap<String, File> mergedBlobs = new TreeMap<>(currentCommitBlobs);

        if (splitPointBlobs != null) {
            mergeSplitPointBlobs(splitPointBlobs, currentCommitBlobs,
                    branchCommitBlobs, mergedBlobs);
        } else if (branchCommitBlobs != null) {
            mergeBranchBlobs(currentCommitBlobs, branchCommitBlobs, mergedBlobs);
        }

        return mergedBlobs;
    }

    private static void mergeSplitPointBlobs(TreeMap<String, File> splitPointBlobs,
                                             TreeMap<String, File> currentCommitBlobs,
                                             TreeMap<String, File> branchCommitBlobs,
                                             TreeMap<String, File> mergedBlobs) {
        for (String fileName : splitPointBlobs.keySet()) {
            boolean isBlobInBranch = branchCommitBlobs != null
                    && branchCommitBlobs.containsKey(fileName);
            boolean isBlobModifiedInBranch = !isBlobInBranch
                    || !branchCommitBlobs.get(fileName).equals(splitPointBlobs.get(fileName));
            boolean isBlobInCurrent = currentCommitBlobs != null
                    && currentCommitBlobs.containsKey(fileName);
            boolean isBlobModifiedInCurrent = !isBlobInCurrent
                    || !currentCommitBlobs.get(fileName).equals(splitPointBlobs.get(fileName));
            boolean onlyModifiedInBranch = isBlobModifiedInBranch && !isBlobModifiedInCurrent;
            boolean modifiedInBoth = isBlobModifiedInBranch && isBlobModifiedInCurrent;

            if (onlyModifiedInBranch) {
                updateMergedBlobs(mergedBlobs, isBlobInBranch, fileName, branchCommitBlobs);
            } else if (modifiedInBoth) {
                handleModifiedInBoth(mergedBlobs, fileName, isBlobInBranch,
                        isBlobInCurrent, branchCommitBlobs, currentCommitBlobs);
            }
        }

        handleUntrackedFiles(splitPointBlobs, currentCommitBlobs, branchCommitBlobs, mergedBlobs);
    }

    private static void updateMergedBlobs(TreeMap<String, File> mergedBlobs,
                                          boolean isBlobInBranch,
                                          String fileName,
                                          TreeMap<String, File> branchCommitBlobs) {
        if (!isBlobInBranch) {
            mergedBlobs.remove(fileName);
        } else {
            mergedBlobs.put(fileName, branchCommitBlobs.get(fileName));
        }
    }

    private static void handleModifiedInBoth(TreeMap<String, File> mergedBlobs,
                                             String fileName,
                                             boolean isBlobInBranch,
                                             boolean isBlobInCurrent,
                                             TreeMap<String, File> branchCommitBlobs,
                                             TreeMap<String, File> currentCommitBlobs) {
        boolean isBlobInBoth = isBlobInBranch && isBlobInCurrent;

        if (isBlobInBoth && !branchCommitBlobs.get(fileName).equals(
                currentCommitBlobs.get(fileName))) {
            handleMergeConflict(mergedBlobs, fileName, currentCommitBlobs, branchCommitBlobs);
        } else if (isBlobInCurrent && !isBlobInBranch) {
            handleMergeConflict(mergedBlobs, fileName, currentCommitBlobs, null);
        } else if (!isBlobInCurrent && isBlobInBranch) {
            handleMergeConflict(mergedBlobs, fileName, null, branchCommitBlobs);
        }
    }

    private static void handleMergeConflict(TreeMap<String, File> mergedBlobs,
                                            String fileName,
                                            TreeMap<String, File> currentCommitBlobs,
                                            TreeMap<String, File> branchCommitBlobs) {
        System.out.println("Encountered a merge conflict.");
        String contentsOfCurrentBlob = (currentCommitBlobs != null) ? readObject(
                currentCommitBlobs.get(fileName), Blob.class).getFileContentAsString() : "";
        String contentsOfBranchBlob = (branchCommitBlobs != null) ? readObject(
                branchCommitBlobs.get(fileName), Blob.class).getFileContentAsString() : "";
        mergedBlobs = writeConflictedBlob(fileName, contentsOfCurrentBlob,
                contentsOfBranchBlob, mergedBlobs);
    }

    private static void handleUntrackedFiles(TreeMap<String, File> splitPointBlobs,
                                             TreeMap<String, File> currentCommitBlobs,
                                             TreeMap<String, File> branchCommitBlobs,
                                             TreeMap<String, File> mergedBlobs) {
        if (branchCommitBlobs != null) {
            for (String fileName : branchCommitBlobs.keySet()) {
                if (!splitPointBlobs.containsKey(fileName)
                        && !currentCommitBlobs.containsKey(fileName)) {
                    mergedBlobs.put(fileName, branchCommitBlobs.get(fileName));
                }
                if (join(CWD, fileName).exists() && (currentCommitBlobs == null
                        || !currentCommitBlobs.containsKey(fileName))) {
                    EXCEPTION.untrackedFileException();
                }
            }
        }
    }

    private static void mergeBranchBlobs(TreeMap<String, File> currentCommitBlobs,
                                         TreeMap<String, File> branchCommitBlobs,
                                         TreeMap<String, File> mergedBlobs) {
        for (String fileName : branchCommitBlobs.keySet()) {
            if (!currentCommitBlobs.containsKey(fileName)) {
                mergedBlobs.put(fileName, branchCommitBlobs.get(fileName));
            } else if (!currentCommitBlobs.get(fileName).equals(branchCommitBlobs.get(fileName))) {
                handleMergeConflict(mergedBlobs, fileName, currentCommitBlobs, branchCommitBlobs);
            }
        }
    }


    /** Create the merged files in the CWD.
     * @param mergedBlobs the merged blobs
     * */
    private static void createMergedFiles(TreeMap<String, File> mergedBlobs) {
        List<String> cwdFiles = plainFilenamesIn(CWD);
        for (String fileName : cwdFiles) {
            restrictedDelete(join(CWD, fileName));
        }
        for (String fileName : mergedBlobs.keySet()) {
            Blob b = readObject(mergedBlobs.get(fileName), Blob.class);
            try {
                join(CWD, fileName).createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            writeContents(join(CWD, fileName), b.getFileContentAsString());
        }
    }

    /** Write the conflicted blob to the CWD.
     * @param fileName the name of the conflicted blob
     * @param contentsOfCurrentBlob the contents of the current blob
     * @param contentsOfBranchBlob the contents of the branch blob
     * @param mergedBlobs the merged blobs
     * @return the merged blobs
     * */
    private static TreeMap<String, File> writeConflictedBlob(String fileName,
                                                             String contentsOfCurrentBlob,
                                                             String contentsOfBranchBlob,
                                                             TreeMap<String, File> mergedBlobs) {
        /* 1. create a new file in BLOB_DIR with the name of the fileName
         * 2. write the conflict contents to the new file
         * 3. create a new blob with the fileName and conflict contents
         * 4. create a new file in BLOB_DIR with the name of the blob SHA
         * 5. write the blob to the new file
         * 6. delete the file with the name of fileName in BLOB_DIR
         * 7. put the fileName and the new blob in the mergedBlobs
         */
        try {
            join(BLOB_DIR, fileName).createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String conflictContents = "<<<<<<< HEAD\n"
                + contentsOfCurrentBlob
                + "=======\n"
                + contentsOfBranchBlob
                + ">>>>>>>\n";
        writeContents(join(BLOB_DIR, fileName), conflictContents);
        Blob conflictedBlob = new Blob(fileName, readContents(join(BLOB_DIR, fileName)));
        try {
            join(BLOB_DIR, conflictedBlob.getBlobSHA()).createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        writeObject(join(BLOB_DIR, conflictedBlob.getBlobSHA()), conflictedBlob);
        join(BLOB_DIR, fileName).delete();
        mergedBlobs.put(fileName, join(BLOB_DIR, conflictedBlob.getBlobSHA()));
        return mergedBlobs;
    }
}
