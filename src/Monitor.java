import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
    private int buff = 30;
    private final int maxPortion;
    private final int buffLimit;
    private final ReentrantLock lock = new ReentrantLock();
    private final ReentrantLock prodLock = new ReentrantLock();
    private final ReentrantLock consLock = new ReentrantLock();
    private final Condition atWork = lock.newCondition();
    private final Map<Integer, Long> threadsLockTimes = new HashMap<>();
    private final Map<Integer, Long> threadsCPUTimes = new HashMap<>();

    public Monitor(int buffLimit, int maxPortion) {
        this.buffLimit = buffLimit;
        this.maxPortion = maxPortion;
    }

    public void produce(int portion, int threadId) {
        prodLock.lock();
        lock.lock();
        long startTime = System.currentTimeMillis();

        while(buff + portion > buffLimit) {
//            System.out.println("Thread id: " + threadId + " (producer) buff = " + buff + " portion = " + portion + " | await()");
            try {
                atWork.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        long startCpuTime = System.currentTimeMillis();

//        if(threadId == 1000) System.out.println("Thread id: " + threadId + " (producer) buff = " + buff + " portion = " + portion + " | produce");
        buff += portion;
        atWork.signal();

        long endTime = System.currentTimeMillis();

        long lockTime = endTime - startTime;
        long cpuTime = endTime - startCpuTime;
        threadsLockTimes.merge(threadId, lockTime, Long::sum);
        threadsCPUTimes.merge(threadId, cpuTime, Long::sum);
        lock.unlock();
        prodLock.unlock();
    }

    public void consume(int portion, int threadId) {
        consLock.lock();
        lock.lock();

        while(buff - portion < 0) {
//            System.out.println("Thread id: " + threadId + " (consumer) buff = " + buff + " portion = " + portion + " | await()");
            try {
                atWork.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        buff -= portion;
        atWork.signal();

        lock.unlock();
        consLock.unlock();
    }

    public int getMaxPortion() {
        return maxPortion;
    }

    public Map<Integer, Long> getThreadsLockTimes() {
        return threadsLockTimes;
    }

    public Map<Integer, Long> getThreadsCPUTimes() {
        return threadsCPUTimes;
    }
}
