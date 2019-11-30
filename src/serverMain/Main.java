package serverMain;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

	public static void main(String[] args) {
		Socket 			clientSocket = null;
		ServerSocket 	serverSocket = null;
		
			try {
				serverSocket = new ServerSocket(50000);
				
				while(true) {
					clientSocket = serverSocket.accept();
					System.out.println("Client From : " + clientSocket.getInetAddress());
					
					new Thread(new DataListener(clientSocket)).start();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
