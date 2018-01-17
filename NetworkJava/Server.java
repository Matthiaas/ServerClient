package Network;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable{


	
	
	private ExecutorService threadPool;
	
	private ServerSocket serverSocket;
	private int serverPort;
	
	
	//Maximum amount of clients that will be served but all clients will be accepted!
	private int maxThreads = 1;
	private boolean isStopped = false;
	Class<? extends ConnectedClient> cl;
	
	
	
	
	
	public Server(int port , Class<? extends ConnectedClient> cl) {
		this.serverPort = port;
		this.cl = cl;
		threadPool = Executors.newFixedThreadPool(maxThreads);
	}
	
	
	public Server(int port , Class<? extends ConnectedClient> cl , int maxThreads) {
		this.serverPort = port;
		this.cl = cl;
		this.maxThreads = maxThreads;
		threadPool = Executors.newFixedThreadPool(maxThreads);
	}
	

	

    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }

    
    
    private void openServerSocket() {
        try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port 8080", e);
        }
    }

    private synchronized boolean isStopped() {
        return this.isStopped;
    }
	

	@Override
	public void run() {
		
		openServerSocket();
		
		
        while(! isStopped()){
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
                ConnectedClient ccl = cl.getDeclaredConstructor().newInstance();
                ccl.setSocket(clientSocket);
                this.threadPool.execute(ccl); 
           
            
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.") ;
                    break;
                }
                throw new RuntimeException( "Error accepting client connection", e);
            } catch (InstantiationException | InvocationTargetException| IllegalAccessException 
            		| IllegalArgumentException | NoSuchMethodException | SecurityException e) {
				e.printStackTrace();
			}
            
           
        }
        this.threadPool.shutdown();
        System.out.println("Server Stopped.") ;
		
		
	}
		
	
	
	
}
