//@author: j.n.magee
//updated: daniel.sykes 2013

package concurrency.message;

import java.awt.*;
import java.applet.*;
import concurrency.display.*;

public class SynchMsgDemo extends Applet {

    ThreadPanel tx;
    ThreadPanel rx;
    SlotCanvas senddisp;
    SlotCanvas recvdisp;

    public void init() {
        add(tx =new ThreadPanel("Sender",Color.blue,false));
        add(senddisp = new SlotCanvas("e",Color.cyan,1));
        add(recvdisp = new SlotCanvas("v",Color.cyan,1));
        add(rx =new ThreadPanel("Receiver",Color.blue,false));
		setBackground(Color.lightGray);
     }

    public void start() {
        Channel<Integer> chan = new Channel<Integer>();
        tx.start(new Sender(chan,senddisp));
        rx.start(new Receiver(chan,recvdisp));

    }

    public void stop() {
        tx.stop();
        rx.stop();
    }

}



/*******************SENDER************************/

class Sender implements Runnable {
  private Channel<Integer> chan;
  private SlotCanvas display;

  Sender(Channel<Integer> c, SlotCanvas d) {chan=c; display =d;}

  public void run() {
    try {
      int ei = 0;
      while(true) {
        display.enter(String.valueOf(ei));
        ThreadPanel.rotate(12);
        chan.send(new Integer(ei));
        display.leave(String.valueOf(ei));
        ei=(ei+1)%10;
        ThreadPanel.rotate(348);
      }
    } catch (InterruptedException e){}
  }
}


/*******************RECEIVER************************/

class Receiver implements Runnable {
  private Channel<Integer> chan;
  private SlotCanvas display;

  Receiver(Channel<Integer> c, SlotCanvas d) {chan=c; display =d;}

  public void run() {
    try {
      Integer v=null;
      while(true) {
        ThreadPanel.rotate(180);
        if (v!=null) display.leave(v.toString());
        v = chan.receive();
        display.enter(v.toString());
        ThreadPanel.rotate(180);
      }
    } catch (InterruptedException e){}
  }
}
