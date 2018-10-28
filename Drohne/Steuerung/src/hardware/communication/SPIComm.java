package hardware.communication;

import java.nio.ByteBuffer;

public class SPIComm {
	
	static{
		System.load("/lib/drohne/SPICommunication.so");
	}
	
	ByteBuffer sending;
	ByteBuffer receive;
	int size = 10;
	
	private native int init(ByteBuffer in, ByteBuffer out, int speed, int buffersize, int nanoSleep);
	private native void transmit();
	
	
	public SPIComm() {
		sending = ByteBuffer.allocateDirect(size);
		receive = ByteBuffer.allocateDirect(size);
		
		for (int i = 0; i < size; i++) {
			sending.put(i, (byte)i);
		}
		
		init(receive, sending, 10000000,size,3);
		
		
	}
	
	public void comm() {
		long start = System.nanoTime();
		transmit();
		float time = ((float)(System.nanoTime()-start)/1000000000.0f);
		for (int i = 0; i< size; i++) {
			System.out.print(receive.get(i)+ ", ");
		}
		System.out.println("Time: "+time+"ss");
	}
	
	public void setTransmitBuffer(byte[] toSend) {
		for (int i = 0; i < size; i++) {
			sending.put(i, toSend[i]);
		}
	}
	
	public void getReceived(byte[] rcv) {
		for (int i = 0; i < size; i++) {
			rcv[i] = receive.get(i);
		}
	}

}
