public class Producer extends WorkerThread {
    public Producer(int id, Monitor monitor, long threadLiveTime) {
        super(id, monitor, threadLiveTime);
    }

    @Override
    public void work(int id, int portion, Monitor monitor, long maxAwaitTime, long startTime) {
        monitor.produce(portion, id, maxAwaitTime, startTime);
    }
}
