package main;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

import hardware.Beeper;
import utility.FlyingMode;

public class ProgramState {
	private static ProgramState instance = new ProgramState();
	public static ProgramState getInstance() {return instance;}
	private ProgramState() {}
	
	private Queue<Byte> received = new PriorityBlockingQueue<Byte>();
	private Queue<Byte> toSend = new PriorityBlockingQueue<Byte>();
	
	
	public synchronized void addControlWord(byte controlWord) {
		if (controlWord != 0) {
			received.add(controlWord);
		}
			
	}
	
	public synchronized void sendControlWord(byte controlWord) {
		toSend.add(controlWord);
	}
	
	public synchronized byte getNextSeningWord() {
		return (toSend.peek() == null)? 0: toSend.poll();
	}
	
	public synchronized void evaluateControlWord() {
		byte recv = (received.peek() == null)? 0: received.poll();
		
		evaluateWord(recv);
		
	}
	
	private void evaluateWord(byte b) {
		if (b != 0)
			System.out.println("evl: "+b);
		
		switch (b) {
		case 0x02: Beeper.getInstance().addBeep(100); break;
		
		case 0x12: Daten.setFlyingMode(FlyingMode.FORCESTOP); break;
		case 0x13: /*TODO: Rückkehr*/; break;
		case 0x14: Daten.setFlyingMode(FlyingMode.FORCEDOWN); break;
		case 0x15: Daten.setFlyingMode(FlyingMode.MANUAL); break;
		case 0x16: Daten.setFlyingMode(FlyingMode.AUTOMATIC); break;
		}
		
	}
	
}
