package top.fols.atri.thread;

public class Threads {
    public FixedThreadPool fixed_pool(int size) { return new FixedThreadPool().setMaxRunningCount(size); }
}
