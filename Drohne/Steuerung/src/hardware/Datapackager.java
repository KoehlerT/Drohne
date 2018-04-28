package hardware;

public class Datapackager {
	
	public Datapackager(){
		
	}
	
	
	public static void untangleReceived(byte[] received) {
		byte controlWord = received[1];
		
		System.out.println("CW: "+controlWord);
		int throttle = 0;
		throttle = received[2];
		throttle &= 0x00000FF;
		throttle |= ((received[6]&0b11000000)<<2);
		throttle+=1000;
		System.out.println(throttle);
	}
	
	
	public static void printBinaryArray(byte[] bin) {
		for (int i = 0; i < bin.length; i++) {
			byte content = bin[i];
			String str = String.format("%8s", Integer.toBinaryString(content & 0xFF)).replace(' ', '0');
			System.out.println("Array ["+i+"]: "+str);
		}
	}
	
}
