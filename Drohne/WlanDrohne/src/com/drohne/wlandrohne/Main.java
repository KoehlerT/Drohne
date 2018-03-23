package com.drohne.wlandrohne;

public class Main {

	/**
	 * @param args
	 */
	static private Client b;
	static private byte[][] pic = new byte[100][];
	
	public static void main(String[] args) {
		b = new Client();
		constrPic();
		test();
	}
	
	static private void constrPic(){
		for(int i = 0; i < pic.length; i++){
			pic[i] = new byte[100];
			for (int j = 0; j < pic[i].length; j++){
				pic[i][j] = (byte)i;
			}
		}
	}
	
	static private void test(){
		while (true){
			b.receiveControls();
		}
	}

}
