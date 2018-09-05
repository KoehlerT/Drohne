package com.koehlert.serialcomm;

public class Run {

	/**
	 * @param args
	 */
	
	static{
		System.load("/lib/drohne/libSerialComm.so");
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
		Communicator comm = new Communicator(10,10);
		
		byte[] transmit = new byte[]{0,1,2,3,4,5,6,7,8,9};
		byte[] receive = new byte[10];
		
		for (int x = 0; x < 100; x++){
			comm.setTransmitBuffer(transmit);
			long start = System.nanoTime();
			comm.communicate();
			comm.getReceiveBuffer(receive);
			long dur = System.nanoTime() - start;
			
			for (int i = 0; i < 10; i++){
				System.out.print(receive[i]+", ");
			}
			System.out.println(" in "+((float)dur/1000000.0f)+" ms");
		}
		
		
		
		comm.close();
		
	}

}
