package org.example;

import org.jcsp.lang.*;

import java.util.LinkedList;

public final class Main {
    public static void main(String[] args) {
        int n = 5; // prod
        int m = 4; // cons
        int w = 3; // buff

        CSProcess[] procList = new CSProcess[n + m + w + 1];

        LinkedList<Buffer> buffs = new LinkedList<>();

        One2OneChannelInt[] workerOut = new One2OneChannelInt[n + m];
        One2OneChannel[] workerIn = new One2OneChannel[n + m];
        CSProcess[] workers = new CSProcess[n + m]; // for test


        for (int i = 0; i < n; i++) {
            workerOut[i] = Channel.one2oneInt();
            workerIn[i] = Channel.one2one();
            Producer prod = new Producer(i, workerOut[i], workerIn[i]);
            procList[i] = prod;
            workers[i] = prod; // for test
        }
        for (int i = 0; i < m; i++) {
            workerOut[n + i] = Channel.one2oneInt();
            workerIn[n + i] = Channel.one2one();
            Consumer cons = new Consumer(n + i, workerOut[n + i], workerIn[n + i]);
            procList[n + i] = cons;
            workers[n + i] = cons; // for test
        }
        for (int i = 0; i < w; i++) {
            Buffer buff = new Buffer(Channel.one2oneInt(), Channel.one2oneInt(), Channel.one2oneInt());
            buffs.add(buff);
            procList[n + m + i] = buff;
        }
        procList[n + m + w] = new Manager(workerOut, workerIn, buffs, n, workers);

        Parallel par = new Parallel(procList);
        par.run();
    }
}