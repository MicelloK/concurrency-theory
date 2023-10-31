import java.util.LinkedList;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int m = 2;
        int n = 1;
        int w = 8;
        int max_portion = 5; // jeśli max_portion będzie większy niż połowa buffora, wystąpi zakleszczenie

        Monitor monitor = new Monitor(w, max_portion);
        LinkedList<Thread> threads = new LinkedList<>();

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