public class Producer extends Thread {
    private final int id;
    private final Monitor monitor;

    public Producer(int id, Monitor monitor) {
        this.id = id;
        this.monitor = monitor;
    }

    public void run() {
        while (true) {
            System.out.println("Thread id: " + id + " buff = " + monitor.getBuff());
            monitor.produce();
        }
    }
}
