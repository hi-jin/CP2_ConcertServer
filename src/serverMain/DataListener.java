package serverMain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import authenticate.*;

public class DataListener implements Runnable {

	Socket clientSocket;
	User user;
	
	public DataListener(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	
	public void run() {
		try {
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(
								new InputStreamReader(
								clientSocket.getInputStream()));
			
			String inputLine;
			String[] command;
			
//			TODO
			while((inputLine = in.readLine()) != null) {
//				command => method name / ... / + params...
				command = inputLine.split("/");
				if(command[0].equalsIgnoreCase("login")) {
//					inputLine = login/id/pw
					user = FileIO.login(command[1], command[2]);
				} else if(command[0].equalsIgnoreCase("addAudience")) {
//					inputLine = addAudience/name/id/pw/contact
//					TODO
//					user = FileIO.addUser(new Audience());
				} else if(command[0].equalsIgnoreCase("addManager")) {
					user = new Manager(command[1], command[2], command[3], command[4]);
					FileIO.addUser(user);
				} else if(command[0].equalsIgnoreCase("addEventRegistrant")) {
//					inputLine = addEventRegistrant/name/id/pw/contact
					user = new EventRegistrant(command[1], command[2], command[3], command[4]);
					FileIO.addUser(user);
				} else if(command[0].equalsIgnoreCase("logout")) {
					FileIO.logout(user);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
