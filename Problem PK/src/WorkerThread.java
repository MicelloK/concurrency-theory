import java.lang.management.ManagementFactory;
import java.util.Random;

public abstract class WorkerThread extends Thread {
    private final int id;
    private final Monitor monitor;
    private final Random random;
    private final long threadLiveTime;
    private long threadCpuTime = -1;

    public WorkerThread(int id, Monitor monitor, long threadLiveTime) {
        this.id = id;
        this.monitor = monitor;
        this.threadLiveTime = threadLiveTime;
        random = new Random(id);
    }

    public void run() {
        long executionTime = 0;
        while(executionTime <= threadLiveTime) {
            int randPortion = random.nextInt(monitor.getMaxPortion());
            long startTime = System.currentTimeMillis();
            work(id, randPortion, monitor, threadLiveTime-executionTime, startTime);
            long endTime = System.currentTimeMillis();
            executionTime += endTime - startTime;
        }
        threadCpuTime = ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
    }

    public abstract void work(int id, int portion, Monitor monitor, long maxAwaitTime, long startTime);

    public long getThreadCpuTime() {
        return threadCpuTime;
    }

    public int getThreadId() {
        return id;
    }
}
