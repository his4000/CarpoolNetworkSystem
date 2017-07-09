package MultiChat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

class MultiChatServerThread extends Thread{
	Socket socket = null;
	Hashtable<Socket, String> room = null;
	public MultiChatServerThread(Socket socket, Hashtable<Socket, String> room){
		this.socket = socket;
		this.room = room;
	}
	public void run(){
		BufferedReader br = null;
		
		sendMember();
		try{
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			String str =null;
			while(true){
				/* recv string from peer */
				str=br.readLine();
				/* disconnect from peer */
				if(str==null){
					room.remove(socket);
					break;
				}
				/* send string to other peers */
				sendMsg(str);				
			}
			
		}catch(IOException ie){
			System.out.println(ie.getMessage());
		}finally{
			try{
				if(br != null) br.close();
				if(socket != null) socket.close();
			}catch(IOException ie){
				System.out.println(ie.getMessage());
			}
		}
	}
	
	/* send string to other peers */
	public void sendMsg(String str){
		try{
			for(Socket socket:room.keySet()){
				if(socket != this.socket){
					PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);
					pw.println(str);
					pw.flush();
				}
			}
		}catch(IOException ie){
			System.out.println(ie.getMessage());
		}
	}
	/* send chatting member list */
	public void sendMember(){
		Iterator it = this.room.keySet().iterator();
		PrintWriter pw = null;
		
		try{
			while(it.hasNext()){
				Socket temp = (Socket)it.next();
				pw = new PrintWriter(socket.getOutputStream(), true);
				pw.println(this.room.get(temp));
			}
			pw.println(Integer.toString(-1));
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		//pw.close();
	}
}


public class MultiChatServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerSocket server = null;
		Socket socket =null;
		Hashtable<String, Hashtable<Socket, String>> roomlist = new Hashtable<String, Hashtable<Socket, String>>();
		BufferedReader br = null;
		
		try{
			server= new ServerSocket(3000);
			while(true){
				System.out.println("MultiChat Server waiting...");
				socket = server.accept();
				
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));

				String roomName = br.readLine();  //read room name
				String clientName = br.readLine();  //read client name
				
				/* room is exist */
				if(roomlist.containsKey(roomName)){
					roomlist.get(roomName).put(socket, clientName);
				}
				/* room doesn't exist */
				else{
					roomlist.put(roomName, new Hashtable<Socket, String>());  //room allocation
					roomlist.get(roomName).put(socket, clientName);  //insert client in the room
				}
				new MultiChatServerThread(socket, roomlist.get(roomName)).start();
			}
		}catch(IOException ie){
			System.out.println(ie.getMessage());
		}
	}

}
