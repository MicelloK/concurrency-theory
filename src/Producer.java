import java.util.Random;

public class Producer extends Thread {
    private final int id;
    private final Monitor monitor;
    private final Random random = new Random();

    public Producer(int id, Monitor monitor) {
        this.id = id;
        this.monitor = monitor;
    }

    public void run() {
        while (true) {
            int randPortion = random.nextInt(monitor.getMaxPortion()) + 1;
            monitor.produce(randPortion, id);
        }
    }
}
