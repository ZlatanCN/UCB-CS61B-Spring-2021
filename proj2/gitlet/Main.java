package gitlet;

import java.io.IOException;
import java.util.Objects;

import static gitlet.Repository.*;
import static gitlet.Utils.*;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author TODO
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) throws IOException {
        if (args.length == 0) {
            EXCEPTION.noArgsException();
        }
        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                // TODO: handle the `init` command
                validateNumArgs("init", args, 1);
                init();
                break;
            case "add":
                // TODO: handle the `add [filename]` command
                hasInitialized();
                validateNumArgs("add", args, 2);
                add(args[1]);
                break;
            // TODO: FILL THE REST IN
            case "commit":
                hasInitialized();
                validateNumArgs("commit", args, 2);
                commit(args[1]);
                break;
            case "rm":
                hasInitialized();
                validateNumArgs("rm", args, 2);
                rm(args[1]);
                break;
            case "log":
                hasInitialized();
                validateNumArgs("log", args, 1);
                log();
                break;
            case "global-log":
                hasInitialized();
                validateNumArgs("global-log", args, 1);
                globalLog();
                break;
            case "find":
                hasInitialized();
                validateNumArgs("find", args, 2);
                find(args[1]);
                break;
            case "status":
                hasInitialized();
                validateNumArgs("status", args, 1);
                status();
                break;
            case "checkout":
                hasInitialized();
                validateNumArgs("checkout", args, 4);
                checkout(args);
                break;
            case "branch":
                hasInitialized();
                validateNumArgs("branch", args, 2);
                branch(args[1]);
                break;
            case "rm-branch":
                hasInitialized();
                validateNumArgs("rm-branch", args, 2);
                rmBranch(args[1]);
                break;
            case "reset":
                hasInitialized();
                validateNumArgs("reset", args, 2);
                reset(args[1]);
                break;
            case "merge":
                hasInitialized();
                validateNumArgs("merge", args, 2);
                merge(args[1]);
                break;
            default:
                EXCEPTION.notExistCommentsException();
        }
    }

    /** check the number of arguments.
     * @param cmd the command
     * @param args the arguments
     * @param n the number of arguments
     * */
    public static void validateNumArgs(String cmd, String[] args, int n) {
        if (cmd.equals("checkout")) {
            if (args.length > n || args.length < 2) {
                EXCEPTION.wrongCommentsException();
            }
            return;
        }
        if (cmd.equals("commit")) {
            if (args.length != n || args[1].isEmpty()) {
                EXCEPTION.wrongCommitCommentsException();
            }
        }
        if (args.length != n) {
            EXCEPTION.wrongCommentsException();
        }
    }

    /** check if the repository has been initialized. */
    public static void hasInitialized() {
        if (!GITLET_DIR.exists()) {
            EXCEPTION.notInitException();
        }
    }
}
