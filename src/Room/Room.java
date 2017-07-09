package Room;

import Client.*;
import ShortestPath.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.net.Socket;

public class Room {
	private int limit = 0;  //person limit
	private int count = 0;  //member counting
	private ArrayList<Socket> member = null;
	private String subject = null;  //Room subject
	private String source = null;  //Source Vertex
	private String dest = null;  //Destination Vertex
	private LinkedList<Vertex> path = null;  //path
	
	public Room(int limit, String subject){
		this.limit = limit;
		this.subject = subject;
		this.member = new ArrayList<Socket>();
		this.path = new LinkedList<Vertex>();
	}
	
	/* add member */
	public void addMember(Socket socket){
		if(count < limit){
			member.add(socket);
			this.count++;
		}
		else
			System.out.println("Room is Full!!");
	}
	/* delete member */
	public void deleteMember(Socket socket){
		if(count > 0){
			member.remove(socket);
			this.count--;
		}
		else
			System.out.println("Room is already empty!!");
	}
	/* Set Source */
	public void setSource(String str){
		this.source = str;
	}
	/* Set Destination */
	public void setDest(String str){
		this.dest = str;
	}
	/* Set Path */
	public void setPath(String src, String dest, LinkedList<Vertex> path){
		setSource(src);
		setDest(dest);
		this.path = path;
	}
	/* return subject */
	public String getSubject(){
		return this.subject;
	}
	/* Edit Subject */
	public void editSubject(String subject){
		this.subject = subject;
	}
	/* return limit */
	public int getLimit(){
		return this.limit;
	}
	/* return count */
	public int getCount(){
		return this.count;
	}
	public String toString(){
		return this.subject;
	}
	/* return source */
	public String getSource(){
		return this.source;
	}
	/* return destination */
	public String getDest(){
		return this.dest;
	}
	/* return path */
	public LinkedList<Vertex> getPath(){
		return this.path;
	}
}
