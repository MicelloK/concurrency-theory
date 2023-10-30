public class Main {
    public static void main(String[] args) throws InterruptedException {
        Monitor monitor = new Monitor();
        Producer producer = new Producer(monitor);
        Consumer consumer = new Consumer(monitor);

        producer.start();
        consumer.start();

        producer.join();
        consumer.join();
    }
}