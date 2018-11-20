package com.example.cml.cell;

public class Main {

	public static void main(String[] args) {
		Cell<String> aCell = new Cell<String>("");
		System.out.println(aCell.get());
		aCell.put("hello");
		System.out.println(aCell.get());
		System.out.println(aCell.get());
		System.out.println(aCell.get());

		aCell.put("world");
		System.out.println(aCell.get());
	}

}
