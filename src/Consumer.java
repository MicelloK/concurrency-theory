import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Consumer extends Thread {
    private final int id;
    private final Monitor monitor;
    private final Random random = new Random();
    private final long threadLiveTime;

    public Consumer(int id, Monitor monitor, long threadLiveTime) {
        this.id = id;
        this.monitor = monitor;
        this.threadLiveTime = threadLiveTime;
    }

    public void run() {
//        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
//
//        executorService.schedule(executorService::shutdown, threadLiveTime, TimeUnit.MILLISECONDS);
//
//        while(!Thread.interrupted()) {
//            int randPortion = random.nextInt(monitor.getMaxPortion()) + 1;
//            monitor.consume(randPortion, id);
//        }
//
//        executorService.shutdown();
//        System.out.println("Consumer - shutdown");


        long startTime = System.currentTimeMillis();
        long currentTime = startTime;
        while (true) {
            if (currentTime - startTime > threadLiveTime) {
                return;
            }
            int randPortion = random.nextInt(monitor.getMaxPortion()) + 1;
            monitor.consume(randPortion, id);
            currentTime = System.currentTimeMillis();
        }
    }
}
