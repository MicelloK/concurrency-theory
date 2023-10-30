public class Monitor {
    private int buff = 0;

    public synchronized void produce() {
        while (buff == 1) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        buff = 1;
        System.out.println("Produced!");
        notifyAll();
    }

    public synchronized void consume() {
        while (buff == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        buff = 0;
        System.out.println("Consumed!");
        notifyAll();
    }

    public int getBuff() {
        return buff;
    }
}
