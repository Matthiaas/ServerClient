package Network;

public abstract class ConnectedClient extends Client implements Runnable{
		
	
	public ConnectedClient() {
		
	}
	
	
//	public void inti() {}
	
	
	@Override
	public abstract void run();	
	
}