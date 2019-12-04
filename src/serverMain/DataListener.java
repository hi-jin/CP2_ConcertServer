package serverMain;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;

import authenticate.*;

public class DataListener implements Runnable {

	Socket 			clientSocket;
	
	User 			user;
	Manager 		manager;
	EventRegistrant eventRegistrant;
//	TODO
//	Audience 		audience;
	
	public DataListener(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	
	public void run() {
		try {
			PrintStream out = new PrintStream(clientSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(
								new InputStreamReader(
								clientSocket.getInputStream()));
			
			String inputLine;
			String[] command;
			
//			TODO
			while((inputLine = in.readLine()) != null) {
				// command => method name / ... / + params...
				if(inputLine.equalsIgnoreCase("quit")) {
					out.close();
					in.close();
					clientSocket.close();
					break;
				}
				command = inputLine.split("/");
				if(command[0].equalsIgnoreCase("logout")) {
					FileIO.logout(user);
					user = null;
				} 
				if(user == null) {
					// 로그인, 로그아웃
					if(command[0].equalsIgnoreCase("login")) {
						// inputLine = login/id/pw
						user = FileIO.login(command[1], command[2]);
						if(user == null) {
							out.write(-1); // 로그인 실패할 경우 -1을 보낸다.
						} else {
							out.println(user.getName() + "/" + user.getId() + "/" + user.getContact() + "/" + user.getType());
							if(user.getType().equals(Type.ServerManager.toString())) {
								manager = (Manager) user;
//								TODO
							} else if(user.getType().equals(Type.EventRegistrant.toString())) {
								eventRegistrant = (EventRegistrant) user;
//								TODO
							} else if(user.getType().equals(Type.Audience.toString())) {
//								audience = (Audience) user;
//								TODO
							}
						}
					} else if(command[0].equalsIgnoreCase("addAudience")) {
						// inputLine = add~/name/id/pw/contact
//						TODO
					} else if(command[0].equalsIgnoreCase("addManager")) {
						user = new Manager(command[1], command[2], command[3], command[4]);
						FileIO.addUser(user);
					} else if(command[0].equalsIgnoreCase("addEventRegistrant")) {
						// inputLine = addEventRegistrant/name/id/pw/contact
						user = new EventRegistrant(command[1], command[2], command[3], command[4]);
						FileIO.addUser(user);
					}
				} else if(user.getType().equals(Type.ServerManager.toString())){
					if(command[0].equalsIgnoreCase("getConcertList")) {
//						TODO
					} else if(command[0].equalsIgnoreCase("getWaitingList")) {
//						TODO
					} else if(command[0].equalsIgnoreCase("getCancelList")) {
//						TODO
					} else if(command[0].equalsIgnoreCase("cancelConcert")) {
//						TODO
					} else if(command[0].equalsIgnoreCase("addConcert")) {
//						TODO
					}
				} else if(user.getType().equals(Type.EventRegistrant.toString())) {
					if(command[0].equalsIgnoreCase("getRegisteredConcertList")) {
//						TODO
					} else if(command[0].equalsIgnoreCase("getConcertsWaitingForApproval")) {
//						TODO
					} else if(command[0].equalsIgnoreCase("getConcertsWaitingForCancel")) {
//						TODO
					} else if(command[0].equalsIgnoreCase("requestRegistration")) {
//						TODO
					} else if(command[0].equalsIgnoreCase("cancelRequest")) {
//						TODO
					}
				} else if(user.getType().equals(Type.Audience.toString())) {
//					TODO
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
