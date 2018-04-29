package main;

import java.util.Date;
import java.util.PriorityQueue;
import java.util.Queue;

public class ProgramState {
	private ProgramState() {};
	private static ProgramState instance = new ProgramState();
	public static ProgramState getInstance() {return instance;}
	
	private long sendingTime;
	private Date timeKeeper = new Date();
	
	private Queue<Byte> cws = new PriorityQueue<Byte>();
	private byte lastSent = 0;
	private Queue<Byte> sending = new PriorityQueue<Byte>();
	
	public synchronized void addReceivedControlWord(byte controlWord) {
		cws.add(controlWord);
	}
	
	public synchronized void addSendingWord(byte controlWord) {
		sending.add(controlWord);
	}
	
	public void evaluateWord() {
		Byte toEvaluate = cws.poll();
		//System.out.println("Last sent: "+lastSent+" Evl: "+toEvaluate );
		if (toEvaluate == null)
			toEvaluate = 0;
		//Gesandtes zurück?
		if (lastSent == (byte)toEvaluate) {
			//System.out.println("Last Sending Time: "+(timeKeeper.getTime()-sendingTime)/1000f+"s");
			Data.setContrWord((byte)0);
			lastSent = 0;
			
		}
		//Neues Byte Senden
		if (lastSent == 0 && !sending.isEmpty()) {
			//Wenn gerade 0 gesandt wurde -> nichts erwartet
			//Und noch etwas gesendet werden soll
			sendingTime= timeKeeper.getTime();
			lastSent = sending.remove();
			Data.setContrWord(lastSent);
		}
	}
}
