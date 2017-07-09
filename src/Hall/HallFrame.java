package Hall;

import Client.*;
import Room.*;
import MultiChat.*;
import ShortestPath.*;
import Message.*;

import java.util.Hashtable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Graphics;
import java.awt.color.*;
import java.awt.Color;

public class HallFrame extends JFrame implements ActionListener{
	ImageIcon bg = new ImageIcon("./Image/Hall.jpg");
	JPanel contPane = new JPanel(){
		public void paintComponent(Graphics g){
			g.drawImage(bg.getImage(), 0, 0, 640, 360, this);
		}
	};
	private Hashtable<String, LinkedList<Vertex>> roomList = null;
	private ArrayList<String> clientList = null;
	private RoomPanel roomPanel = null;
	private ClientPanel clientPanel = null;
	private JPanel BottomPanel = new JPanel();
	private JPanel CenterPanel = new JPanel();
	private JPanel TopPanel = new JPanel();
	private JButton refresh = new JButton("새로고침");
	private JButton makeRoom = new JButton("방만들기");
	private Client client = null;
	private BufferedReader br = null;
	private PrintWriter pw = null;
	
	final int refreshX = 70;
	final int refreshY = 0;
	final int roomPanelX = 120;
	final int roomPanelY = 0;
	
	public HallFrame(Client client){
		super("["+client.getID()+"]"+"Hall");
		this.add(contPane);
		this.client = client;
		this.roomPanel = new RoomPanel(this);
		this.clientPanel = new ClientPanel(this);
		
		try{
			br = new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()));
			pw = new PrintWriter(client.getSocket().getOutputStream(),true);
		
			recvRoomList();
			recvClientList();
			makeRoomList();
			makeClientList();
			
			this.contPane.setLayout(new BorderLayout());
			this.TopPanel.setLayout(new FlowLayout());
			this.BottomPanel.setLayout(new FlowLayout());
			this.CenterPanel.setLayout(new BorderLayout());
			
			this.TopPanel.setBackground(new Color(255,0,0,0));
			this.BottomPanel.setBackground(new Color(255,0,0,0));
			this.CenterPanel.setBackground(new Color(255,0,0,0));
			this.roomPanel.setBackground(new Color(255,0,0,0));
			this.clientPanel.setBackground(new Color(255,0,0,0));
			
			this.setBounds(200, 200, 640, 390);
				
			this.TopPanel.add(new JLabel("[ "+this.client.getID() + "'s Hall ]"));

			this.BottomPanel.add(makeRoom);
			this.BottomPanel.add(refresh);
			
			this.contPane.add("North",TopPanel);
			this.contPane.add("South",BottomPanel);
			this.contPane.add("Center",CenterPanel);
			this.contPane.add("West",roomPanel);
			this.contPane.add("East",clientPanel);
			
			this.addWindowListener(new JFrameWindowClosingEventHandler(this.pw));
			this.refresh.addActionListener(this);
			this.makeRoom.addActionListener(this);
			
			this.setVisible(true);
			this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	
	public void actionPerformed(ActionEvent e){
		/* 새로고침 */
		if(e.getSource() == refresh){
			Refresh();
		}
		/* 방만들기 */
		if(e.getSource() == makeRoom){
			sendData("1");
			new makeRoomFrame(this);
		}
	}
	
	/* return Room Panel */
	public JPanel getRoomPanel(){
		return this.roomPanel;
	}
	/* return Client Panel */
	public JPanel getClientPanel(){
		return this.clientPanel;
	}
	/* return Room Path */
	public Hashtable<String, LinkedList<Vertex>> getRoomList(){
		return this.roomList;
	}
	/* send Data */
	public void sendData(String str){
		this.pw.println(str);
	}
	/* receive Data */
	public String recvData(){
		String result = null;
		try{
			result = br.readLine();
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		
		return result;
	}
	
	/* recv Room List */
	public void recvRoomList(){
		this.roomList = new Hashtable<String, LinkedList<Vertex>>();
		String temp = "0";
		
		while(true){
			temp = recvData();
			if(temp.equals("-1"))
				break;
			roomList.put(temp, new LinkedList<Vertex>());
			recvRoomPath(temp);
			//System.out.println("roomlist recv : "+temp);
		}
	}
	/* recv Room Path */
	public void recvRoomPath(String roomName){
		String temp = "0";
		while(true){
			temp = recvData();
			if(temp.equals("-1"))
				break;
			roomList.get(roomName).add(new Vertex(temp, temp));
		}
	}
	/* recv Client List */
	public void recvClientList(){
		this.clientList = new ArrayList<String>();
		String temp = "0";
		
		while(true){
			temp = recvData();
			if(temp.equals("-1"))
				break;
			this.clientList.add(temp);
		}
		
	}
	/* make Room List Panel */
	public void makeRoomList(){
		Iterator it = this.roomList.keySet().iterator();
		this.roomPanel = new RoomPanel(this);
		this.roomPanel.setBackground(new Color(255,0,0,0));

		while(it.hasNext()){
			String ButtonTag = (String)it.next();
			this.roomPanel.addElement(ButtonTag, this.roomList.get(ButtonTag));
		}
	}
	/* make Client List Panel */
	public void makeClientList(){
		Iterator it = this.clientList.iterator();
		this.clientPanel = new ClientPanel(this);
		this.clientPanel.setBackground(new Color(255,0,0,0));

		while(it.hasNext()){
			String ButtonTag = (String)it.next();
			this.clientPanel.addElement(ButtonTag);
		}
		
	}
	/* Refresh */
	public void Refresh(){
		sendData("0");
		
		recvRoomList();
		recvClientList();
		makeRoomList();
		makeClientList();
		
		this.contPane.removeAll();
		this.contPane.add("North",TopPanel);
		this.contPane.add("South",BottomPanel);
		this.contPane.add("Center",CenterPanel);
		this.contPane.add("West",roomPanel);
		this.contPane.add("East",clientPanel);
		
		this.roomPanel.revalidate();
		this.roomPanel.repaint();
		
		this.clientPanel.revalidate();
		this.clientPanel.repaint();
		
		this.contPane.revalidate();
		this.contPane.repaint();
		
		this.revalidate();
		this.repaint();
		
	}
	/* make Room */
	public void makeRoom(String name, String source, String dest){
		pw.println(name);  //send room name
		pw.println(source);
		pw.println(dest);
		this.client.setRoom(name);
		
		Refresh();
	}
	/* return client */
	public Client getClient(){
		return this.client;
	}
}

class RoomPanel extends JPanel implements ActionListener{  //Panel의 Hall의 객체가 맞는지 그래서 통신이 안되나
	private Hashtable<String, JButton> roomButtonList = null;
	private Hashtable<String, String> roomPathList = null;
	private HallFrame hall = null;
	private JPanel ButtonPanel = new JPanel();
	private JLabel subjectLabel = new JLabel("<  R O O M   L I S T  >");
	PrintWriter pw = null;
	Client client = null;
	
	public RoomPanel(HallFrame hall){
		this.setBackground(new Color(255,0,0,0));
		this.roomButtonList = new Hashtable<String, JButton>();
		this.roomPathList = new Hashtable<String, String>();
		this.hall = hall;
		this.client = this.hall.getClient();
		this.setLayout(new BorderLayout());
		this.subjectLabel.setBackground(new Color(255,0,0,0));
		this.add("North",subjectLabel);
		this.ButtonPanel.setLayout(new GridLayout(5,1));
		this.add("Center",this.ButtonPanel);
		this.ButtonPanel.setBackground(new Color(255,0,0,0));
	}
	
	public void actionPerformed(ActionEvent e){
		Iterator it = this.roomButtonList.keySet().iterator();
		
		while(it.hasNext()){
			String name = (String)it.next();
			if(e.getSource() == this.roomButtonList.get(name)){
				try{
					pw = new PrintWriter(client.getSocket().getOutputStream(),true);
					pw.println("4");  //enter room signal to HallServer
					pw.println(name);  //room name send to HallServer
					
					client.setSubSocket(client.getSocket());
					client.setSocket(new Socket(client.ServerIP, client.MultiChatPort));
					
					hall.setVisible(false);
					new MultiChatClient(client, hall, name, this.roomPathList.get(name));
				}catch(IOException ie){
					System.out.println(ie.getMessage());
				}
			}
		}
	}
	
	public Hashtable<String, JButton> getRoomButtonList(){
		return this.roomButtonList;
	}
	
	public Hashtable<String, String> getRoomPathList(){
		return this.roomPathList;
	}
	
	public void addElement(String name, LinkedList<Vertex> path){
		String roomName = "["+name+"]";
		String pathStr = "";
		Iterator it = path.iterator();
		/* add path in room name */
		while(it.hasNext()){
			Vertex vtemp = (Vertex)it.next();
			pathStr = pathStr+" "+vtemp.getName();
		}
		this.roomPathList.put(name, pathStr);
		roomName = roomName + pathStr;
		JButton temp = new JButton(roomName);
		this.ButtonPanel.add(temp);
		this.roomButtonList.put(name, temp);
		temp.addActionListener(this);
	}
	
	public void refreshAction(){
		Iterator it = this.roomButtonList.keySet().iterator();
		
		while(it.hasNext()){
			this.roomButtonList.get(it.next()).addActionListener(this);
		}
	}
}

class ClientPanel extends JPanel implements ActionListener{
	private Hashtable<String, JButton> clientButtonList = null;
	private HallFrame hall = null;
	private PrintWriter pw = null;
	private JPanel ButtonPanel = new JPanel();
	private JLabel subjectLabel = new JLabel("<  C L I E N T   L I S T  > ");
	
	public ClientPanel(HallFrame hall){
		this.setBackground(new Color(255,0,0,0));
		this.clientButtonList = new Hashtable<String, JButton>();
		this.hall = hall;
		this.setLayout(new BorderLayout());
		this.ButtonPanel.setLayout(new GridLayout(5,1));
		//this.setBounds(0, 0, 300, 300);
		this.subjectLabel.setBackground(new Color(255,0,0,0));
		this.add("North",this.subjectLabel);
		this.ButtonPanel.setBackground(new Color(255,0,0,0));
		this.add("Center",this.ButtonPanel);
	}
	
	public void actionPerformed(ActionEvent e){
		Iterator it = this.clientButtonList.keySet().iterator();
		
		while(it.hasNext()){
			String name = (String)it.next();
			/* Client Button Action Handler */
			if(name != this.hall.getClient().getID() && e.getSource() == this.clientButtonList.get(name)){
				try{
					pw = new PrintWriter(this.hall.getClient().getSocket().getOutputStream(), true);
					pw.println(Integer.toString(5));  //Open Other Client Information
					pw.println(name);  //search ID send
					
					new MemberInfoFrame(name, hall.getClient());
				}catch(IOException ie){
					System.out.println(ie.getMessage());
				}
			}
		}
	}
	public Hashtable<String, JButton> getClientButtonList(){
		return this.clientButtonList;
	}
	public void addElement(String name){
		JButton temp = new JButton(name);
		this.ButtonPanel.add(temp);
		this.clientButtonList.put(name, temp);
		temp.addActionListener(this);
	}
	
}

class MemberInfoFrame extends JFrame implements ActionListener{
	private JPanel picturePanel = new JPanel();
	private JLabel picture = null;
	private JLabel car = null;
	private JButton chatting = new JButton("쪽지");
	private JButton ok = new JButton("확인");
	private JPanel ButtonPanel = new JPanel();
	private JTextArea info = new JTextArea(8,25);
	private JTextArea carInfo = new JTextArea(5,25);
	private JPanel infoPanel = new JPanel();
	private String searchID = null;	
	private Client client = null;
	private BufferedReader br = null;
	
	/* Image */
	private ImageIcon nullCar = new ImageIcon("./Image/nullcar.png");
	private ImageIcon Sonata = new ImageIcon("./Image/sonata.jpg");
	private ImageIcon Avante = new ImageIcon("./Image/avante.jpg");
	private ImageIcon Soul = new ImageIcon("./Image/soul.jpg");
	private ImageIcon Lamborghini = new ImageIcon("./Image/람보르기니.jpg");
	private ImageIcon MiniCooper = new ImageIcon("./Image/미니쿠퍼.jpg");
	
	public MemberInfoFrame(String searchID, Client client){
		super("["+searchID+"] Information");
		this.searchID = searchID;
		this.client = client;
		
		this.picture = new JLabel(new ImageIcon("./Image/boss.png"));
		this.car = new JLabel();
		
		recvSearchClientInfo();
		
		this.setLayout(new BorderLayout());
		this.ButtonPanel.setLayout(new FlowLayout());
		this.infoPanel.setLayout(new BorderLayout());
		this.picturePanel.setLayout(new BorderLayout());
		
		this.infoPanel.add("North",this.info);
		this.infoPanel.add("South",this.carInfo);
		
		this.add("North",new JPanel());
		this.picturePanel.add("North",picture);
		this.picturePanel.add("Center",new JLabel("  Car Identification"));
		this.picturePanel.add("South",car);
		this.add("Center",picturePanel);
		this.add("East",infoPanel);
		this.add("South",ButtonPanel);
		this.ButtonPanel.add(chatting);
		this.ButtonPanel.add(ok);
		
		this.chatting.addActionListener(this);
		this.ok.addActionListener(this);
		
		this.setBounds(300, 300, 450, 350);
		
		this.setVisible(true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == this.ok){
			this.dispose();
		}
		if(e.getSource() == this.chatting){
			new MessageSendFrame(this.client, this.searchID);
		}
	}
	
	public void recvSearchClientInfo(){
		try{
			br = new BufferedReader(new InputStreamReader(this.client.getSocket().getInputStream()));
			String temp = null;
			this.info.append("아이디 : " + this.searchID + "\n");
			temp = br.readLine();  //recv phone number
			this.info.append("전화 번호 : "+temp+"\n");
			temp = br.readLine();  //recv car identify
			if(temp.equals("null"))
				this.carInfo.append("차 종 : 등록된 차량이 없습니다.\n\n");
			else
				this.carInfo.append("차 종 : "+temp+"\n\n");
			setCarImage(temp);
			temp = br.readLine();  //recv introduction
			this.info.append("\n자기 소개\n"+temp+"\n");
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	
	private void setCarImage(String car){
		if(car.equalsIgnoreCase("Sonata") || car.equals("소나타"))
			this.car.setIcon(this.Sonata);
		else if(car.equalsIgnoreCase("Avante") || car.equals("아반떼"))
			this.car.setIcon(this.Avante);
		else if(car.equalsIgnoreCase("Soul") || car.equals("소울"))
			this.car.setIcon(this.Soul);
		else if(car.equalsIgnoreCase("Lamborghini") || car.equals("람보르기니"))
			this.car.setIcon(this.Lamborghini);
		else if(car.equalsIgnoreCase("Mini Cooper") || car.equalsIgnoreCase("MiniCooper") || car.equals("미니쿠퍼") || car.equals("미니 쿠퍼"))
			this.car.setIcon(this.MiniCooper);
		else
			this.car.setIcon(this.nullCar);
	}
}

class JFrameWindowClosingEventHandler extends WindowAdapter{
	PrintWriter pw = null;
	
	public JFrameWindowClosingEventHandler(PrintWriter pw){
		this.pw = pw;
	}
	
	public void windowClosing(WindowEvent e){
		this.pw.println("-1");  //room is remain after me out
		System.exit(0);
	}
}
