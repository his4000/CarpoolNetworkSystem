package Message;

import Client.*;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.FlowLayout;

import java.io.PrintWriter;
import java.io.IOException;

public class MessageSendFrame extends JFrame implements ActionListener, KeyListener {
	private JPanel contentPane;
	JPanel TopPanel = new JPanel();
	JLabel recvName = new JLabel();
	JPanel BottomPanel = new JPanel();
	JButton btnTransfer = new JButton("전송");
	JButton btnCancel = new JButton("취소");
	JTextArea txtA = new JTextArea();
	Client client = null;
	String receiver = null;
	PrintWriter pw = null;

	public MessageSendFrame(Client client, String receiver) {
		super("Send Message");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 338, 256);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		this.recvName.setText("To : "+receiver);
		this.client = client;
		this.receiver = receiver;
		
		contentPane.add(TopPanel, BorderLayout.NORTH);
		TopPanel.add(recvName);
		contentPane.add(BottomPanel, BorderLayout.SOUTH);
		BottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		BottomPanel.add(btnTransfer);
		BottomPanel.add(btnCancel);	
		contentPane.add(txtA, BorderLayout.CENTER);
		
		this.btnTransfer.addActionListener(this);
		this.btnCancel.addActionListener(this);
		this.txtA.addKeyListener(this);
		
		this.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == this.btnCancel)
			this.dispose();
		if(e.getSource() == this.btnTransfer){
			try{
				pw = new PrintWriter(this.client.getMsgSocket().getOutputStream(), true);
				pw.println(this.receiver);
				pw.println(this.txtA.getText());
			}catch(IOException ie){
				System.out.println(ie.getMessage());
			}
			this.dispose();
		}
	}
	public void keyPressed(KeyEvent e){}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){
		if(e.getKeyChar() == KeyEvent.VK_ENTER){
			try{
				pw = new PrintWriter(this.client.getMsgSocket().getOutputStream(), true);
				pw.println(this.receiver);
				pw.println(this.txtA.getText());
			}catch(IOException ie){
				System.out.println(ie.getMessage());
			}
			this.dispose();
		}
	}

}
