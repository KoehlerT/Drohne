package main;

import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

public class ControlWordHandler {
	private ControlWordHandler() {};
	private static ControlWordHandler instance = new ControlWordHandler();
	public static ControlWordHandler getInstance() {return instance;}
	
	
	private Queue<Byte> received = new PriorityBlockingQueue<Byte>();
	private Queue<Byte> sending = new PriorityBlockingQueue<Byte>();
	
	public synchronized void addReceivedControlWord(byte controlWord) {
		if (controlWord != 0) {
			received.add(controlWord);
		}
			
	}
	
	public synchronized void addSendingWord(byte controlWord) {
		sending.add(controlWord);
	}
	
	public synchronized byte getNextSendingWord() {
		return (sending.peek() == null)? 0: sending.poll();
	}
	
	public void evaluateWord() {
		byte recv = (received.peek() == null)? 0: received.poll();
		
		actions(recv);
	}
	
	private void actions(byte controlWord) {
		
		if (controlWord != 0)
		System.out.println("evl: "+controlWord);
		
		switch(controlWord) {
		case 0b00000001: System.out.println("1 Empfangen!"); break;
		
		default: break;
		}
	}
}
