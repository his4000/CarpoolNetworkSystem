package Login;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.GridLayout;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.FormSpecs;
import java.awt.CardLayout;
import java.awt.FlowLayout;

public class FrameError extends JFrame implements ActionListener, KeyListener{
	private JPanel contentPane;
	JButton ok = new JButton("»Æ¿Œ");
	JLabel text = new JLabel();

	public FrameError(String str) {
		super("Confim");
		text.setBounds(61, 38, 184, 15);
		this.text.setText(str);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 330, 152);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		ok.setBounds(120, 80, 81, 23);
		
		contentPane.add(ok);
	
		contentPane.add(text);
		
		ok.addActionListener(this);
		this.ok.addKeyListener(this);
		
		this.setVisible(true);
	}
	public void actionPerformed(ActionEvent e){
		if(e.getSource() == ok)
			this.dispose();  //close window
	}
	public void keyPressed(KeyEvent e){}
	public void keyReleased(KeyEvent e){}
	public void keyTyped(KeyEvent e){
		if(e.getKeyChar() == KeyEvent.VK_ENTER)
			this.dispose();
	}
}
