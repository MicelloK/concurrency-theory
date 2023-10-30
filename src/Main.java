import java.util.LinkedList;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Monitor monitor = new Monitor();
        LinkedList<Thread> threads = new LinkedList<>();

        int m = 2;
        int n = 1;

        for (int i = 0; i < m; i++) {
            threads.add(new Producer(i + 1, monitor));
        }

        for (int i = 0; i < n; i++) {
            threads.add(new Consumer(m + i + 1, monitor));
        }

        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
            thread.join();
        }




    }
}