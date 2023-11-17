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

    public Monitor(int buffLimit, int maxPortion) {
        this.buffLimit = buffLimit;
        this.maxPortion = maxPortion;
    }

    public void produce(int portion, int threadId) {
        prodLock.lock();
        lock.lock();

        while(buff + portion > buffLimit) {
            System.out.println("Thread id: " + threadId + " (producer) buff = " + buff + " portion = " + portion + " | await()");
            try {
                atWork.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("Thread id: " + threadId + " (producer) buff = " + buff + " portion = " + portion + " | produce");
        buff += portion;
        atWork.signal();

        lock.unlock();
        prodLock.unlock();
    }

    public void consume(int portion, int threadId) {
        consLock.lock();
        lock.lock();

        while(buff - portion < 0) {
            System.out.println("Thread id: " + threadId + " (consumer) buff = " + buff + " portion = " + portion + " | await()");
            try {
                atWork.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("Thread id: " + threadId + " (consumer) buff = " + buff + " portion = " + portion + " | consume");
        buff -= portion;
        atWork.signal();

        lock.unlock();
        consLock.unlock();
    }

    public int getMaxPortion() {
        return maxPortion;
    }

    public int getBuffLimit() {
        return buffLimit;
    }
}