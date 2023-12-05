import java.util.Map;

public interface Monitor {
    void produce(int portion, int threadId, long maxAwaitTime, long startTime);
    void consume(int portion, int threadId, long maxAwaitTime, long startTime);
    int getMaxPortion();
    Map<Integer, Integer> getThreadAwaitCounter();
    Map<Integer, Integer> getThreadCpuCounter();
}
