package Hall;

import MultiChat.*;
import ShortestPath.*;
import Login.FrameError;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Iterator;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.net.Socket;
import java.io.*;

public class makeRoomFrame extends JFrame implements ActionListener, KeyListener{
	private HallFrame hf = null;
	private JPanel contentPane;
	private JTextField name;
	private JTextField source;
	private JTextField destination;
	JButton ok = new JButton("Ȯ��");
	JButton cancel = new JButton("���");
	private PrintWriter pw = null;

	public makeRoomFrame(HallFrame hf) {
		super("Make Room");
		this.hf = hf;

		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 363, 227);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		name = new JTextField();
		name.setBounds(66, 37, 267, 21);
		contentPane.add(name);
		name.setColumns(10);
		
		JLabel lblName = new JLabel("�� �̸�");
		lblName.setBounds(12, 40, 42, 15);
		contentPane.add(lblName);
		
		source = new JTextField();
		source.setBounds(66, 80, 89, 21);
		contentPane.add(source);
		source.setColumns(10);
		
		JLabel lblSource = new JLabel("�����");
		lblSource.setBounds(12, 83, 42, 15);
		contentPane.add(lblSource);
		
		destination = new JTextField();
		destination.setColumns(10);
		destination.setBounds(244, 80, 89, 21);
		contentPane.add(destination);
		
		JLabel lblDestination = new JLabel("������");
		lblDestination.setBounds(193, 83, 71, 15);
		contentPane.add(lblDestination);
		
		ok.addActionListener(this);
		ok.setBounds(66, 134, 97, 23);
		contentPane.add(ok);
		
		cancel.addActionListener(this);
		cancel.setBounds(193, 134, 97, 23);
		contentPane.add(cancel);
		
		this.name.addKeyListener(this);
		this.source.addKeyListener(this);
		this.destination.addKeyListener(this);
		this.setVisible(true);
	}

	public void actionPerformed(ActionEvent e){
		String getName = this.name.getText();
		String getSource = this.source.getText();
		String getDestination = this.destination.getText();
		
		if(e.getSource() == ok){
			if(getName.equals("") || getSource.equals("") || getDestination.equals(""))
				return;
			if(!isVertex(getSource)){
				new FrameError("������� �߸� �Է��ϼ̽��ϴ�.\n�յ� ���� ���� �Է��ϼ���");
				return;
			}
			if(!isVertex(getDestination)){
				new FrameError("�������� �߸� �Է��ϼ̽��ϴ�.\n�յ� ���� ���� �Է��ϼ���");
				return;
			}
			hf.makeRoom(getName, getSource, getDestination);
			this.dispose();
			/* make path */
			LinkedList<Vertex> tmpPathList = hf.getRoomList().get(getName);
			Iterator it = tmpPathList.iterator();
			String tmpPathStr = "";
			while(it.hasNext()){
				tmpPathStr = tmpPathStr + " " + it.next();
			}
			try{
				//hf.getClient().getSocket().close();  //close socket
				hf.getClient().setSubSocket(hf.getClient().getSocket());  //transfer hall socket to subSocket
				hf.getClient().setSocket(new Socket(hf.getClient().ServerIP, hf.getClient().MultiChatPort));  //open MultiChatSocket
				
				//hf.getClient().getRoom().addMember(hf.getClient().getSocket());  //add Room member
			
				//pw = new PrintWriter(hf.getClient().getSocket().getOutputStream(),true);
				
				//pw.println(getName);  //send Room Name to MultiChatServer

				new MultiChatClient(hf.getClient(),this.hf,getName, tmpPathStr);
				
				hf.setVisible(false);  //hall frame invisible
			}catch(IOException ie){
				System.out.println(ie.getMessage());
			}
		}
		if(e.getSource() == cancel){
			this.dispose();
		}
	}
	
	public void keyPressed(KeyEvent e){}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){
		String getName = this.name.getText();
		String getSource = this.source.getText();
		String getDestination = this.destination.getText();
		
		if(e.getKeyChar() == KeyEvent.VK_ENTER){
			if(getName.equals("") || getSource.equals("") || getDestination.equals(""))
				return;
			if(!isVertex(getSource)){
				new FrameError("������� �߸� �Է��ϼ̽��ϴ�.\n�յ� ���� ���� �Է��ϼ���");
				return;
			}
			if(!isVertex(getDestination)){
				new FrameError("�������� �߸� �Է��ϼ̽��ϴ�.\n�յ� ���� ���� �Է��ϼ���");
				return;
			}
			hf.makeRoom(getName, getSource, getDestination);
			this.dispose();
			/* make path */
			LinkedList<Vertex> tmpPathList = hf.getRoomList().get(getName);
			Iterator it = tmpPathList.iterator();
			String tmpPathStr = "";
			while(it.hasNext()){
				tmpPathStr = tmpPathStr + " " + it.next();
			}
			try{
				//hf.getClient().getSocket().close();  //close socket
				hf.getClient().setSubSocket(hf.getClient().getSocket());  //transfer hall socket to subSocket
				hf.getClient().setSocket(new Socket(hf.getClient().ServerIP, hf.getClient().MultiChatPort));  //open MultiChatSocket
				
				//hf.getClient().getRoom().addMember(hf.getClient().getSocket());  //add Room member
			
				//pw = new PrintWriter(hf.getClient().getSocket().getOutputStream(),true);
				
				//pw.println(getName);  //send Room Name to MultiChatServer
				
				new MultiChatClient(hf.getClient(),this.hf,getName, tmpPathStr);
				
				hf.setVisible(false);  //hall frame invisible
			}catch(IOException ie){
				System.out.println(ie.getMessage());
			}
		}
		
	}
	public boolean isVertex(String str){
		if(str.equals("�ŵ���"))
			return true;
		if(str.equals("õȣ"))
			return true;
		if(str.equals("����"))
			return true;
		if(str.equals("����"))
			return true;
		if(str.equals("����"))
			return true;
		if(str.equals("��ȭ��"))
			return true;
		if(str.equals("�д�"))
			return true;
		if(str.equals("�ϻ�"))
			return true;
		if(str.equals("��"))
			return true;
		if(str.equals("�Ȼ�"))
			return true;
		if(str.equals("�Ⱦ�"))
			return true;
		if(str.equals("����"))
			return true;
		if(str.equals("�Ǵ�"))
			return true;
		if(str.equals("������"))
			return true;
		if(str.equals("�ϳ�"))
			return true;
		return false;
	}
}
