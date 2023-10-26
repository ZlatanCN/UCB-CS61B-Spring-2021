package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeSLList {
    private static int maxN = 128000;
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeGetLast();
    }

    public static void timeGetLast() {
        // TODO: YOUR CODE HERE
        int ops = 10000;
        int nowN = 1000;
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCount = new AList<>();
        while (nowN <= maxN) {
            Ns.addLast(nowN);
            SLList temp = new SLList<>();
            for (int i = 0; i < nowN; i++) {
                temp.addFirst(i);
            }
            Stopwatch sw = new Stopwatch();
            for (int j = 0; j < ops; j++) {
                Object tempValue = temp.getLast();
            }
            double timeInSeconds = sw.elapsedTime();
            times.addLast(timeInSeconds);
            opCount.addLast(ops);
            nowN *= 2;
        }
        printTimingTable(Ns, times, opCount);
    }

}
