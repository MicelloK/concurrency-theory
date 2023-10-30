public class Consumer extends Thread {
    private final int id;
    private final Monitor monitor;

    public Consumer(int id, Monitor monitor) {
        this.id = id;
        this.monitor = monitor;
    }

    public void run() {
        while (true) {
            System.out.println("Thread id: " + id + " buff = " + monitor.getBuff());
            monitor.consume();
        }
    }
}
