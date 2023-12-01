import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int m = 10;
        int n = 10;
        int w = 100;
        int max_portion = 5; // jeśli max_portion będzie większy niż połowa buffora, wystąpi zakleszczenie
        long threadLiveTime = 3 * 1000; //ms

        Monitor monitor = new Monitor(w, max_portion);
        LinkedList<Thread> threads = new LinkedList<>();

        for (int i = 0; i < m; i++) {
            threads.add(new Producer(i + 1, monitor, threadLiveTime));
        }

        for (int i = 0; i < n; i++) {
            threads.add(new Consumer(m + i + 1, monitor, threadLiveTime));
        }

//        threads.add(new EvilProducer(1000, monitor));

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }

        Map<Integer, Long> threadTimes = monitor.getThreadsLockTimes();
        Map<Integer, Long> threadCPUTimes = monitor.getThreadsCPUTimes();
        System.out.println(threadTimes);
        System.out.println(threadCPUTimes);
    }
}