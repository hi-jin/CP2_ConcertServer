package serverMain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Arrays;

import authenticate.*;
import concertManagement.Concert;
import concertManagement.Seat;

public class DataListener implements Runnable {

	Socket 			clientSocket;
	
	public DataListener(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	
	public void run() {
		try {
			User 			user = null;
			Manager 		manager = null;
			EventRegistrant eventRegistrant = null;
//			TODO
//			Audience 		audience = null;
			
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
							System.out.println("로그인 실패");
						} else {
							System.out.println("로그인\n" + user.getName() + "/" + user.getId() + "/" + user.getContact() + "/" + user.getType());
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
						continue;
					} else if(command[0].equalsIgnoreCase("addManager")) {
						user = new Manager(command[1], command[2], command[3], command[4]);
						if(!FileIO.addUser(user)) {
							out.println("-1");
						} else {
							out.println("1");
						}
						user = null;
						continue;
					} else if(command[0].equalsIgnoreCase("addEventRegistrant")) {
						// inputLine = addEventRegistrant/name/id/pw/contact
						user = new EventRegistrant(command[1], command[2], command[3], command[4]);
						if(!FileIO.addUser(user)) {
							out.println("-1");
						} else {
							out.println("1"); 
						}
						user = null;
						continue;
					}
				} else if(user.getType().equals(Type.ServerManager.toString())){
//					System.out.println("TEST 타입 인식:Manager");
					if(command[0].equalsIgnoreCase("getConcertList")) {
//						TODO
					} else if(command[0].equalsIgnoreCase("getWaitingList")) {
//						TODO
					} else if(command[0].equalsIgnoreCase("getCancelList")) {
//						TODO
					} else if(command[0].equalsIgnoreCase("cancelConcert")) {
						// inputLine = cancelConcert/title/20190212/numOfSeat
						Concert concert = new Concert(
								command[1], command[2],
								new Seat(Integer.parseInt(command[3])));
						manager.cancelConcert(concert);
					} else if(command[0].equalsIgnoreCase("addConcert")) {
						// inputLine = addConcert/title/20190212/numOfSeat
						Concert concert = new Concert(
								command[1], command[2],
								new Seat(Integer.parseInt(command[3])));
						manager.addConcert(concert);
					}
				} else if(user.getType().equals(Type.EventRegistrant.toString())) {
//					System.out.println("TEST 타입 인식:ER");
					if(command[0].equalsIgnoreCase("getRegisteredConcertList")) {
//						TODO
					} else if(command[0].equalsIgnoreCase("getConcertsWaitingForApproval")) {
//						TODO
					} else if(command[0].equalsIgnoreCase("getConcertsWaitingForCancel")) {
//						TODO
					} else if(command[0].equalsIgnoreCase("requestRegistration")) {
						// inputLine = requestRegistration/title/20190212/numOfSeat
						Concert concert = new Concert(
									command[1], command[2],
									new Seat(Integer.parseInt(command[3])));
						System.out.println("request");
						eventRegistrant.requestRegistration(concert);
						out.println(1);
					} else if(command[0].equalsIgnoreCase("cancelRequest")) {
						// inputLine = requestRegistration/title/20190212/numOfSeat
						Concert concert = new Concert(
								command[1], command[2],
								new Seat(Integer.parseInt(command[3])));
						eventRegistrant.cancelRequest(concert);
						out.println(1);
					}
				} else if(user.getType().equals(Type.Audience.toString())) {
//					TODO
				}
				FileIO.updateUser(user);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
