package Map;

import ShortestPath.*;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

import java.util.LinkedList;
import java.util.Hashtable;
import java.util.Iterator;

public class SeoulMap extends JFrame {
	private BackGroundPanel contentPane;
	private LinkedList<Vertex> path = null;
	
	public SeoulMap(LinkedList<Vertex> path) {
		super("Map");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setLayout(null);
		setBounds(800, 100, 757, 665);
		this.path = path;
		contentPane = new BackGroundPanel(this.path);
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		this.setVisible(true);
	}
}

class BackGroundPanel extends JPanel{
	private ImageIcon bg = null;
	private ImageIcon point = null;
	private LinkedList<Vertex> path = null;
	private Hashtable<String, Point> vertexPoint = null;
	final int sizeofOvral = 20;
	
	public BackGroundPanel(LinkedList<Vertex> path){
		this.bg = new ImageIcon("./Image/Seoul.jpg");
		this.point = new ImageIcon("./Image/Point.jpg");
		this.path = path;
		this.vertexPoint = new Hashtable<String, Point>();
		makeVertexPoint();
	}
	
	public void paintComponent(Graphics g){
		g.drawImage(bg.getImage(), 0, 0, 757, 665, this);
		Iterator it = this.path.iterator();
		//g.drawLine(0, 0, 100, 100);
		Vertex prev = (Vertex)it.next();
		while(it.hasNext()){
			g.drawImage(point.getImage(), this.vertexPoint.get(prev.getName()).getX(), this.vertexPoint.get(prev.getName()).getY(), this.sizeofOvral, this.sizeofOvral, this);
			Vertex next = (Vertex)it.next();
			
			g.drawImage(point.getImage(), this.vertexPoint.get(next.getName()).getX(), this.vertexPoint.get(next.getName()).getY(), this.sizeofOvral, this.sizeofOvral, this);
			drawMyLine(g,Color.red,this.vertexPoint.get(prev.getName()).getX(), this.vertexPoint.get(prev.getName()).getY(),
					this.vertexPoint.get(next.getName()).getX(), this.vertexPoint.get(next.getName()).getY());
			prev = next;	
		}
	}
	
	public void makePathMap(Graphics g){
		Iterator it = this.path.iterator();
		
		while(it.hasNext()){
			Vertex prev = (Vertex)it.next();
			g.drawImage(point.getImage(), this.vertexPoint.get(prev).getX(), this.vertexPoint.get(prev).getY(), this.sizeofOvral, this.sizeofOvral, this);
			Vertex next = null;
			if(it.hasNext()){
				next = (Vertex)it.next();
				g.drawImage(point.getImage(), this.vertexPoint.get(next).getX(), this.vertexPoint.get(next).getY(), this.sizeofOvral, this.sizeofOvral, this);
				drawMyLine(g,Color.red,this.vertexPoint.get(prev).getX(), this.vertexPoint.get(prev).getY(),this.vertexPoint.get(next).getX(), this.vertexPoint.get(next).getY());
			}
				
		}
	}
	
	public void drawMyLine(Graphics g, Color c, int sx, int sy, int dx, int dy){
		int dep = (sx - dx) * (sy - dy);
		
		g.setColor(c);
		if(dep < 0){
			for(int i=0;i<5;i++)
				g.drawLine(sx+i+10, sy+i+10, dx+i+10, dy+i+10);
		}
		else{
			for(int i=0;i<5;i++)
				g.drawLine(sx+i+10, sy-i+10, dx+i+10, dy-i+10);
		}
		g.setColor(Color.BLACK);
	}
	
	public void makeVertexPoint(){
		this.vertexPoint.put("신도림", new Point(270, 370));
		this.vertexPoint.put("천호", new Point(520, 360));
		this.vertexPoint.put("강남", new Point(420, 400));
		this.vertexPoint.put("수원", new Point(400, 600));
		this.vertexPoint.put("신촌", new Point(300, 300));
		this.vertexPoint.put("광화문", new Point(350, 300));
		this.vertexPoint.put("분당", new Point(600, 500));
		this.vertexPoint.put("일산", new Point(120,120));
		this.vertexPoint.put("목동", new Point(250, 350));
		this.vertexPoint.put("안산", new Point(200, 600));
		this.vertexPoint.put("안양", new Point(300, 550));
		this.vertexPoint.put("종로", new Point(350, 270));
		this.vertexPoint.put("건대", new Point(480, 350));
		this.vertexPoint.put("의정부", new Point(440, 60));
		this.vertexPoint.put("하남", new Point(600, 400));
	}
}

class Point{
	private int x;
	private int y;
	
	public Point(int x, int y){
		this.x = x;
		this.y = y;
	}
	public void setX(int x){
		this.x = x;
	}
	public void setY(int y){
		this.y = y;
	}
	public int getX(){
		return this.x;
	}
	public int getY(){
		return this.y;
	}
}