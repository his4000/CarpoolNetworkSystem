package Login;

import java.net.ServerSocket;
import java.net.Socket;
import java.io.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Iterator;

class MemberInfo{
	private Hashtable<String, String> info = null;  //member info<ID.Password> hashtable	
	final private String filename = "./Login/memeber_info.txt";  //member info filename
	
	public MemberInfo(){
		info = new Hashtable<String, String>();
	
		try{
			/* make Hashtable */
			makeHash();
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		
	}
	
	/* make init Hashtable */
	public void makeHash() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(filename));  //make Buffered Reader from file
		String ID;  //ID variable
		String password;  //password variable
		
		while((ID = br.readLine()) != null){
			password = br.readLine();
			info.put(ID, password);
		}		
	}
	/* Insert new member */
	public void InsertMember(String ID, String password){
		info.put(ID, password);
	}
	/* Store in file */
	public void SaveFile(){
		try{
			FileWriter fw = new FileWriter(filename);  //make File Writer to file
			
			Iterator it = info.keySet().iterator();
			while(it.hasNext()){
				String ID = (String)it.next();				
				fw.write(ID+"\n");
				fw.write((String)info.get(ID)+"\n");
			}
			fw.close();
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
	/* Check ID is exist */
	public int CheckID(String id){
		if(info.containsKey(id))
			return 1;  //ID exists
		else
			return -1;  //ID doesn't exist
	}
	/* password check */
	public int Check(String id, String password){
		/* ID is exists */
		if(this.CheckID(id) == 1){
			if(password.equals(info.get(id)))
				return 1;  //password is correct
			else
				return 0;  //password is not correct
		}
		else
			return -1;
	}
}

class LoginThread extends Thread{
	private Socket socket = null;
	private Vector<Socket> vec = null;
	private MemberInfo mem;
	
	public LoginThread(Socket socket, Vector<Socket> vec, MemberInfo mem){
		this.socket = socket;
		this.vec = vec;
		this.mem = mem;
	}
	
	public void run(){
		String id = null;
		String password = null;
		String phoneNumber = null;
		String carIdentify = null;
		String Introduction = null;
		int mode;
		BufferedReader br = null;
		PrintWriter pw = null;
		
		try{
			while(true){
				br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				pw = new PrintWriter(socket.getOutputStream(),true);
				mode = Integer.parseInt(br.readLine());
				/* quit sign input */
				if(mode == -1)
					break;
				/* get ID & password from client */
				id = br.readLine();
				password = br.readLine();

				/* New Member */
				if(mode == 0){
					phoneNumber = br.readLine();
					carIdentify = br.readLine();
					Introduction = br.readLine();
					/* ID already exist */
					if(mem.CheckID(id) == 1)
						pw.println("ID already exist");
					else{
						/* Write Member File */
						mem.InsertMember(id, password);
						this.saveMemberInfo(id, phoneNumber, carIdentify, Introduction);
						pw.println("Registration Complete");
					}
				}
				/* Log-in */
				else if(mode == 1){
					int result = mem.Check(id, password);
					switch(result){
					case 0 : 
						pw.println("Password is not correct");
						pw.println("-1");
						break;
					case 1 : 
						pw.println("Log-in is completed");
						pw.println("0");
						mem.SaveFile();
						vec.remove(socket);
						return;
					case -1 : 
						pw.println("You're not member");
						pw.println("1");
						break;
					}
				}
			}
			
		}catch(IOException e){
			System.out.println(e.getMessage());
		}finally{
			try{
				if(br != null)	br.close();
				if(socket != null)	socket.close();
			}catch(IOException e){
				System.out.println(e.getMessage());
			}
		}
	}
	public void saveMemberInfo(String ID, String phoneNumber, String carIdentify, String Introduction){
		FileWriter fw = null;
		try{
			fw = new FileWriter("./MemberInfo/"+ID+".txt");
			fw.write(phoneNumber+"\n");
			fw.write(carIdentify+"\n");
			fw.write(Introduction+"\n");
			fw.close();
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}
}

public class LoginServer {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ServerSocket server = null;
		Socket socket = null;
		MemberInfo mem = new MemberInfo();
		Vector<Socket> vec = new Vector<Socket>();
		
		try{
			server = new ServerSocket(4000);
			while(true){
				System.out.println("Login Server waiting...");
				socket = server.accept();
				vec.add(socket);
				
				new LoginThread(socket, vec, mem).start();	
			}
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		
	}

}
