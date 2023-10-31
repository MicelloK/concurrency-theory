public class Monitor {
    private int buff = 0;
    private final int maxPortion;
    private final int buffLimit;

    public Monitor(int buffLimit, int maxPortion) {
        this.buffLimit = buffLimit;
        this.maxPortion = maxPortion;
    }

    public synchronized void produce(int portion, int threadId) {
        while (buff + portion > buffLimit) {
            System.out.println("Thread id: " + threadId + " (producer) buff = " + buff + " portion = " + portion + " | wait()");
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("Thread id: " + threadId + " (producer) buff = " + buff + " portion = " + portion + " | produce");
        buff += portion;
        notifyAll();
    }

    public synchronized void consume(int portion, int threadId) {
        while (buff - portion < 0) {
            System.out.println("Thread id: " + threadId + " (consumer) buff = " + buff + " portion = " + portion + " | wait()");
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        System.out.println("Thread id: " + threadId + " (consumer) buff = " + buff + " portion = " + portion + " | consume");
        buff -= portion;
        notifyAll();
    }

    public int getMaxPortion() {
        return maxPortion;
    }
}
