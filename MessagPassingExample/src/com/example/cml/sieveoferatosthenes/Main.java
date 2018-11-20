package com.example.cml.sieveoferatosthenes;

import java.util.ArrayList;

import concurrency.message.Channel;

public class Main {

	public static void main(String[] args) {
		ArrayList<Integer> prime_list = primes(500);
		for(Integer i : prime_list) {
			System.out.print(i + " ");
		}
		System.out.println();
	}
	
	public static ArrayList<Integer> primes(int n) {
		Channel<Integer> ch = sieve();
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i=0; i<n; i++) {
			try {
				list.add(ch.receive());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return list;
	}
	
	public static Channel<Integer> filter(int p, Channel<Integer> inCh) {
		Channel<Integer> outCh = new Channel<Integer>();
		Thread t = new Thread() {
			public void run() {
				while (true) {
					try {
						int i = inCh.receive();
						if (i % p != 0) {
							outCh.send(i);
						}
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		t.start();
		
		return outCh;
	}
	
	public static Channel<Integer> sieve () {
		Channel<Integer> primes = new Channel<Integer>();
		
		Thread t = new Thread() {
			public void run() {
				try {
					Channel<Integer> head_ch = counter(2);
					while(true) {
						int p = head_ch.receive();
						primes.send(p);
						head_ch = filter(p, head_ch);
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
		
		return primes;
	}

	public static Channel<Integer> counter(int n) {
		Channel<Integer> ch = new Channel<Integer>();
		Thread t = new Thread() {
			public void run() {
				int i = n;
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
		
		return ch;
	}
}
