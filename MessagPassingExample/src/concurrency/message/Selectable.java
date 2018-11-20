package concurrency.message;



class Selectable {
    private boolean open = false;
    private int ready = 0;
    private Select inchoice = null;
    private boolean guard_ = true;

    void setSelect(Select s) {
        inchoice = s;
    }

    synchronized void block() throws InterruptedException {
          if (inchoice == null) {
            setOpen();
            while(ready==0) wait();
            clearOpen();
        }
    }

    synchronized void signal() {
        if (inchoice == null) {
            ++ready;
            if (open) notifyAll();
        } else {
            synchronized (inchoice) {
                ++ready;
                if (open) inchoice.notifyAll();
            }
        }
    }

    boolean testReady() {
        return ready>0;
    }

    synchronized void clearReady() {
        --ready;
    }

    void setOpen() {
        open=true;
    }

    void clearOpen() {
         open=false;
    }

    public void guard(boolean g) {
        guard_=g;
    }

    boolean testGuard(){
        return guard_;
    }
}
