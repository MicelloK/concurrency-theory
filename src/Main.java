import java.util.LinkedList;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        int m = 5;
        int n = 5;
        int w = 100;
        int max_portion = 50; // jeśli max_portion będzie większy niż połowa buffora, wystąpi zakleszczenie
        long threadLifeTime = 1000; //ms
        Measurement measurement = new Measurement();

        // zmieniająca się liczba wątków:
        for(int i = 1; i <= 10; i++) {
            Monitor3L monitor = new Monitor3L(w, max_portion);
            measurement.setParams(monitor, i, i, threadLifeTime, "Thread3L.txt");
            measurement.perform();
        }
        for(int i = 1; i <= 10; i++) {
            Monitor4C monitor = new Monitor4C(w, max_portion);
            measurement.setParams(monitor, i, i, threadLifeTime, "Thread4C.txt");
            measurement.perform();
        }

        // zmieniająca się wielkość buffora
        for(int i = 50; i < 1000; i+=100) {
            Monitor3L monitor = new Monitor3L(i, i/2);
            measurement.setParams(monitor, m, n, threadLifeTime, "Buff3L.txt");
            measurement.perform();
        }
        for(int i = 50; i < 1000; i+=100) {
            Monitor4C monitor = new Monitor4C(i, i/2);
            measurement.setParams(monitor, m, n, threadLifeTime, "Buff4C.txt");
            measurement.perform();
        }

        // zmianiająca się wielkość buffora ale portion nie
        for(int i = 50; i < 1000; i+=100) {
            Monitor3L monitor = new Monitor3L(i, 25);
            measurement.setParams(monitor, m, n, threadLifeTime, "FixedPor3L.txt");
            measurement.perform();
        }
        for(int i = 50; i < 1000; i+=100) {
            Monitor4C monitor = new Monitor4C(i, 25);
            measurement.setParams(monitor, m, n, threadLifeTime, "FixedPor4C.txt");
            measurement.perform();
        }

        //zmieniająca się max portion
        for(int i = 50; i < 500; i+=50) {
            Monitor3L monitor = new Monitor3L(1000, i);
            measurement.setParams(monitor, m, n, threadLifeTime, "Por3L.txt");
            measurement.perform();
        }
        for(int i = 50; i < 500; i+=50) {
            Monitor4C monitor = new Monitor4C(1000, i);
            measurement.setParams(monitor, m, n, threadLifeTime, "Por4C.txt");
            measurement.perform();
        }

        //zmianiający się threadLifeTime
        for(int i = 500; i < 11000; i += 1000) {
            Monitor3L monitor = new Monitor3L(w, max_portion);
            measurement.setParams(monitor, m, n, i, "Tlt3L.txt");
            measurement.perform();
        }
        for(int i = 500; i < 11000; i += 1000) {
            Monitor4C monitor = new Monitor4C(w, max_portion);
            measurement.setParams(monitor, m, n, i, "Tlt4C.txt");
            measurement.perform();
        }

        //różna ilość producentów konsumentów
        for(int i = 1; i < 11; i++) {
            Monitor3L monitor = new Monitor3L(w, max_portion);
            measurement.setParams(monitor, 1, i, threadLifeTime, "Prod3L.txt");
            measurement.perform();
        }
        for(int i = 1; i < 11; i++) {
            Monitor4C monitor = new Monitor4C(w, max_portion);
            measurement.setParams(monitor, 1, i, threadLifeTime, "Prod4C.txt");
            measurement.perform();
        }


    }
}