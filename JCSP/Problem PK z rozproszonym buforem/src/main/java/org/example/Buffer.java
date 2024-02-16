package org.example;

import org.jcsp.lang.Alternative;
import org.jcsp.lang.CSProcess;
import org.jcsp.lang.Guard;
import org.jcsp.lang.One2OneChannelInt;

public class Buffer implements CSProcess {
    private final One2OneChannelInt in;
    private final One2OneChannelInt req;
    private final One2OneChannelInt out;

    private int buff = 0;

    public Buffer(One2OneChannelInt in, One2OneChannelInt req, One2OneChannelInt out) {
        this.in = in;
        this.req = req;
        this.out = out;
    }

    public One2OneChannelInt getIn() {
        return in;
    }

    public One2OneChannelInt getOut() {
        return out;
    }

    public One2OneChannelInt getReq() {
        return req;
    }

    @Override
    public void run() {
        final Guard[] guards = {in.in(), req.in()};
        final Alternative alt = new Alternative(guards);

        while(true) {
            int idx = alt.select();
            switch (idx) {
                case 0:
                    int item = in.in().read();
                    buff += item;
                    break;
                case 1:
                    req.in().read();
                    if (buff > 0) {
                        out.out().write(1);
                        buff -= 1;
                    } else {
                        System.out.println("Empty buffer!");
                        out.out().write(0);
                    }
                    break;
            }
        }
    }
}
