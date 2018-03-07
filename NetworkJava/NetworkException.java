package Network;

import java.io.IOException;

public class NetworkException extends RuntimeException {

	private static final long serialVersionUID = -8154485522805727356L;

	IOException realException;
	String error;
	
	public NetworkException(String error, IOException realException) {
		this.realException = realException;
		this.error = error;
	}
	
	public IOException getRealException() {
		return this.realException;
	}
}
