package Hall;

import Room.*;
import Client.*;
import ShortestPath.*;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Iterator;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;

class HallThread extends Thread{
	ArrayList<Room> roomlist = null;
	Hashtable<String, Socket> clientSocketList = null;
	ArrayList<Client> clientInfoList = null;
	BufferedReader br = null;
	PrintWriter pw = null;
	Socket socket = null;
	String name = null;
	Room myRoom = null;
	Seoul map = null;
	
	/* Search Client variables */
	String searchID = new String();
	String searchPhoneNumber = new String();
	String searchCarIdentify = new String();
	String searchIntroduction = new String();
	
	public HallThread(Socket socket, ArrayList<Room> roomlist, Hashtable<String, Socket> clientSocketList, ArrayList<Client> clientInfoList, Seoul map){
		this.socket = socket;
		this.roomlist = roomlist;
		this.clientSocketList = clientSocketList;
		this.clientInfoList = clientInfoList;
		this.map = map;
		
		try{
			br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
			pw = new PrintWriter(this.socket.getOutputStream(),true);
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	public void run(){
		name = recvData();  //receive client ID from socket
		this.clientSocketList.put(name, socket);  //add in the hashtable

		sendRoomList();
		sendClientSocketList();
		
		while(true){
			/* Receive order number from Client */
			int order = Integer.parseInt(recvData());
				
			switch(order){
			case -2 :   //terminate room
				deleteRoom();
				exitRoom();
				break;
			case -1 : //exit client
				this.clientSocketList.remove(this.name);  //remove from client list
				break;
			case 0 : //refresh
				sendRoomList();
				sendClientSocketList();
				break;
			case 1 : //make room
				addRoom();
				break;
			case 2 : //exit room
				exitRoom();
				break;
			case 3 : //please send room count
				pw.println(Integer.toString(this.myRoom.getCount()));
				break;
			case 4 : //enter room
				enterRoom();
				break;
			case 5 : //search other client
				this.searchID = recvData();
				loadClient();  //load search Client information

				/* send search client information to Client */
				pw.println(searchPhoneNumber);
				pw.println(searchCarIdentify);
				pw.println(searchIntroduction);
				this.searchID = null;
				this.searchPhoneNumber = null;
				this.searchCarIdentify = null;
				this.searchIntroduction = null;
				break;
			}
		}
	}
	/* add Room */
	public void addRoom(){
		try{
			String roomName = br.readLine();  //recv Room name
			String source = br.readLine();  //recv Source Vertex
			String dest = br.readLine();  //recv Destination Vertex
			myRoom = new Room(4, roomName);  //make Room Object
			myRoom.setPath(source, dest, map.getPath(source, dest));  //path setting
			myRoom.addMember(socket);
			this.roomlist.add(myRoom);
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	/* enter Room */
	public void enterRoom(){
		Iterator it = this.roomlist.iterator();
		try{
			String roomName = br.readLine();  //recv Room name from Hall Frame
			while(it.hasNext()){
				Room temp = (Room)it.next();
				if(temp.getSubject().equals(roomName)){
					this.myRoom = temp;
					this.myRoom.addMember(socket);
					return;
				}
			}
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	/* delete Room */
	public void deleteRoom(){
		this.roomlist.remove(myRoom);
	}
	/* exit Room */
	public void exitRoom(){
		myRoom.deleteMember(socket);
		//System.out.println("exit Room Running");
		myRoom = null;
	}
	/* send Data */
	public void sendData(String str){
		pw.println(str);  //send string data
	}
	/* receive Data */
	public String recvData(){
		String result = null;
		
		try{
			result = br.readLine();
			//System.out.println();
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		return result;
	}
	/* send roomList */
	public void sendRoomList(){
		Iterator it = this.roomlist.iterator();
		
		while(it.hasNext()){
			Room temp = (Room)it.next();
			sendData(temp.getSubject());
			//System.out.println(name);
			sendRoomPath(temp);
		}
		sendData("-1");
	}
	/* send Room Path */
	public void sendRoomPath(Room room){
		Iterator it = room.getPath().iterator();
		while(it.hasNext()){
			Vertex temp = (Vertex)it.next();
			this.sendData(temp.getName());
		}
		this.sendData(Integer.toString(-1));
	}
	/* send clientList */
	public void sendClientSocketList(){
		Iterator it = this.clientSocketList.keySet().iterator();
		
		while(it.hasNext()){
			String ID = (String)it.next();
			sendData(ID);
			//System.out.println(ID);
		}
		sendData("-1");
	}
	/* Load Client Info */
	public void loadClient(){   ////객체 선언!!!!!!!!!!!!!!!!!!!!!
		BufferedReader br = null;
		/* 여기 테스트 할것 */
		try{
			br = new BufferedReader(new FileReader("./MemberInfo/"+this.searchID+".txt"));
			this.searchPhoneNumber = br.readLine();
			//System.out.println("phone number : "+this.searchPhoneNumber);
			this.searchCarIdentify = br.readLine();
			//System.out.println("car identify : "+this.searchCarIdentify);
			this.searchIntroduction = br.readLine();
			//System.out.println("introduction : "+this.searchIntroduction);
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
}

public class HallServer {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Room> roomlist = new ArrayList<Room>();
		Hashtable<String, Socket> clientSocketList = new Hashtable<String, Socket>();
		ArrayList<Client> clientInfoList = new ArrayList<Client>();
		ServerSocket server = null;
		Socket socket = null;
		Seoul map = new Seoul();
		try{
			server = new ServerSocket(5000);
			while(true){
				System.out.println("Hall Server Waiting...");
				socket = server.accept();
				
				/* Connection is complete */
				new HallThread(socket, roomlist, clientSocketList, clientInfoList, map).start();
			}
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}

}
