import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor4C implements Monitor {
    private int buff = 30;
    private final int maxPortion;
    private final int buffLimit;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition firstProdCond = lock.newCondition();
    private final Condition restProdCond = lock.newCondition();
    private final Condition firstConsCond = lock.newCondition();
    private final Condition restConsCond = lock.newCondition();
    private boolean firstProd = false;
    private boolean firstCons = false;
    private final Map<Integer, Integer> threadRestAwaitCounter = new HashMap<>();
    private final Map<Integer, Integer> threadAwaitCounter = new HashMap<>();
    private final Map<Integer, Integer> threadCpuCounter = new HashMap<>();

    public Monitor4C(int buffLimit, int maxPortion) {
        this.buffLimit = buffLimit;
        this.maxPortion = maxPortion;
    }

    public void produce(int portion, int threadId, long maxAwaitTime, long startTime) {
        lock.lock();

        while(firstProd) {
            try {
                threadRestAwaitCounter.merge(threadId, 1, Integer::sum);
                boolean timeLimitExceeded = !restProdCond.await(maxAwaitTime, TimeUnit.MILLISECONDS);
                if(timeLimitExceeded) {
                    lock.unlock();
                    return;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        while(buff + portion > buffLimit) {
            try {
                threadAwaitCounter.merge(threadId, 1, Integer::sum);
                firstProd = true;
                boolean timeLimitExceeded = !firstProdCond.await(maxAwaitTime, TimeUnit.MILLISECONDS);
                if(timeLimitExceeded) {
                    lock.unlock();
                    return;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        threadCpuCounter.merge(threadId, 1, Integer::sum);
        buff += portion;
        firstProd = false;
        restProdCond.signal();
        firstConsCond.signal();

        lock.unlock();
    }

    public void consume(int portion, int threadId, long maxAwaitTime, long startTime) {
        lock.lock();

        while(firstCons) {
            try {
                threadRestAwaitCounter.merge(threadId, 1, Integer::sum);
                boolean timeLimitExceeded = !restConsCond.await(maxAwaitTime, TimeUnit.MILLISECONDS);
                if(timeLimitExceeded) {
                    lock.unlock();
                    return;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        while(buff - portion < 0) {
            try {
                threadAwaitCounter.merge(threadId, 1, Integer::sum);
                firstCons = true;
                boolean timeLimitExceeded = !firstConsCond.await(maxAwaitTime, TimeUnit.MILLISECONDS);
                if(timeLimitExceeded) {
                    lock.unlock();
                    return;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        threadCpuCounter.merge(threadId, 1, Integer::sum);
        buff -= portion;
        firstCons = false;
        restConsCond.signal();
        firstProdCond.signal();

        lock.unlock();
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

    public Map<Integer, Integer> getThreadRestCounter() {
        return threadRestAwaitCounter;
    }
}
