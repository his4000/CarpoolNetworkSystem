package Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

class MessageServerThread extends Thread{
	Hashtable<String, Socket> clientList = null;
	String clientName = null;
	BufferedReader br = null;
	PrintWriter pw = null;
	
	public MessageServerThread(String clientName, Hashtable<String, Socket> clientList){
		this.clientName = clientName;
		this.clientList = clientList;
	}
	
	public void run(){
		try{
			while(true){
				br = new BufferedReader(new InputStreamReader(this.clientList.get(clientName).getInputStream()));
				String receiver = br.readLine();  //Who receive message
				String content = br.readLine();  //What message content
				sendMsg(this.clientList.get(receiver),this.clientName);
				sendMsg(this.clientList.get(receiver),content);
			}
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	
	private void sendMsg(Socket socket, String msg){
		try{
			pw = new PrintWriter(socket.getOutputStream(),true);
			pw.println(msg);
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
}

public class MessageServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerSocket server = null;
		Socket socket =null;
		Hashtable<String, Socket> clientList = new Hashtable<String, Socket>();
		BufferedReader br = null;
		
		try{
			server= new ServerSocket(6000);
			while(true){
				System.out.println("Message Server waiting...");
				socket = server.accept();
				
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String clientName = br.readLine();  //read client name
				
				clientList.put(clientName, socket);
				
				new MessageServerThread(clientName, clientList).start();
			}
		}catch(IOException ie){
			System.out.println(ie.getMessage());
		}
	}

}
