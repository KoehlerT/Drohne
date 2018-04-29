package main;

import java.util.PriorityQueue;
import java.util.Queue;

import hardware.Beeper;

public class ProgramState {
	private static ProgramState instance = new ProgramState();
	public static ProgramState getInstance() {return instance;}
	private ProgramState() {}
	
	private Queue<Byte> incoming = new PriorityQueue<Byte>();
	private byte lastWord = 0;
	
	public synchronized void addControlWord(byte controlWord) {
		incoming.add(controlWord);
	}
	
	public void evaluateControlWord() {
		//System.out.println("incm: "+incoming.peek()+" lw: "+lastWord);
		if (!incoming.isEmpty()) {
			byte cw = incoming.poll();
			if (cw != lastWord) {
				lastWord = cw;
				evaluateWord(cw);
			}
			
			Daten.setControlWord(cw);
		}
	}
	
	private void evaluateWord(byte b) {
		if (b == (byte)0x02) {
			Beeper.getInstance().addBeep(100);
		}
	}
	
}
