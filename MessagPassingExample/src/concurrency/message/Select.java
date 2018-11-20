/*
@author  j.n.magee
*/
package concurrency.message;

import java.util.*;

/*********************Select*****************************/
// implements choice
class Select {
    ArrayList<Selectable> list = new ArrayList<Selectable>(2);

    public void add(Selectable s) {
        list.add(s);
        s.setSelect(this);
    }

    private void clearAll() {
        for (Selectable s:list) {
           s.clearOpen();
        }
    }

    private void openAll() {
        for (Selectable s:list) {
             if (s.testGuard()) s.setOpen();
        }
    }

    private int testAll() {
        int i = 1;
        for (Selectable s:list){
           if (s.testReady() && s.testGuard()) return i;
		   ++i;
        }
        return 0;
    }

    public synchronized int choose() throws InterruptedException {
        int readyIndex = 0;
        while (readyIndex==0) {
            readyIndex=testAll();
            if (readyIndex==0) {
                openAll();
                wait();
                clearAll();
            }
        }
        return readyIndex;
    }
}




