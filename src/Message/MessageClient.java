package Message;

import Client.*;
import Hall.*;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

class MessageRecvThread extends Thread{
	Client client = null;
	Socket socket = null;
	BufferedReader br = null;
	
	public MessageRecvThread(Client client){
		this.client = client;
		this.socket = this.client.getMsgSocket();
	}
	public void run(){
		System.out.println("Message Receive Thread Running...");
		try{
			while(true){
				br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
				String sender = br.readLine();
				System.out.println("sender : "+sender);
				String content = br.readLine();
				System.out.println("content : "+content);
				
				new MessageRecvFrame(this.client, sender, content);
			}
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
}

public class MessageClient {
	Client client = null;
	PrintWriter pw = null;
	
	public MessageClient(Client client){
		this.client = client;
		try{
			this.client.setMsgSocket(new Socket(this.client.ServerIP, this.client.MessagePort));
			pw = new PrintWriter(this.client.getMsgSocket().getOutputStream(),true);
			pw.println(this.client.getID());
			new MessageRecvThread(this.client).start();
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
}
