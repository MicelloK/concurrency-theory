import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
    private int buff = 50;
    private final int maxPortion;
    private final int buffLimit;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition bufferReadyCondition = lock.newCondition();
    private final Condition bufferFullCondition = lock.newCondition();

    public Monitor(int buffLimit, int maxPortion) {
        this.buffLimit = buffLimit;
        this.maxPortion = maxPortion;
    }

    public void produce(int portion, int threadId) {
        lock.lock();
        while (buff + portion > buffLimit) {
            if(threadId == 1000) System.out.println("Thread id: " + threadId + " (producer) buff = " + buff + " portion = " + portion + " | wait()");
            try {
                bufferFullCondition.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        if(threadId == 1000) System.out.println("Thread id: " + threadId + " (producer) buff = " + buff + " portion = " + portion + " | produce");
        buff += portion;
        bufferReadyCondition.signal();
        lock.unlock();
    }

    public void consume(int portion, int threadId) {
        lock.lock();
        while (buff - portion < 0) {
            try {
                bufferReadyCondition.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        buff -= portion;
        bufferFullCondition.signal();
        lock.unlock();
    }

    public int getMaxPortion() {
        return maxPortion;
    }

    public int getBuffLimit() {
        return buffLimit;
    }
}
