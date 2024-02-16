package org.example;

import org.jcsp.lang.CSProcess;
import org.jcsp.lang.One2OneChannel;
import org.jcsp.lang.One2OneChannelInt;

public class Producer implements CSProcess {
    private final One2OneChannelInt manReq;
    private final One2OneChannel manIn;
    private final int id;
    private int task_count = 0;

    public Producer(int id, One2OneChannelInt out, One2OneChannel in) {
        this.id = id;
        manReq = out;
        manIn = in;
    }

    public void run() {
        One2OneChannelInt chan;
        while (true) {
            manReq.out().write(0);
            chan = (One2OneChannelInt) manIn.in().read();
            chan.out().write(1);

            System.out.println("ID: " + id + " Produced");
            task_count++;
        }
    }

    public int getTask_count() {
        return task_count;
    }
}
