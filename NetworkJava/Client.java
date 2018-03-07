package Network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * @author matthias
 *
 */
public class Client {

	
	
	private Socket socket;
	private String adress ; 
	private int port;
	
	private OutputStream out;
	private InputStream in;
	
	/**
	 * @param adress
	 * @param port
	 */
	public Client(String adress , int port) {
		this.port = port;
		this.adress=adress;
	}
	
	/**
	 * 
	 */
	public Client() {
		
	}
	
	/**
	 * @param socket
	 * @return true if successful 
	 */
	public boolean setSocket(Socket socket) {
		if(this.socket != null) return false;

		this.socket= socket;
		try {
			out = socket.getOutputStream();
			in = socket.getInputStream();
		} catch (IOException e) {
			return false;
		}
		return true;
		
	}
	
	
	/**
	 * @return
	 */
	public boolean connect() {
		if(socket != null && socket.isConnected()) return true;
		try {
			return setSocket(new Socket(adress , port));
		} catch (IOException e) {
			System.err.println(e);
			return false;
		}
	}
	
	
	
	/**
	 * @return true if successful 
	 */
	public final boolean disconnect() {
		if(socket == null ) return false;
		try {
			socket.close();
			return true;
		} catch (IOException e) {
			throw new NetworkException("Error Closing connection to Client",e);
		}
	}
	
	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////
					// WRITE SECTION //

	/**
	 * @param write
	 * @return
	 */
	public final boolean writeByte(int write) {
		if(socket == null ) return false;
		try {
			out.write(write);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	/**
	 * @param buf
	 * @return true if successful 
	 */
	public final boolean writeByteArray(byte[] buf) {
		if(socket == null ) return false;
		try {
			out.write(buf);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	/**
	 * @param buf - buffer
	 * @param off - offset
	 * @param len - length
	 * @return
	 */
	public final boolean writeByteArray(byte[] buf , int off , int len) {
		if(socket == null ) return false;
		try {
			out.write(buf, off, len);
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	/**
	 * @param chars
	 * @return true if successful 
	 */
	private final byte[] toBytes(char[] chars) {
	    CharBuffer charBuffer = CharBuffer.wrap(chars);
	    ByteBuffer byteBuffer = Charset.forName("UTF-8").encode(charBuffer);
	    byte[] bytes = Arrays.copyOfRange(byteBuffer.array(),
	            byteBuffer.position(), byteBuffer.limit());
	    Arrays.fill(charBuffer.array(), '\u0000'); // clear sensitive data
	    Arrays.fill(byteBuffer.array(), (byte) 0); // clear sensitive data
	    return bytes;
	}
	
	/**
	 * @param toSend
	 * @return
	 */
	public final boolean writeCharArray(char[] toSend) {
		if(socket == null ) return false;
		try {
			out.write(toBytes(toSend));
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	/**
	 * @param send
	 * @return true if successful 
	 */
	public final boolean writeInt(int send) {
		if(socket == null ) return false;
		for(int i = 24; i >= 0 ; i-=8) {
			try {
				out.write((int)(send>>> i));
			} catch (IOException e) {
				return false;
			}
		}
		return true;
	}
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	/**
	 * @param send
	 * @return true if successful 
	 */
	public final boolean writeLong(long send) {
		
		if(socket == null ) return false;
		for(int i = 56; i >= 0 ; i-=8) {
			try {
				out.write((int)(send>>> i));
			} catch (IOException e) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * @param s
	 * @return true if successful 
	 */
	public final boolean writeString(String s) {
		if(socket == null ) return false;
		try {
			out.write(s.getBytes());
			out.write(0);
		} catch (IOException e) {
			return false;
		}
		
		return true;
	}
	
	
	
	
	
	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////
						// READ SECTION //
	/**
	 * @return byte
	 */
	public final int readByte()  {
		try {
			return in.read();
		} catch (IOException e) {
			throw new NetworkException("Error reading byte",e);
		}
	}
	
	
	/**
	 * @return int
	 */
	public final int readInt() {
		int res = 0;
		for( int i  = 24; i >= 0; i-=8)
			try {
				res |= in.read() << i;
			} catch (IOException e) {
				throw new NetworkException("Error reading int",e);
			}
		
		return res;
	}
	/**
	 * @return long
	 */
	public final long readLong() {
		long res = 0;
		for( int i  = 56; i >= 0; i-=8)
			try {
				res |= ((long) in.read()) << i;
			} catch (IOException e) {
				throw new NetworkException("Error reading long",e);
			}
		
		
		return res;
	}
	
	
	/**
	 * @return String
	 */
	public final String readString()  {
		String res = "";
		
		char curr;
		do {
			try {
				curr = (char)in.read();
			} catch (IOException e) {
				throw new NetworkException("Error reading String",e);
			}
			if(curr != 0) {
				res += curr;
			}
		}while(curr != 0);
		
		return res;
	}
	
	
	/**
	 * @param buf - buffer
	 * @param off - offset
	 * @param len - length
	 */
	public final void readByteArray(byte[] buf , int off , int len)  {
		try {
			in.read(buf, off, len);
		} catch (IOException e) {
			throw new NetworkException("Error reading byte array",e);
		}
	}
	
	/**
	 * @param buf - buffer
	 */
	public final void readByteArray(byte[] buf ) {
		try {
			in.read(buf);
		} catch (IOException e) {
			throw new NetworkException("Error reading byte array",e);
		}
	}
	
	/**
	 * @param len - length
	 * @return byte array of length len
	 */
	public final byte[] readByteArray(int len)  {
		byte[] buf = new byte[len];
		try {
			in.read(buf);
		}catch (IOException e) {
			throw new NetworkException("Error reading byte array",e);
		}
		return buf;
	}
	
	
	/**
	 * @param len - length
	 * @return char array of length len
	 */
	public final char[] readCharArray(int len)  {
		char[] res= new char[len];
		for(int i = 0; i < len ; i++) {
			try {
				res[i] = (char) in.read();
			}catch (IOException e) {
				throw new NetworkException("Error char array",e);
			}
		}
		return res;
	}
	
	
	
	
	///////////////////////////////////////////////////////////////
	///////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////
						// GETTER SECTION //
	
	
	
	/**
	 * @return true if client connected
	 */
	public final boolean isConnected() {		
		if(socket == null ) return false;
		return socket.isConnected();
	}
	
}
