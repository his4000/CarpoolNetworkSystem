	package Login;

import Client.*;
import Hall.*;
import Message.*;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.swing.Action;
import javax.swing.JPasswordField;

public class FrameClientLogin extends JFrame implements ActionListener, KeyListener{
	private JPanel panel;
	private JTextField idText;
	private JPasswordField pwText;
	JButton ok = new JButton("확인");
	JButton cancel = new JButton("취소");
	JButton register = new JButton("회원가입");
	private Client client;
	
	public FrameClientLogin(Client client) {
		super("Log-in session");
		this.client = client;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 289, 222);
		panel = new JPanel();
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(panel);
		panel.setLayout(null);
		
		idText = new JTextField();
		idText.setBounds(74, 30, 163, 21);
		panel.add(idText);
		idText.setColumns(15);
		
		JLabel lblId = new JLabel("ID : ");
		lblId.setBounds(29, 33, 23, 15);
		panel.add(lblId);
		
		JLabel lblPw = new JLabel("PW :");
		lblPw.setBounds(29, 65, 33, 15);
		panel.add(lblPw);
		
		ok.setBounds(29, 108, 97, 23);
		ok.addActionListener(this);
		panel.add(ok);
		
		cancel.setBounds(140, 108, 97, 23);
		cancel.addActionListener(this);
		panel.add(cancel);
		
		register.setBounds(74, 141, 128, 23);
		register.addActionListener(this);
		panel.add(register);
		
		pwText = new JPasswordField();
		pwText.setBounds(74, 62, 163, 21);
		panel.add(pwText);
		
		this.idText.addKeyListener(this);
		this.pwText.addKeyListener(this);
		this.setVisible(true);
	}
	public void sendData(String str){
		PrintWriter pw = null;
		
		try{
			pw = new PrintWriter(client.getSocket().getOutputStream(),true);
			pw.println(str);  //send string data
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	
	public void actionPerformed(ActionEvent e){
		BufferedReader br = null;
		
		try{
			br = new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()));
		
			if(e.getSource() == ok){
				sendData("1");  //send mode data : Log-in mode
				/* Input is nothing */
				if(idText.getText().equals(""))
					return;
				/* send Member Data */
				String ID = idText.getText();
				String password = pwText.getText();
				sendData(ID);  //send id to server
				sendData(password);  //send password to server
				
				/* receive message from server */
				String check = null;
				new FrameError(br.readLine());  //open error message
				check = br.readLine();  //recv check code from login server
				
				/* Log in complete */
				if(check.equals("0")){
					this.client.setID(ID);
					this.client.setPW(password);
					this.client.LoginComplete = true;
					this.dispose();
					/* Close Login Socket */
					client.getSocket().close();

					new HallClient(client);
					new MessageClient(client);
				}
			}
		}catch(IOException ie){
			System.out.println(ie.getMessage());
		}
			
		/* Init TextField */
		idText.setText("");
		pwText.setText("");
		
		if(e.getSource() == cancel){
			String mode = "-1";
			sendData(mode);  //send cancel signal to server
			this.dispose();  //frame exit
			try{
				this.client.getSocket().close();
			}catch(IOException ie){
				System.out.println(ie.getMessage());
			}
		}
		if(e.getSource() == register){
			String mode = "0";
			sendData(mode);  //send register signal to server
			this.dispose();  //exit log-in frame
			new FrameRegister(this.client);
		}
	}
	public void keyPressed(KeyEvent e){}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){
		if(e.getKeyChar() == KeyEvent.VK_ENTER){
			BufferedReader br = null;
			
			try{
				br = new BufferedReader(new InputStreamReader(client.getSocket().getInputStream()));
				sendData("1");  //send mode data : Log-in mode
				/* Input is nothing */
				if(idText.getText().equals(""))
					return;
				/* send Member Data */
				String ID = idText.getText();
				String password = pwText.getText();
				sendData(ID);  //send id to server
				sendData(password);  //send password to server
				
				/* receive message from server */
				String check = null;
				FrameError fe = new FrameError(br.readLine());  //open error message
				check = br.readLine();  //recv check code from login server
				
				/* Log in complete */
				if(check.equals("0")){
					fe.dispose();
					this.client.setID(ID);
					this.client.setPW(password);
					this.client.LoginComplete = true;
					this.dispose();
					/* Close Login Socket */
					client.getSocket().close();

					new HallClient(client);
					new MessageClient(client);
				}
			}catch(IOException ie){
				System.out.println(ie.getMessage());
			}
		}
	}
}
