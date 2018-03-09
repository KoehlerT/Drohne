import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.TimeUnit;
//http://www.rgagnon.com/javadetails/java-0542.html
public class sender {

	public final static int SOCKET_PORT = 13267; // you may change this

	public static void main(String[] args) throws IOException {
		byte[] ma = new byte[4];
		ServerSocket s = sock÷ffnen(SOCKET_PORT);
		ma = inttobyte(10);
		verschicken(s, ma);
		ma = inttobyte(12);
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		verschicken(s, ma);
	}

	public static byte[] inttobyte(int data) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(4);
		IntBuffer intBuffer = byteBuffer.asIntBuffer();
		intBuffer.put(data);

		byte[] array = byteBuffer.array();
		return array;

	}

	public static ServerSocket sock÷ffnen(int soPort) {
		ServerSocket servsock = null;
		try {
			servsock = new ServerSocket(soPort);
		} catch (IOException e) {
			System.out.println("Error: " + e);
			e.printStackTrace();
		}
		return servsock;
	}

	public static void verschicken(ServerSocket s, byte[] b) {
	  OutputStream os = null;
	  Socket sock = null;
	  
          System.out.println("Waiting...");
          try {
            sock = s.accept();
            System.out.println("Accepted connection : " + sock);
            // send file
            byte [] mybytearray  = b;
            System.out.println("Zu senden: " + b);
            os = sock.getOutputStream();
            System.out.println("Sending! "+  mybytearray.length + " bytes)");
            os.write(mybytearray,0,mybytearray.length);
            os.flush();
            System.out.println("Done.");
            if (os != null) os.close();
            if (sock!=null) sock.close();
            for(int i = 0; i < mybytearray.length-1; i++) {
          	  mybytearray[i] = 0;
            }
          }
            
           catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
          finally {
            if (os != null)
				try {
					os.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            if (sock!=null)
				try {
					sock.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
          }
        
          }
	  
  
}
