import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;


public class receiver {

  public final static int SOCKET_PORT = 13267;      // you may change this
  public final static String SERVER = "127.0.0.1";  // localhost
  public final static String
       FILE_TO_RECEIVED = "c:/temp/source-downloaded.jpg";  // you may change this, I give a
                                                            // different name because i don't want to
                                                            // overwrite the one used by server...

  public final static int FILE_SIZE = 6022386; // file size temporary hard coded
                                               // should bigger than the file to be downloaded

  public static void main (String [] args ) throws IOException {
	  datenEmpfangen(SOCKET_PORT, SERVER);
	  datenEmpfangen(SOCKET_PORT, SERVER);
  }
  public static int bytetoint(byte[] bar) {
	  ByteBuffer bb = ByteBuffer.wrap(bar);
	  return bb.getInt();
	  
  }
  public static byte[] datenEmpfangen(int port, String server) throws IOException, IOException {
	  int bytesRead;
	  int current = 0;
	  Socket sock = null;
      byte [] mybytearray  = new byte [FILE_SIZE];
	  try {
	  sock = new Socket(server, port);
      System.out.println("Connecting...");

      // receive file
      InputStream is = sock.getInputStream();
      bytesRead = is.read(mybytearray,0,mybytearray.length);
      current = 4;

      do {
         bytesRead =
            is.read(mybytearray, current, (mybytearray.length-current));
         if(bytesRead >= 0) current += bytesRead;
      } while(bytesRead < -1);

      System.out.println("Int: " + bytetoint(mybytearray));
      System.out.println("Int: " + bytetoint(mybytearray) 
          + " downloaded (" + current + " bytes read)");
      if (is != null) is.close();
	  }
    finally {
      if (sock != null) sock.close();
    }
	  return mybytearray;  
  }
  

}