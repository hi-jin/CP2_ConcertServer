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
			Audience 		audience = null;
//			TODO
			
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
					eventRegistrant = null;
//					audience = null;
					out.close();
					in.close();
					clientSocket.close();
					break;
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
								audience = (Audience) user;
//								TODO
							}
						}
					} else if(command[0].equalsIgnoreCase("addAudience")) {
						user = new Audience(command[1], command[2], command[3], command[4]);
						if(!FileIO.addUser(user)) {
							out.println("-1");
						} else {
							out.println("1");
						}
						user = null;
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
						string.append(manager.getConcertList().get(i).getSeat().getNumberOfSeat() + "/");
						string.append(manager.getConcertList().get(i).getDate());
					}
					string.delete(0, 2);
					System.out.println(string.toString());
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
							string.append(waitingConcert.getDate() + "/");
							string.append(waitingConcert.getSeat().getNumberOfSeat());
						}
						string.delete(0, 2);
						out.println(string.toString());
					} else if(command[0].equalsIgnoreCase("getCancelList")) {
						StringBuilder string = new StringBuilder();
						Vector<Concert> cancelList = manager.getCancelList();
						for(int i = 0; i < cancelList.size(); i++) {
							Concert waitingConcert = cancelList.get(i);
							string.append("//");
							string.append(waitingConcert.getTitle() + "/");
							string.append(waitingConcert.getDate() + "/");
							string.append(waitingConcert.getSeat().getNumberOfSeat());
						}
						string.delete(0, 2);
						out.println(string.toString());
					} else if(command[0].equalsIgnoreCase("cancelConcert")) {
						// inputLine = cancelConcert/index
						Concert concert = manager.getCancelList().get(Integer.parseInt(command[1]));
						manager.cancelConcert(concert);
					} else if(command[0].equalsIgnoreCase("addConcert")) {
						// inputLine = addConcert/index
						Concert concert = manager.getWaitingList().get(Integer.parseInt(command[1]));
						manager.addConcert(concert);
					}
				} else if(user.getType().equals(Type.EventRegistrant.toString())) {
//					System.out.println("TEST 타입 인식:ER");
					if(command[0].equalsIgnoreCase("getRegisteredConcertList")) {
						StringBuilder string = new StringBuilder();
						for(int i = 0; i < eventRegistrant.getRegisteredConcertList().size(); i++) {
							string.append("//");
							string.append(eventRegistrant.getRegisteredConcertList().get(i).getTitle() + "/");
							string.append(eventRegistrant.getRegisteredConcertList().get(i).getSeat().getNumberOfSeat() + "/");
							string.append(eventRegistrant.getRegisteredConcertList().get(i).getDate());
						}
						string.delete(0, 2);
						System.out.println(string.toString());
						out.println(string.toString());
					} else if(command[0].equalsIgnoreCase("getConcertsWaitingForApproval")) {
//						TODO
					} else if(command[0].equalsIgnoreCase("getConcertsWaitingForCancel")) {
//						TODO
					} else if(command[0].equalsIgnoreCase("requestRegistration")) {
						// inputLine = requestRegistration/title/2019-02-12/numOfSeat
						Concert concert = new Concert(
									command[1], command[2],
									new Seat(Integer.parseInt(command[3])), eventRegistrant);
						System.out.println("request");
						if(eventRegistrant.requestRegistration(concert))
							manager.getWaitingList().add(concert);
						out.println(1);
					} else if(command[0].equalsIgnoreCase("cancelRequest")) {
						// inputLine = cancelRequest/index
						Concert concert = eventRegistrant.getRegisteredConcertList().get(Integer.parseInt(command[1]));
						if(eventRegistrant.cancelRequest(concert))
							manager.getCancelList().add(concert);
						out.println(1);
					}
				} else if(user.getType().equals(Type.Audience.toString())) {
					if(command[0].equalsIgnoreCase("getReservedConcertList")) {
						StringBuilder string = new StringBuilder();
						for(int i = 0; i < audience.getReservedConcertList().size(); i++) {
							string.append("//");
							string.append(audience.getReservedConcertList().get(i).getTitle() + "/");
							string.append(audience.getReservedConcertList().get(i).getSeat().getNumberOfSeat() + "/");
							string.append(audience.getReservedConcertList().get(i).getDate() + "/");
							string.append(audience.getSeatNumList().get(i));
						}
						string.delete(0, 2);
						System.out.println(string.toString());
						out.println(string.toString());
					} else if(command[0].equalsIgnoreCase("reserveSeat")) {
						// inputLine = reserveSeat/index/seatNum
						if(audience.reserveSeat(manager.getConcertList().get(Integer.parseInt(command[1])), Integer.parseInt(command[2]))) {
							out.println("1");
						} else {
							out.println("2");
						}
					}
				}
				FileIO.updateUser(user);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
