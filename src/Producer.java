public class Producer extends Thread {
    private final Monitor monitor;

    public Producer(Monitor monitor) {
        this.monitor = monitor;
    }

    public void run() {
        while (true) {
            monitor.produce();
            System.out.println("Produced!");
        }
    }
}
