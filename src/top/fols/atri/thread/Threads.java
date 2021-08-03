package top.fols.atri.thread;

public class Threads {
    public static FixedThreadPool fixed_pool(int size) { return new FixedThreadPool().setMaxRunningCount(size); }
}
