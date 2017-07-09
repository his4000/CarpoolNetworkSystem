package Login;

import Client.*;

public class LoginClient {
	private Client client = null;
	
	public LoginClient(Client client){
		this.client = client;
			
		new FrameClientLogin(this.client);
	}
}
