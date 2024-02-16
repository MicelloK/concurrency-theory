import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor3L implements Monitor {
    private int buff = 30;
    private final int maxPortion;
    private final int buffLimit;
    private final ReentrantLock lock = new ReentrantLock();
    private final ReentrantLock prodLock = new ReentrantLock();
    private final ReentrantLock consLock = new ReentrantLock();
    private final Condition atWork = lock.newCondition();
    private final Map<Integer, Integer> threadAwaitCounter = new HashMap<>();
    private final Map<Integer, Integer> threadCpuCounter = new HashMap<>();

    public Monitor3L(int buffLimit, int maxPortion) {
        this.buffLimit = buffLimit;
        this.maxPortion = maxPortion;
    }

    public void produce(int portion, int threadId, long maxAwaitTime, long startTime) {
        prodLock.lock();
        lock.lock();

        while(buff + portion > buffLimit) {
            try {
                threadAwaitCounter.merge(threadId, 1, Integer::sum);
                boolean timeLimitExceeded = !atWork.await(maxAwaitTime, TimeUnit.MILLISECONDS);
                if(timeLimitExceeded) {
                    lock.unlock();
                    prodLock.unlock();
                    return;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        threadCpuCounter.merge(threadId, 1, Integer::sum);
        buff += portion;
        atWork.signal();

        lock.unlock();
        prodLock.unlock();
    }

    public void consume(int portion, int threadId, long maxAwaitTime, long startTime) {
        consLock.lock();
        lock.lock();

        while(buff - portion < 0) {
            try {
                threadAwaitCounter.merge(threadId, 1, Integer::sum);
                boolean timeLimitExceeded = !atWork.await(maxAwaitTime, TimeUnit.MILLISECONDS);
                if(timeLimitExceeded) {
                    lock.unlock();
                    consLock.unlock();
                    return;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        threadCpuCounter.merge(threadId, 1, Integer::sum);
        buff -= portion;
        atWork.signal();

        lock.unlock();
        consLock.unlock();
    }

    public int getMaxPortion() {
        return maxPortion;
    }

    @Override
    public Map<Integer, Integer> getThreadAwaitCounter() {
        return threadAwaitCounter;
    }

    @Override
    public Map<Integer, Integer> getThreadCpuCounter() {
        return threadCpuCounter;
    }

}
