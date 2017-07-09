package Message;

import java.net.Socket;

public class MessagePeer {
	private Socket sender = null;
	private Socket receiver = null;
	
	public MessagePeer(Socket sender, Socket receiver){
		this.sender = sender;
		this.receiver = receiver;
	}
	
	public Socket getSender(){
		return this.sender;
	}
	public Socket getReceiver(){
		return this.receiver;
	}
	public void setSender(Socket sender){
		this.sender = sender;
	}
	public void setReceiver(Socket receiver){
		this.receiver = receiver;
	}
	
	public String getString(){
		String get = this.sender.toString() + this.receiver.toString();
		
		return get;
	}
}
