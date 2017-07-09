package Hall;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import Client.*;
import Room.*;

public class HallClient {
	Client client = null;
	
	public HallClient(Client client){
		this.client = client;
		
		try{
			client.setSocket(new Socket(client.ServerIP, client.HallPort));
		}catch(IOException e){
			System.out.println(e.getMessage());
		}	
		sendData(this.client.getID());
		
		new HallFrame(this.client);
	}
	
	/* send Data */
	public void sendData(String str){
		PrintWriter pw = null;
		try{
			pw = new PrintWriter(this.client.getSocket().getOutputStream(),true);
			pw.println(str);  //send string data
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
}
