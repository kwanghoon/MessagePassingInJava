package com.example.cml.cell;

public class Request<T> {
	public static final int GET = 1;
	public static final int PUT = 2;
	
	private int tag;
	private T a;

	public Request(int tag, T a) {
		this.tag = tag;
		this.a = null;
		if (tag == PUT) this.a = a;
	}
	
	public int getTag() {
		return this.tag;
	}
	
	public T getA() {
		return this.a;
	}
}
