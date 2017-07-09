package Client;

import Login.*;
import MultiChat.*;
import Hall.*;
import Room.*;
import java.net.Socket;
import java.util.ArrayList;
import java.io.IOException;

public class Client {
	public final int LoginPort = 4000;
	public final int MultiChatPort = 3000;
	public final int HallPort = 5000;
	public final int MessagePort = 6000;
	public final String ServerIP = "127.0.0.1";
	private String ID = null;
	private String password = null;
	private Socket socket = null;
	private Socket subsocket = null;
	private Socket msgSocket = null;
	private Socket readyMessageSocket = null;
	public boolean LoginComplete = false;
	private String roomName = null;
	private boolean license;
	private String phoneNumber = null;
	private String carIdentify = null;
	private String introduction = null;
	
	/* Constructor */
	public Client(String ID, String password, Socket socket){
		this.ID = ID;
		this.password = password;
		this.socket = socket;
	}
	
	/* return Client ID */
	public String getID(){
		return this.ID;
	}
	/* return Client password */
	public String getPW(){
		return this.password;
	}
	/* return Client Socket */
	public Socket getSocket(){
		return this.socket;
	}
	/* return Client Sub Socket */
	public Socket getSubSocket(){
		return this.subsocket;
	}
	/* return Client License */
	public boolean getLicense(){
		return this.license;
	}
	/* return Client phone number */
	public String getPhoneNumber(){
		return this.phoneNumber;
	}
	/* return Car Identify */
	public String getCarIdentify(){
		return this.carIdentify;
	}
	/* return Client Introduction */
	public String getIntroduction(){
		return this.introduction;
	}
	/* return Msg Socket */
	public Socket getMsgSocket(){
		return this.msgSocket;
	}
	/* Set ID */
	public void setID(String ID){
		this.ID = ID;
	}
	/* Set Password */
	public void setPW(String password){
		this.password = password;
	}
	/* Set Socket */
	public void setSocket(Socket socket){
		this.socket = socket;
	}
	/* Set subSocket */
	public void setSubSocket(Socket socket){
		this.subsocket = socket;
	}
	/* Set Client License */
	public void setLicense(boolean license){
		this.license = license;
	}
	/* Set Client phone number */
	public void setPhoneNumber(String phoneNumber){
		this.phoneNumber = phoneNumber;
	}
	/* Set Car Identify */
	public void setCarIdentify(String carIdentify){
		this.carIdentify = carIdentify;
	}
	/* Set Introduction */
	public void setIntroduction(String introduction){
		this.introduction = introduction;
	}
	/* Enter Room */
	public void setRoom(String roomName){
		this.roomName = roomName;
		//this.room.addMember(this.getSocket());
	}
	/* Set Msg Socket */
	public void setMsgSocket(Socket socket){
		this.msgSocket = socket;
	}
	/* Exit room */
	public void removeRoom(){
		//this.room.deleteMember(this.getSocket());
		this.roomName = null;
	}
	/* return room */
	public String getRoom(){
		return this.roomName;
	}
	/* end client */
	public void endClient(){
		try{
			this.socket.close();
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	//Overiding
	public String toString(){
		return this.ID;
	}
}
