package Message;

import Client.*;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.*;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.FlowLayout;
import javax.swing.JTextArea;

public class MessageRecvFrame extends JFrame implements ActionListener, KeyListener{
	private JPanel contentPane;
	JPanel TopPanel = new JPanel();
	JLabel sendName = new JLabel();
	JPanel BottomPanel = new JPanel();
	JButton btnReply = new JButton("답장");
	JButton btnCancel = new JButton("확인");
	JTextArea txtA = new JTextArea();
	Client client = null;
	String sender = null;

	public MessageRecvFrame(Client client, String sender, String content) {
		super("Received Message");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(500, 100, 330, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		this.sender = sender;
		this.client = client;
		this.sendName.setText("From : "+sender);
		this.txtA.setText(content);
		
		contentPane.add(TopPanel, BorderLayout.NORTH);
		TopPanel.add(sendName);
		contentPane.add(BottomPanel, BorderLayout.SOUTH);
		BottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		BottomPanel.add(btnReply);
		BottomPanel.add(btnCancel);
		contentPane.add(txtA, BorderLayout.CENTER);
		
		this.btnCancel.addActionListener(this);
		this.btnReply.addActionListener(this);
		this.txtA.addKeyListener(this);
		
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == this.btnCancel)
			this.dispose();
		if(e.getSource() == this.btnReply){
			this.dispose();
			new MessageSendFrame(this.client, this.sender);
		}
	}
	public void keyPressed(KeyEvent e){}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){
		if(e.getKeyChar() == KeyEvent.VK_ENTER){
			this.dispose();
		}
	}

}
