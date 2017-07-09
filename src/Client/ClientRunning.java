package Client;

import java.io.IOException;
import java.net.Socket;

import Login.LoginClient;

public class ClientRunning {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Client client = null;
		
		try{
			/* Make Client Object */
			client = new Client(null, null, new Socket("127.0.0.1", 4000));
		
			/* run Log-inClient session */
			new LoginClient(client);
			
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
	}

}
