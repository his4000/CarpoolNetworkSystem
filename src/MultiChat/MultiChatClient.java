package MultiChat;

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

class WriteThread{
	Client client = null;
	MultiChatFrame cf;
	String str = null;
	BufferedReader br = null;
	PrintWriter pw = null;
	
	public WriteThread(MultiChatFrame cf) {
		this.cf = cf;
		this.client = cf.getClient();
		try{
			//키보드로부터 읽어오기 위한 스트림객체 생성
			br = new BufferedReader(new InputStreamReader(System.in));
			//서버로 문자열 전송하기 위한 스트림객체 생성
			pw=new PrintWriter(this.client.getSocket().getOutputStream(),true);
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	public void sendMsg() {
		str= "["+this.client.getID()+"] "+cf.txtF.getText();
		pw.println(Integer.toString(1));	
		//입력받은 문자열 서버로 보내기
		pw.println(str);
	}
	/* Init Code sending */
	public void sendID(){
		pw.println(Integer.toString(0));
		pw.println(this.client.getID());
	}
	/* send Terminate */
	public void sendTerminate(){
		pw.println(Integer.toString(-1));
		pw.println(this.client.getID());
	}
}
//서버가 보내온 문자열을 전송받는 스레드
class ReadThread extends Thread{
	Socket socket;
	MultiChatFrame cf;
	public ReadThread(Socket socket, MultiChatFrame cf) {
		this.cf = cf;
		this.socket=socket;
	}
	public void run() {
		BufferedReader br=null;
		try{
			//서버로부터 전송된 문자열 읽어오기 위한 스트림객체 생성
			br=new BufferedReader(new InputStreamReader(socket.getInputStream()));
			while(true){
				int mode = Integer.parseInt(br.readLine());
				
				switch(mode){
				case 0 : //Init ID recv
					String enterName = br.readLine();
					cf.txtA.append("["+enterName+"]님이 입장하셨습니다.\n");
					cf.memberLabel.put(enterName, new JLabel(enterName));
					cf.memberInfo.add(cf.memberLabel.get(enterName));
					cf.setVisible(true);
					break;
				case 1 : //Message input
					//소켓으로부터 문자열 읽어옴
					String str=br.readLine();
					if(str==null){
						System.out.println("접속이 끊겼음");
						break;
					}
					//전송받은 문자열 화면에 출력
					cf.txtA.append(str+"\n");
					break;
				case -1 : 
					String exitName = br.readLine();
					cf.txtA.append("["+exitName+"]님이 퇴장하셨습니다.\n");
					cf.memberInfo.remove(cf.memberLabel.get(exitName));
					cf.memberLabel.remove(exitName);
					cf.memberInfo.revalidate();
					cf.memberInfo.repaint();
					cf.setVisible(true);
					break;
				}
			}
		}catch(IOException ie){
			System.out.println(ie.getMessage());
		}finally{
			try{
				if(br!=null) br.close();
				if(socket!=null) socket.close();
			}catch(IOException ie){}
		}
	}
}


public class MultiChatClient {
	Client client = null;
	MultiChatFrame cf = null;
	HallFrame hf = null;
	String roomName = null;
	PrintWriter pw = null;
	BufferedReader br = null;
	Hashtable<String, JLabel> memberLabel = null;
	
	public MultiChatClient(Client client, HallFrame hf, String roomName, String path){
		this.client = client;
		this.hf = hf;
		this.memberLabel = new Hashtable<String, JLabel>();
		this.roomName = roomName;
		
		try{
			pw = new PrintWriter(this.client.getSocket().getOutputStream(),true);
			pw.println(this.roomName);
			pw.println(this.client.getID());
			makeMemberList();
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		cf = new MultiChatFrame(this.client, this.hf, roomName, this.memberLabel, path);
		new ReadThread(this.client.getSocket(), cf).start();
	}
	
	public void makeMemberList(){
		try{
			br = new BufferedReader(new InputStreamReader(this.client.getSocket().getInputStream()));
			
			while(true){
				String temp = br.readLine();
				if(temp.equals("-1"))
					break;
				this.memberLabel.put(temp,new JLabel(temp));
			}
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
}
