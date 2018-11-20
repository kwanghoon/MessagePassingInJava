//@author: j.n.magee
package concurrency.message;

import java.awt.*;
import java.applet.*;
import concurrency.display.*;

public class EntryDemo extends Applet {

    ThreadPanel clA,clB,sv;
    SlotCanvas clientAdisp, clientBdisp, serverdisp;

    public void init() {
        setLayout(new BorderLayout());
        Panel bot = new Panel();
        Panel top = new Panel();
        top.setLayout(new FlowLayout(FlowLayout.CENTER,80,5));
        top.setBackground(Color.cyan);
        bot.add(clA =new ThreadPanel("Client A",Color.magenta,false));
        bot.add(sv =new ThreadPanel("Server",Color.blue,true));
        bot.add(clB =new ThreadPanel("Client B",Color.yellow,false));
        top.add(clientAdisp = new SlotCanvas("req A",Color.cyan,1));
        top.add(serverdisp  = new SlotCanvas("v",Color.cyan,1));
        top.add(clientBdisp = new SlotCanvas("req B",Color.cyan,1));
        add("Center",top);
        add("South",bot);
		setBackground(Color.lightGray);
     }

    public void start() {
        Entry<String,String> entry = new Entry<String,String>();
        clA.start(new Client(entry,clientAdisp,"A"));
        clB.start(new Client(entry,clientBdisp,"B"));
        sv.start(new Server(entry,serverdisp));

    }

    public void stop() {
        clA.stop();
        clB.stop();
        sv.stop();
    }

}



/*******************CLIENT************************/

class Client implements Runnable {
  private Entry<String,String> entry;
  private SlotCanvas display;
  private String id;

  Client(Entry<String,String> e, SlotCanvas d, String s)
    {entry=e; display =d; id=s;}

  public void run() {
    try {
    while(true) {
      ThreadPanel.rotate(90);
      display.enter(id);
      String result = entry.call(id);
      display.leave(id); display.enter(result);
      ThreadPanel.rotate(90);
      display.leave(result);
      ThreadPanel.rotate(180);
    }
    } catch (InterruptedException e){}
  }
}


/*******************Server************************/

class Server implements Runnable {
  private Entry<String,String> entry;
  private SlotCanvas display;

  Server(Entry<String,String> e, SlotCanvas d)
    {entry=e; display =d;}

  public void run() {
    try {
    while(true) {
      while(!ThreadPanel.rotate());
      String request = entry.accept();
      display.enter(request);
      if (request.equals("A"))
         ThreadPanel.setSegmentColor(Color.magenta);
      else
         ThreadPanel.setSegmentColor(Color.yellow);
      while(ThreadPanel.rotate());
      display.leave(request);
      entry.reply("R");
    }
    } catch (InterruptedException e){}
  }
}
