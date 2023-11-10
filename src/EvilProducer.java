import java.util.Random;

public class EvilProducer extends Thread {
    private final int id;
    private final Monitor monitor;

    public EvilProducer(int id, Monitor monitor) {
        this.id = id;
        this.monitor = monitor;
    }

    public void run() {
        while (true) {
            monitor.produce(50, id);
        }
    }
}
