package Login;

import Client.*;
import MultiChat.*;
import Hall.*;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.Socket;

import java.io.*;

/*class ErrorFrame extends JFrame implements ActionListener{
	JButton ok = new JButton("확인");
	JLabel text = new JLabel();
	
	public ErrorFrame(String str){
		super("Confirm");
		text.setText(str);
		
		this.setLayout(new FlowLayout());
		this.add(text);
		this.add(ok);
		
		ok.addActionListener(this);
		
		this.setBounds(300, 300, 250, 100);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == ok)
			this.dispose();  //close window
	}
}*/

class RegisterFrame extends JFrame implements ActionListener{
	JButton subscribe = new JButton("제출");
	JButton cancel = new JButton("취소");
	JTextField idText = new JTextField(15);
	JPasswordField pwText = new JPasswordField(15);
	JTextField phoneNumber = new JTextField(15);
	JTextField CarIdentify = new JTextField(15);
	JTextArea Introduction = new JTextArea(5,15);
	JRadioButton HasCar = new JRadioButton("Car Owner");
	JRadioButton notHasCar = new JRadioButton("non-Car Owner");
	ButtonGroup Radios = new ButtonGroup();
	Client client = null;
	
	public RegisterFrame(Client client){
		super("회원가입");
		this.client = client;
		
		Radios.add(HasCar);
		Radios.add(notHasCar);
		
		this.setLayout(new FlowLayout());
		this.add(new JLabel("ID"));
		this.add(idText);
		this.add(new JLabel("Password"));
		this.add(pwText);
		this.add(new JLabel("H.P"));
		this.add(phoneNumber);
		this.add(HasCar);	this.add(notHasCar);
		this.add(new JLabel("Car Identify"));
		this.add(CarIdentify);
		this.add(new JLabel("Introduction"));
		this.add(Introduction);
		this.add(subscribe);
		this.add(cancel);
		
		subscribe.addActionListener(this);
		cancel.addActionListener(this);
		
		this.setBounds(300, 300, 350, 300);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	public void sendData(String str){
		PrintWriter pw = null;
		
		try{
			pw = new PrintWriter(this.client.getSocket().getOutputStream(),true);
			pw.println(str);  //send string data
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	public void actionPerformed(ActionEvent e){
		BufferedReader br = null;
		
		try{
			br = new BufferedReader(new InputStreamReader(this.client.getSocket().getInputStream()));
		}catch(IOException ie){
			System.out.println(ie.getMessage());
		}
		if(e.getSource() == subscribe){
			/* send data to server */
			sendData(idText.getText());  //send new id
			sendData(pwText.getText());  //send new password
			sendData(phoneNumber.getText());  //send phone number
			//sendData(Integer.toString(this.Radios.getElements()));
			/* Car Identify send */
			String CarIdentifytemp = this.CarIdentify.getText();
			if(CarIdentifytemp.equals(""))
				sendData("null");
			else
				sendData(CarIdentifytemp);
			sendData(Introduction.getText());  //send Introduction
			/* open confirm */
			try{
				new FrameError(br.readLine());
			}catch(IOException ie){
				System.out.println(ie.getMessage());
			}
			this.dispose();
			new ClientLoginFrame(this.client);
		}
		if(e.getSource() == cancel){
			this.dispose();
			new ClientLoginFrame(this.client);  //re-open log-in frame
		}
	}
}

public class ClientLoginFrame extends JFrame implements ActionListener{
	JTextField idText = new JTextField(15);
	JPasswordField pwText = new JPasswordField(15);
	JButton ok = new JButton("확인");
	JButton cancel = new JButton("취소");
	JButton register = new JButton("회원가입");
	JPanel panel = new JPanel();
	Client client = null;
	
	public ClientLoginFrame(Client client){
		super("Log-in session");
		this.client = client;
		
		panel.setLayout(new FlowLayout());
		panel.add(new JLabel("ID"));
		panel.add(idText);
		panel.add(new JLabel("Password"));
		panel.add(pwText);
		panel.add(ok);
		panel.add(cancel);
		panel.add(register);
		this.add(panel);
		
		ok.addActionListener(this);
		cancel.addActionListener(this);
		register.addActionListener(this);
		
		this.setBounds(300, 300, 350, 300);
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
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
					//client.setSocket(new Socket(client.ServerIP, client.HallPort));
					new HallClient(client);
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
			new RegisterFrame(this.client);
		}
	}
}
