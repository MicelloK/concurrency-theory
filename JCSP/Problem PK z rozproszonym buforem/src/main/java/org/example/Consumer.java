package org.example;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannel;
import org.jcsp.lang.One2OneChannelInt;

public class Consumer implements CSProcess {
    private final One2OneChannel in;
    private final One2OneChannelInt req;
    private final int id;
    private int task_count = 0;

    public Consumer(int id, One2OneChannelInt req, One2OneChannel in) {
        this.req = req;
        this.in = in;
        this.id = id;
    }

    @Override
    public void run() {
        One2OneChannelInt[] chan;
        while(true) {
            req.out().write(0); //req
            System.out.println("ID: " + id + " man request accept");

            chan = (One2OneChannelInt[]) in.in().read();
            chan[0].out().write(0);
            int por = chan[1].in().read();

            System.out.println("ID: " + id + " Consumed " + por);
            task_count++;
        }
    }

    public int getTask_count() {
        return task_count;
    }
}
