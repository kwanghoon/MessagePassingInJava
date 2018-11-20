//@author: j.n.magee
//updated: daniel.sykes 2013

package concurrency.message;

import java.awt.*;
import java.applet.*;
import concurrency.display.*;

public class SelectMsgDemo extends Applet {

    ThreadPanel arrivals;
    ThreadPanel departures;
    ThreadPanel carpark;
    StringCanvas cardisp;

    public void init() {
        setLayout(new BorderLayout());
        Panel p = new Panel();
        p.add(arrivals   =new ThreadPanel("Arrivals",Color.blue,false));
        p.add(carpark    =new ThreadPanel("CarPark",Color.blue,false));
        p.add(departures =new ThreadPanel("Departures",Color.blue,false));
        cardisp = new StringCanvas("Carpark State",Color.cyan);
        cardisp.setSize(470,100);
        add("Center",cardisp);
        add("South",p);
        setBackground(Color.lightGray);
     }

    public void start() {
        Channel<Signal> arrive = new Channel<Signal>();
        Channel<Signal> depart = new Channel<Signal>();
        arrivals.start(new MsgGate(arrive));
        departures.start(new MsgGate(depart));
        carpark.start(new MsgCarPark(arrive,depart,cardisp,4));
    }

    public void stop() {
        arrivals.stop();
        departures.stop();
        carpark.stop();
    }

}

class Signal  {
}


/********************************************************/

class MsgGate implements Runnable {

    private Channel<Signal> chan;
    private Signal signal = new Signal();

    public MsgGate (Channel<Signal> c) {chan=c;}

    public void run() {
        try {
          while(true)  {
            ThreadPanel.rotate(12);
            chan.send(signal);
            ThreadPanel.rotate(348);
          }
        } catch (InterruptedException e){}
    }
}

/********************************************************/

class MsgCarPark implements Runnable {
  private Channel<Signal> arrive,depart;
  private int spaces,N;
  private StringCanvas disp;

  public MsgCarPark(Channel<Signal> a, Channel<Signal> l, StringCanvas d,int capacity) {
    depart=l; arrive=a;
    N=spaces=capacity;
    disp = d;
    disp.setString("Cars: "+0+"   Spaces: "+spaces);
  }

  private void display(int s) throws InterruptedException {
    disp.setString("Cars: "+(N-s)+"   Spaces: "+s);
    ThreadPanel.rotate(348);
  }

  public void run() {
    try {
        Select sel = new Select();
        sel.add(depart);
        sel.add(arrive);
        while(true) {
          ThreadPanel.rotate(12);
          arrive.guard(spaces>0);
          depart.guard (spaces<N);
          switch (sel.choose()) {
          case 1:depart.receive();display(++spaces);
                 break;
          case 2:arrive.receive();display(--spaces);
                 break;
          }
        }
    } catch (InterruptedException e){}
  }
}
