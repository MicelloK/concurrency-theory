public class Consumer extends Thread {
    private final Monitor monitor;

    public Consumer(Monitor monitor) {
        this.monitor = monitor;
    }

    public void run() {
        while (true) {
            monitor.consume();
            System.out.println("Consumed!");
        }
    }
}
