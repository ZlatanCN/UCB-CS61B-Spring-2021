package gitlet;

/** General exception indicating a Gitlet error.  For fatal errors, the
 *  result of .getMessage() is the error message to be printed.
 *  @author P. N. Hilfinger
 */
class GitletException extends RuntimeException {


    /** A GitletException with no message. */
    GitletException() {
        super();
    }

    /** A GitletException MSG as its message. */
    GitletException(String msg) {
        super(msg);
    }

    public void noArgsException() {
        System.out.println("Please enter a command.");
        System.exit(0);
    }

    public void notExistCommentsException() {
        System.out.println("No command with that name exists.");
        System.exit(0);
    }

    public void wrongCommentsException() {
        System.out.println("Incorrect operands.");
        System.exit(0);
    }

    public void notInitException() {
        System.out.println("Not in an initialized Gitlet directory.");
        System.exit(0);
    }

    public void gitletExistedException() {
        System.out.println("A Gitlet version-control system "
                + "already exists in the current directory.");
        System.exit(0);
    }

    public void notExistFileException() {
        System.out.println("File does not exist.");
        System.exit(0);
    }

    public void wrongCommitCommentsException() {
        System.out.println("Please enter a commit message.");
        System.exit(0);
    }

    public void noChangesException() {
        System.out.println("No changes added to the commit.");
        System.exit(0);
    }

    public void noNeedToRemoveException() {
        System.out.println("No reason to remove the file.");
        System.exit(0);
    }

    public void notExistCommitMessageException() {
        System.out.println("Found no commit with that message.");
        System.exit(0);
    }

    public void notExistBranchException() {
        System.out.println("No such branch exists.");
        System.exit(0);
    }

    public void inCurrentBranchException() {
        System.out.println("No need to checkout the current branch.");
        System.exit(0);
    }

    public void untrackedFileException() {
        System.out.println("There is an untracked file in the way; "
                + "delete it, or add and commit it first.");
        System.exit(0);
    }

    public void notExistFileInCommitException() {
        System.out.println("File does not exist in that commit.");
        System.exit(0);
    }

    public void notExistCommitIdException() {
        System.out.println("No commit with that id exists.");
        System.exit(0);
    }

    public void branchExistedException() {
        System.out.println("A branch with that name already exists.");
        System.exit(0);
    }

    public void notExistBranchNameException() {
        System.out.println("A branch with that name does not exist.");
        System.exit(0);
    }

    public void cannotRemoveCurrentBranchException() {
        System.out.println("Cannot remove the current branch.");
        System.exit(0);
    }

    public void uncommitedChangesException() {
        System.out.println("You have uncommitted changes.");
        System.exit(0);
    }

    public void cannotMergeWithItselfException() {
        System.out.println("Cannot merge a branch with itself.");
        System.exit(0);
    }
}
