package serverMain;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import authenticate.FileIO;
import authenticate.Manager;

public class Main {

	static Manager manager;
	
	public static void main(String[] args) throws IOException {
		Socket 			clientSocket = null;
		ServerSocket 	serverSocket = null;
		
		FileIO.readUserList();
		
		manager = new Manager("defualtManager", "defualtManager", "defualtManager", "defualtManager");
			try {
				serverSocket = new ServerSocket(50000);
				
				while(true) {
					clientSocket = serverSocket.accept();
					System.out.println("Client From : " + clientSocket.getInetAddress());
					
					Thread client = new Thread(new DataListener(clientSocket));
					client.setName("Client " + clientSocket.getInetAddress());
					client.start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
