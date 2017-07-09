package MultiChat;

import Client.*;
import Hall.*;
import Map.*;
import ShortestPath.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.StringTokenizer;

import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ImageIcon;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class MultiChatFrame extends JFrame implements ActionListener, KeyListener{
	public JTextArea txtA = new JTextArea();
	public JPanel memberInfo = new JPanel();
	public JLabel pathInfo = new JLabel();
	public JTextField txtF = new JTextField(15);
	private JButton btnTransfer = new JButton("Àü¼Û");
	private JButton btnExit = new JButton("´Ý±â");
	private MapButton btnMap = new MapButton();
	private JPanel p1 = new JPanel();
	private JPanel p2 = new JPanel();
	private WriteThread wt = null;
	private Client client = null;
	private HallFrame hf = null;
	private PrintWriter pw = null;
	private BufferedReader br = null;
	Hashtable<String, JLabel> memberLabel = null;
	LinkedList<Vertex> pathList = null;
	
	public MultiChatFrame(Client client, HallFrame hf, String roomName, Hashtable<String, JLabel> memberLabel, String path) {
		super("["+client.getID()+"]"+roomName);
		this.client = client;
		this.hf = hf;
		this.pathInfo.setText(path);
		//this.memberList = memberList;
		this.memberLabel = memberLabel;
		this.memberInfo.setLayout(new GridLayout(8,1));
		this.tokenizing(path);  //tokenizing path
		
		this.memberInfo.setSize(100, 280);
		wt = new WriteThread(this);
		wt.sendID();
		Iterator it = this.memberLabel.keySet().iterator();
		while(it.hasNext()){
			String name = (String)it.next();
			this.memberLabel.put(name, new JLabel(name));
			this.memberInfo.add(this.memberLabel.get(name));
			//this.txtB.setText(this.txtB.getText() + "\n" + (String)it.next() + "\n");
		}
		add("Center", txtA);
		add("East", memberInfo);
		
		p1.add(txtF);
		p1.add(btnTransfer);
		p1.add(btnExit);
		
		this.p2.setSize(200, 200);
		
		p2.add(pathInfo);
		p2.add(btnMap);
	
		add("South", p1);
		add("North",p2);
		
		btnTransfer.addActionListener(this);
		btnExit.addActionListener(this);
		btnMap.addActionListener(this);
		this.addWindowListener(new JFrameWindowClosingEventHandler(this.client,this.hf,this.wt));
		this.txtF.addKeyListener(this);
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setBounds(300, 300, 400, 600);
		setVisible(true);	
	}
	
	public void actionPerformed(ActionEvent e){
		/* send Button */
		if(e.getSource()==btnTransfer){
			/* No Message */
			if(txtF.getText().equals("")){
				return;
			}
			/* Send Message */
			txtA.append("["+this.client.getID()+"] "+ txtF.getText()+"\n");
			wt.sendMsg();
			txtF.setText("");
		}
		else if(e.getSource() == btnMap){
			new SeoulMap(this.pathList);
		}
		else{
			this.dispose();
			try{
				wt.sendTerminate();
				this.client.getSocket().close();  //close MultiChat Socket
				this.client.setSocket(this.client.getSubSocket());  //open Hall Socket

				pw = new PrintWriter(this.client.getSocket().getOutputStream(),true);
				br = new BufferedReader(new InputStreamReader(this.client.getSocket().getInputStream()));
				pw.println("3");
				int roomCount = Integer.parseInt(br.readLine());  //recv room count from hall server
				System.out.println("room count : "+roomCount);
				/* exit room & not terminate room */
				if(roomCount > 1){
					this.client.setRoom(null);
					pw.println("2");
				}
				/* exit room & terminate room */
				else{
					this.client.setRoom(null);
					pw.println("-2");
				}
				this.hf.setVisible(true);  //hall frame visible
				
			}catch(IOException ie){
				System.out.println(ie.getMessage());
			}
		}
	}
	public void keyPressed(KeyEvent e){}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){
		if(e.getKeyChar() == KeyEvent.VK_ENTER){
			/* No Message */
			if(txtF.getText().equals("")){
				return;
			}
			/* Send Message */
			txtA.append("["+this.client.getID()+"] "+ txtF.getText()+"\n");
			wt.sendMsg();
			txtF.setText("");
		}
	}
	
	public Client getClient(){
		return this.client;
	}
	/* path tokenizing */
	public void tokenizing(String path){
		path = path.trim();
		this.pathList = new LinkedList<Vertex>();
		StringTokenizer st = new StringTokenizer(path," ");
		while(st.hasMoreTokens()){
			String temp = st.nextToken();
			this.pathList.add(new Vertex(temp, temp));
		}
	}
}

class MapButton extends JButton implements MouseListener{
	private ImageIcon OriginIcon = new ImageIcon("./Image/MapIcon.png");
	private ImageIcon OverIcon = new ImageIcon("./Image/MapIconOver.png");
	private ImageIcon ClickedIcon = new ImageIcon("./Image/MapIconClicked.png");
	
	public MapButton(){
		this.setSize(200, 200);
		this.setIcon(this.OriginIcon);
		this.setBorderPainted(false);
		this.setFocusPainted(false);
		this.setContentAreaFilled(false);
		addMouseListener(this);
	}
	
	public void mouseClicked(MouseEvent e){}
	public void mousePressed(MouseEvent e){
		this.setIcon(this.ClickedIcon);
	}
	public void mouseReleased(MouseEvent e){
		this.setIcon(this.OriginIcon);
	}
	public void mouseEntered(MouseEvent e){
		this.setIcon(this.OverIcon);
	}
	public void mouseExited(MouseEvent e){
		this.setIcon(this.OriginIcon);
	}
}

class JFrameWindowClosingEventHandler extends WindowAdapter{
	Client client = null;
	PrintWriter pw = null;
	BufferedReader br = null;
	HallFrame hf = null;
	WriteThread wt = null;
	
	public JFrameWindowClosingEventHandler(Client client, HallFrame hf, WriteThread wt){
		this.client = client;
		this.hf = hf;
		this.wt = wt;
	}
	
	public void windowClosing(WindowEvent e){
		try{
			wt.sendTerminate();
			this.client.getSocket().close();  //close MultiChat Socket
			this.client.setSocket(this.client.getSubSocket());  //open Hall Socket
			pw = new PrintWriter(this.client.getSocket().getOutputStream(),true);
			br = new BufferedReader(new InputStreamReader(this.client.getSocket().getInputStream()));
			pw.println("3");
			int roomCount = Integer.parseInt(br.readLine());  //recv room count from hall server
			System.out.println("room count : "+roomCount);
			/* exit room & not terminate room */
			if(roomCount > 1){
				this.client.setRoom(null);
				pw.println("2");
			}
			/* exit room & terminate room */
			else{
				this.client.setRoom(null);
				pw.println("-2");
			}
			this.hf.setVisible(true);  //hall frame visible
			
		}catch(IOException ie){
			System.out.println(ie.getMessage());
		}
	}
}
