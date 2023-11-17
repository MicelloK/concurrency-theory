import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor {
    private int buff = 30;
    private final int maxPortion;
    private final int buffLimit;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition firstProducerCondition = lock.newCondition();
    private final Condition restProducersCondition = lock.newCondition();
    private final Condition firstConsumerCondition = lock.newCondition();
    private final Condition restConsumersCondition = lock.newCondition();
    private boolean firstProducer = false;
    private boolean firstConsumer = false;

    public Monitor(int buffLimit, int maxPortion) {
        this.buffLimit = buffLimit;
        this.maxPortion = maxPortion;
    }

    public void produce(int portion, int threadId) {
        lock.lock();
        while (firstProducer) {
            if(threadId == 1000) System.out.println("Thread id: " + threadId + " (producer) buff = " + buff + " portion = " + portion + " | wait()");
            try {
                restProducersCondition.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        while (buff + portion > buffLimit) {
            if(threadId == 1000) System.out.println("Thread id: " + threadId + " (producer) buff = " + buff + " portion = " + portion + " | firstProducer wait()");
            try {
                firstProducer = true;
                firstProducerCondition.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }


        if(threadId == 1000) System.out.println("Thread id: " + threadId + " (producer) buff = " + buff + " portion = " + portion + " | produce");
        buff += portion;
        firstProducer = false;
        restProducersCondition.signal();
        firstConsumerCondition.signal();
        lock.unlock();
    }

    public void consume(int portion, int threadId) {
        lock.lock();
        while (firstConsumer) {
            try {
                restConsumersCondition.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        while (buff - portion < 0) {
            try {
                firstConsumer = true;
                firstConsumerCondition.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        buff -= portion;
//        System.out.println("consumed!");
        firstConsumer = false;
        restConsumersCondition.signal();
        firstProducerCondition.signal();
        lock.unlock();
    }

    public int getMaxPortion() {
        return maxPortion;
    }

    public int getBuffLimit() {
        return buffLimit;
    }
}

// haswatiers nie interesuje nas czy ktoś jest na await (obserwujemy coś innego) w tym czasie inne wątki mogą wchodzić