public class Consumer extends WorkerThread {
    public Consumer(int id, Monitor monitor, long threadLiveTime) {
        super(id, monitor, threadLiveTime);
    }

    @Override
    public void work(int id, int portion, Monitor monitor, long maxAwaitTime, long startTime) {
        monitor.consume(portion, id, maxAwaitTime, startTime);
    }
}