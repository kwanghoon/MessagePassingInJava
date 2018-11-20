package com.example.cml.natstream;

import concurrency.message.Channel;

public class Main {

	public static void main(String[] args) {
		Channel<Integer> ch = new Channel<Integer>();
		Thread t = new Thread() {
			public void run() {
				int i = 0;
				while (true) {
					try {
						ch.send(i);
						i = i + 1;
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		t.start();
		
		try {
			for (int i = 0; i < 100; i++) {
				System.out.print(ch.receive() + " ");
			}
			System.out.println();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}
}
