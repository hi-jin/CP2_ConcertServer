package serverMain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Vector;

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
			Manager			manager = Main.manager;
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
							out.println("-1"); // 로그인 실패할 경우 -1을 보낸다.
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
						manager.setName(command[1]);
						manager.setId(command[2]);
						manager.setPw(command[3]);
						manager.setContact(command[4]);
						user = manager;
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
				} else if(command[0].equalsIgnoreCase("getAllConcertList")) {
					StringBuilder string = new StringBuilder();
					for(int i = 0; i < manager.getConcertList().size(); i++) {
						string.append("//");
						string.append(manager.getConcertList().get(i).getTitle() + "/");
						string.append(manager.getConcertList().get(i).getSeat() + "/");
						string.append(manager.getConcertList().get(i).getDate());
					}
					string.delete(0, 2);
					out.println(string.toString());
				} else if(user.getType().equals(Type.ServerManager.toString())){
//					System.out.println("TEST 타입 인식:Manager");
					if(command[0].equalsIgnoreCase("getWaitingList")) {
						StringBuilder string = new StringBuilder();
						Vector<Concert> waitingList = manager.getWaitingList();
						for(int i = 0; i < waitingList.size(); i++) {
							Concert waitingConcert = waitingList.get(i);
							string.append("//");
							string.append(waitingConcert.getTitle() + "/");
							string.append(waitingConcert.getSeat().getNumberOfSeat() + "/");
							string.append(waitingConcert.getDate());
						}
						string.delete(0, 2);
						out.println(string.toString());
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
						// inputLine = requestRegistration/title/2019-02-12/numOfSeat
						Concert concert = new Concert(
									command[1], command[2],
									new Seat(Integer.parseInt(command[3])));
						System.out.println("request");
						eventRegistrant.requestRegistration(concert);
						manager.getWaitingList().add(concert);
						out.println(1);
					} else if(command[0].equalsIgnoreCase("cancelRequest")) {
						// inputLine = requestRegistration/title/2019-02-12/numOfSeat
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
