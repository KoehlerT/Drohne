package com.drohne.wlanserver;

import de.hardcode.jxinput.Axis;

public class Main {
	
	private static Server s;
	private static Controller c;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		s = new Server();
		c = new Controller();
		new Window(c);
		test();
	}
	
	private static void test() {
		long start;
		while (true) {
			s.acceptClients();
			start = System.nanoTime();
			while (true) {
				//c.update(System.nanoTime()-start);
				//start = System.nanoTime();
				s.sendControl(1000, 1100, 1200, 1300);
			}
		}
	}
	
	private static void printBArray(byte[] b) {
		System.out.println("Array: "+b.length);
		for (byte i : b) {
			System.out.print(i+", ");
		}
		System.out.println();

	}

}
