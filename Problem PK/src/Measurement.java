import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Measurement {
    private Monitor monitor;
    private final LinkedList<WorkerThread> threads = new LinkedList<>();
    private String name;
    private Map<Integer, Integer> threadAwaitCounter;
    private Map<Integer, Integer> threadCpuCounter;
    private final Map<Integer, Long> threadCPUTimes = new HashMap<>();;
    private long threadLifeTime;

    public void setParams(Monitor monitor, int m, int n, long threadLifeTime, String measurementName) {
        this.monitor = monitor;
        this.name = measurementName;
        this.threadLifeTime = threadLifeTime;
        resetThreads(m, n, threadLifeTime);
    }

    private void resetThreads(int m, int n, long threadLifeTime) {
        threads.clear();
        for (int i = 0; i < m; i++) {
            threads.add(new Producer(i + 1, monitor, threadLifeTime));
        }

        for (int i = 0; i < n; i++) {
            threads.add(new Consumer(m + i + 1, monitor, threadLifeTime));
        }
    }

    public void perform() {
        for (WorkerThread thread : threads) {
            thread.start();
        }
        for (WorkerThread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        for (WorkerThread thread : threads) {
            threadCPUTimes.put(thread.getThreadId(), thread.getThreadCpuTime());
        }

        threadAwaitCounter = monitor.getThreadAwaitCounter();
        threadCpuCounter = monitor.getThreadCpuCounter();

        writeData(name);
    }

    private void writeData(String filePath) {
        int cpuCounter = 0;
        int awaitCounter = 0;
        long cpuTime = 0;
        int restCond = 0;

        for (Map.Entry<Integer, Integer> entry : threadAwaitCounter.entrySet()) {
            awaitCounter += entry.getValue();
        }
        for (Map.Entry<Integer, Integer> entry : threadCpuCounter.entrySet()) {
             cpuCounter += entry.getValue();
        }
        for (Map.Entry<Integer, Long> entry : threadCPUTimes.entrySet()) {
            cpuTime += entry.getValue();
        }
        if(monitor instanceof Monitor4C) {
            for(Map.Entry<Integer, Integer> entry : ((Monitor4C) monitor).getThreadRestCounter().entrySet()) {
                restCond += entry.getValue();
            }
        }

        try {
            FileWriter writer = new FileWriter("res/"+filePath, true);

            String line = threadLifeTime + " " + cpuTime + " " + cpuCounter + " " + awaitCounter;
            if (monitor instanceof Monitor4C) {
                line += " " + restCond + "\n";
            } else {
                line += "\n";
            }
            writer.write(line);

            writer.flush();
            writer.close();
            System.out.println("Saved... " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
