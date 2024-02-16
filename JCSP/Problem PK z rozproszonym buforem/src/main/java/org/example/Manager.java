package org.example;

import org.jcsp.lang.*;

import java.util.Arrays;
import java.util.LinkedList;

public class Manager implements CSProcess {
    private final One2OneChannelInt[] in;
    private final One2OneChannel[] out;
    private final LinkedList<Buffer> buffs;
    private final int prodNum;
    private final CSProcess[] workers;

    private int head = -1;
    private int tail = -1;


    public Manager(One2OneChannelInt[] in, One2OneChannel[] out, LinkedList<Buffer> buffs, int n, CSProcess[] workers) {
        this.in = in;
        this.out = out;
        this.buffs = buffs;
        this.prodNum = n;
        this.workers = workers;
    }

    public void run() {
        final Guard[] guards = new Guard[in.length];
        for (int i = 0; i < in.length; i++) {
            guards[i] = in[i].in();
        }
        final Alternative alt = new Alternative(guards);

        int iter = 0;
        while(true) {
            iter++;
            int idx = alt.select();
            if (idx < prodNum) {
                if (head < tail + buffs.size()) {
                    in[idx].in().read();
                    head++;

                    One2OneChannelInt chanIn = buffs.get(head % buffs.size()).getIn();
                    out[idx].out().write(chanIn);
                }
                // else
            } else if (tail < head) {
                in[idx].in().read();
                tail++;

                One2OneChannelInt chanReq = buffs.get(tail % buffs.size()).getReq();
                One2OneChannelInt chanOut = buffs.get(tail % buffs.size()).getOut();
                One2OneChannelInt[] chans = {chanReq, chanOut};
                out[idx].out().write(chans);
            }

            // czas na trochę okropnego kodu
            if(iter % 100000 == 0) {
                int[] workers_task_count = new int[workers.length];
                for(int i = 0; i < workers.length; i++) {
                    if(i < prodNum) {
                        workers_task_count[i] = ((Producer)workers[i]).getTask_count();
                    } else {
                        workers_task_count[i] = ((Consumer)workers[i]).getTask_count();
                    }
                }

                System.out.println(Arrays.toString(workers_task_count));

                // Oblicz średnią
                double mean = 0;
                for (double value : workers_task_count) {
                    mean += value;
                }
                mean /= workers_task_count.length;

                double sumSquaredDiff = 0;
                for (double value : workers_task_count) {
                    double diff = value - mean;
                    sumSquaredDiff += diff * diff;
                }

                double variance = sumSquaredDiff / workers_task_count.length;
                double dev = Math.sqrt(variance);
                System.out.println("Iter: " + iter + " Mean: " + mean + " Deviation: " + dev);
            }
        }
    }

}
