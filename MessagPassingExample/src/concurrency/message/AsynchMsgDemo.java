//@author: j.n.magee
//updated: daniel.sykes 2013

package concurrency.message;

import java.awt.*;
import java.applet.*;
import concurrency.display.*;

public class AsynchMsgDemo extends Applet {

    ThreadPanel tx1,tx2,rx;
    SlotCanvas send1disp, send2disp, recvdisp;

    public void init() {
        setLayout(new BorderLayout());
        Panel bot = new Panel();
        Panel top = new Panel();
        top.setLayout(new FlowLayout(FlowLayout.CENTER,80,5));
        top.setBackground(Color.cyan);
        bot.add(tx1 =new ThreadPanel("Sender1",Color.blue,false));
        bot.add(rx =new ThreadPanel("Receiver",Color.blue,false));
        bot.add(tx2 =new ThreadPanel("Sender2",Color.blue,false));
        top.add(send1disp = new SlotCanvas("e1",Color.cyan,1));
        top.add(recvdisp  = new SlotCanvas("v",Color.cyan,1));
        top.add(send2disp = new SlotCanvas("e2",Color.cyan,1));
        add("Center",top);
        add("South",bot);
		setBackground(Color.lightGray);
     }

    public void start() {
        Port<Integer> port = new Port<Integer>();
        tx1.start(new Asender(port,send1disp));
        tx2.start(new Asender(port,send2disp));
        rx.start(new Areceiver(port,recvdisp));

    }

    public void stop() {
        tx1.stop();
        tx2.stop();
        rx.stop();
    }

}



/*******************SENDER************************/

class Asender implements Runnable {
  private Port<Integer> port;
  private SlotCanvas display;

  Asender(Port<Integer> p, SlotCanvas d) {port=p; display =d;}

  public void run() {
    try {
        int ei = 0;
        while(true) {
          display.enter(String.valueOf(ei));
          ThreadPanel.rotate(90);
          port.send(new Integer(ei));
          display.leave(String.valueOf(ei));
          ei=(ei+1)%10;
          ThreadPanel.rotate(270);
        }
    } catch (InterruptedException e){}
  }
}


/*******************RECEIVER************************/

class Areceiver implements Runnable {
  private Port<Integer> port;
  private SlotCanvas display;

  Areceiver(Port<Integer> p, SlotCanvas d) {port=p; display =d;}

  public void run() {
    try {
    Integer v=null;
    while(true) {
      ThreadPanel.rotate(45);
      if (v!=null) display.leave(v.toString());
      ThreadPanel.rotate(45);
      v = port.receive();
      display.enter(v.toString());
    }
    } catch (InterruptedException e){}
  }
}
