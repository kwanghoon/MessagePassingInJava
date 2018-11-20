package com.example.cml.cell;

import concurrency.message.Channel;

public class Cell<T> {
	private Channel<Request<T>> reqCh;
	private Channel<Request<T>> replyCh;
	private Thread t;
	
	public Cell(T x) {
		this.reqCh = new Channel<Request<T>>();
		this.replyCh = new Channel<Request<T>>();
		
		t = new Thread() {
			public void run() {
				T y = x;
				while (true) {
					try {
						Request<T> z = Cell.this.reqCh.receive();
						if (z.getTag() == Request.GET) {
							Cell.this.replyCh.send(new Request<T>(Request.PUT,y));
						}
						else if (z.getTag() == Request.PUT) {
							y = z.getA();
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		t.start();
	}
	
	public T get() {
		try {
			this.reqCh.send(new Request<T>(Request.GET, null));
			Request<T> z = this.replyCh.receive();
			return z.getA();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	 public void put(T x) {
		 try {
			this.reqCh.send(new Request<T>(Request.PUT, x));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	 }
}
