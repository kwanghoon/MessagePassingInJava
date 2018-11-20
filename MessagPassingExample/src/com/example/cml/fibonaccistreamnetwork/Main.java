package com.example.cml.fibonaccistreamnetwork;

import concurrency.message.Channel;

public class Main {

	public static void main(String[] args) {
		try {
			int N = 20;

			Channel<Integer> ch;

			ch = mkFibNetwork();
			
			for(int i=0; i<N; i++) {
				System.out.print(ch.receive() + " ");
			}
			System.out.println();
			
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static Channel<Integer> mkFibNetwork() throws InterruptedException {
		Channel<Integer> outCh = new Channel<Integer>();
		Channel<Integer> c1 = new Channel<Integer>();
		Channel<Integer> c2 = new Channel<Integer>();
		Channel<Integer> c3 = new Channel<Integer>();
		Channel<Integer> c4 = new Channel<Integer>();
		Channel<Integer> c5 = new Channel<Integer>();

		delay(SOME, 0, c4, c5);
		copy(c2, c3, c4);
		add(c3, c5, c1);
		copy(c1, c2, outCh);
		c1.send(1);
		
		return outCh;
	}
	
	public static void add(Channel<Integer> inCh1, Channel<Integer> inCh2, 
			Channel<Integer> outCh) {
		new Thread() {
			public void run() {
				while(true) {
					try {
						outCh.send(inCh1.receive() + inCh2.receive());
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
	}
	
	public static final int SOME = 1;
	public static final int NONE = 2;
	
	public static void delay (int _tag, Integer _x, Channel<Integer> inCh, Channel<Integer> outCh) 
			throws InterruptedException {
		new Thread() {
			public void run() {
				int tag = _tag;
				Integer x = _x;
				try {
					while(true) {
						if (tag == NONE) {
							tag = SOME;
							x = inCh.receive();
						}
						else if (tag == SOME) {
							outCh.send(x);
							tag = NONE;
							x = null;
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}
	
	public static void copy(Channel<Integer> inCh, Channel<Integer> outCh1, Channel<Integer> outCh2) {
		new Thread() {
			public void run() {
				try {
					while(true) {
						int x = inCh.receive();
						outCh1.send(x);
						outCh2.send(x);
					}
				}
				catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

}
