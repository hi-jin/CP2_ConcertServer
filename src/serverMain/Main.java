package serverMain;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;

import authenticate.FileIO;
import authenticate.Manager;
import authenticate.User;

public class Main {

	static Manager manager;
	static final int EVENT_REQUEST_FEE_PER_SEAT = 1000;
	
	private static Vector<DataListener> clientList = null;
	
	public static int getEventFee() {
		return EVENT_REQUEST_FEE_PER_SEAT;
	}
	
	public static Manager getManager() {
		return manager;
	}
	
	public static void main(String[] args) throws IOException {
		Socket 			clientSocket = null;
		ServerSocket 	serverSocket = null;
		clientList = new Vector<>();
		
		manager = new Manager("defualtManager", "defualtManager", "defualtManager", "defualtManager");
		
		FileIO.readUserList();
		Iterator<User> it = FileIO.getUserList().iterator();
		while(it.hasNext()) {
			User user = it.next();
			if(user.getType().equals(authenticate.Type.ServerManager.toString())) {
				manager = (Manager) user;
				break;
			}
		}
			try {
				serverSocket = new ServerSocket(50000);
				
				while(true) {
					clientSocket = serverSocket.accept();
					System.out.println("Client From : " + clientSocket.getInetAddress());
					
					Thread client = new DataListener(clientSocket);
					
					client.setName("Client " + clientSocket.getInetAddress());
					client.start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
