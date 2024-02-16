package pl.edu.agh.macwozni.dmeshparallel;

import pl.edu.agh.macwozni.dmeshparallel.myProductions.*;
import pl.edu.agh.macwozni.dmeshparallel.mesh.Vertex;
import pl.edu.agh.macwozni.dmeshparallel.mesh.GraphDrawer;
import pl.edu.agh.macwozni.dmeshparallel.parallelism.BlockRunner;
import pl.edu.agh.macwozni.dmeshparallel.production.PDrawer;

public class Executor extends Thread {
    
    private final BlockRunner runner;
    
    public Executor(BlockRunner _runner){
        this.runner = _runner;
    }

    @Override
    public void run() {

        PDrawer drawer = new GraphDrawer();
        //axiom
        Vertex s = new Vertex(null, null, "S");

        //p1 
        P1 p1 = new P1(s, drawer);
        this.runner.addThread(p1);

        //start threads
        this.runner.startAll();

        //p2,p3
        P5 p5a = new P5(p1.getObj(), drawer);
        P3 p3 = new P3(p1.getObj().getRight(), drawer);
        this.runner.addThread(p5a);
        this.runner.addThread(p3);

        //start threads
        this.runner.startAll();

        //p5^2,p6^2
        P7 p7 = new P7(p3.getObj().getRight(), drawer);
        P6 p6 = new P6(p3.getObj(), drawer);
        this.runner.addThread(p7);
        this.runner.addThread(p6);

        //start threads
        this.runner.startAll();

        //done
        System.out.println("done");
        drawer.draw(p6.getObj());

    }
}
