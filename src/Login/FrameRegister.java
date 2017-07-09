package Login;

import Client.*;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.awt.event.ActionEvent;

import javax.swing.*;
import javax.swing.border.*;

public class FrameRegister extends JFrame implements ActionListener{
	private Client client = null;
	private JPanel contentPane;
	private JTextField idText;
	private JPasswordField pwText;
	private JTextField phoneNumber;
	private JTextField carIdentify;
	JTextArea Introduction = new JTextArea();
	JButton subscribe = new JButton("제출");
	JButton cancel = new JButton("취소");
	JRadioButton HasCar = new JRadioButton("Car Owner");
	JRadioButton notHasCar = new JRadioButton("Client");
	ButtonGroup Radios = new ButtonGroup();

	public FrameRegister(Client client) {
		super("Registration");
		this.client = client;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 326, 441);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		Radios.add(HasCar);
		Radios.add(notHasCar);
		
		idText = new JTextField();
		idText.setBounds(60, 34, 121, 21);
		contentPane.add(idText);
		idText.setColumns(15);
		
		JLabel lblId = new JLabel("ID : ");
		lblId.setBounds(32, 37, 29, 15);
		contentPane.add(lblId);
		
		JLabel lblPw = new JLabel("PW : ");
		lblPw.setBounds(26, 79, 35, 15);
		contentPane.add(lblPw);
		
		pwText = new JPasswordField();
		pwText.setBounds(60, 76, 121, 21);
		contentPane.add(pwText);
		
		JLabel lblHp = new JLabel("H.P : ");
		lblHp.setBounds(26, 123, 57, 15);
		contentPane.add(lblHp);
		
		HasCar.setSelected(true);
		HasCar.setBounds(26, 161, 121, 23);
		contentPane.add(HasCar);
		
		notHasCar.setBounds(151, 161, 121, 23);
		contentPane.add(notHasCar);
		
		JLabel lblCar = new JLabel("Car : ");
		lblCar.setBounds(26, 202, 35, 15);
		contentPane.add(lblCar);
		
		Introduction.setBounds(12, 274, 286, 77);
		contentPane.add(Introduction);
		
		JLabel lblIntroduction = new JLabel("Introduction");
		lblIntroduction.setBounds(12, 249, 89, 15);
		contentPane.add(lblIntroduction);
		
		subscribe.setBounds(32, 369, 97, 23);
		contentPane.add(subscribe);
		
		phoneNumber = new JTextField();
		phoneNumber.setColumns(15);
		phoneNumber.setBounds(60, 120, 121, 21);
		contentPane.add(phoneNumber);
		
		carIdentify = new JTextField();
		carIdentify.setColumns(15);
		carIdentify.setBounds(60, 199, 121, 21);
		contentPane.add(carIdentify);
		
		
		cancel.setBounds(175, 369, 97, 23);
		contentPane.add(cancel);
		
		subscribe.addActionListener(this);
		cancel.addActionListener(this);
		
		this.setVisible(true);
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

			/* Car Identify send */
			String CarIdentifytemp = this.carIdentify.getText();
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
			new FrameClientLogin(this.client);
		}
		if(e.getSource() == cancel){
			this.dispose();
			new FrameClientLogin(this.client);  //re-open log-in frame
		}
	}
}
