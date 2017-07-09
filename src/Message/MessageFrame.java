package Message;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class MessageFrame extends JFrame {

	private JPanel contentPane;
	JPanel panel = new JPanel();
	JPanel BottomPanel = new JPanel();
	JPanel ButtonPanel = new JPanel();
	JButton btnTransfer = new JButton("Àü¼Û");
	JButton btnClose = new JButton("´Ý±â");
	JPanel intervalPanel = new JPanel();
	JPanel TopPanel = new JPanel();
	JLabel subjectLabel = new JLabel("New label");
	JTextArea txtA = new JTextArea();
	private JTextField txtF;

	public MessageFrame() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 385, 565);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		
		contentPane.add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		
		panel.add(BottomPanel, BorderLayout.SOUTH);
		BottomPanel.setLayout(new BorderLayout(0, 0));
		
		txtF = new JTextField();
		BottomPanel.add(txtF);
		txtF.setColumns(10);
		
		
		BottomPanel.add(ButtonPanel, BorderLayout.SOUTH);
		
		
		ButtonPanel.add(btnTransfer);
		
		
		ButtonPanel.add(btnClose);
		
		
		BottomPanel.add(intervalPanel, BorderLayout.NORTH);
		
		
		panel.add(TopPanel, BorderLayout.NORTH);
		
		
		TopPanel.add(subjectLabel);
		
		
		panel.add(txtA, BorderLayout.CENTER);
		
		this.setVisible(true);
	}

}
