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

public class DataListener extends Thread {

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
							out.flush();
							System.out.println("로그인 실패");
						} else {
							System.out.println("로그인\n" + user.getName() + "/" + user.getId() + "/" + user.getContact() + "/" + user.getType());
							out.println(user.getName() + "/" + user.getId() + "/" + user.getContact() + "/" + user.getType() + "/" + user.getBalance() + "/" + user.getMsgFromServer());
							out.flush();
							user.clearMsg();
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
						out.flush();
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
						out.flush();
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
						out.flush();
						user = null;
						continue;
					}
				} else if(command[0].equalsIgnoreCase("getAllConcertList")) {
					StringBuilder string = new StringBuilder();
					for(int i = 0; i < manager.getConcertList().size(); i++) {
						string.append("//");
						string.append(manager.getConcertList().get(i).getTitle() + "/");
						string.append(manager.getConcertList().get(i).getSeat().getNumberOfSeat() + "/");
						string.append(manager.getConcertList().get(i).getDate() + "/");
						string.append(Arrays.toString(manager.getConcertList().get(i).getSeat().getSeats()) + "/");
						string.append(manager.getConcertList().get(i).getSeat().getReservedSeatCount());
//						좌석 현황도 보내도록 업데이트 하기.
					}
					string.delete(0, 2);
					System.out.println(string.toString());
					out.println(string.toString());
					out.flush();
				} else if(command[0].equalsIgnoreCase("getBalance")) {
					out.println(user.getBalance() + "");
					out.flush();
				} else if(command[0].equalsIgnoreCase("chargeMoney")) {
					out.println(user.chargeMoney(Integer.parseInt(command[1])) + "");
					out.flush();
				} else if(command[0].equalsIgnoreCase("refundMoney")) {
					out.println(user.refundMoney(Integer.parseInt(command[1])) + "");
					out.flush();
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
							string.append(waitingConcert.getSeat().getNumberOfSeat() + "");
						}
						string.delete(0, 2);
						out.println(string.toString());
						out.flush();
					} else if(command[0].equalsIgnoreCase("getCancelList")) {
						StringBuilder string = new StringBuilder();
						Vector<Concert> cancelList = manager.getCancelList();
						for(int i = 0; i < cancelList.size(); i++) {
							Concert waitingConcert = cancelList.get(i);
							string.append("//");
							string.append(waitingConcert.getTitle() + "/");
							string.append(waitingConcert.getDate() + "/");
							string.append(waitingConcert.getSeat().getNumberOfSeat() + "");
						}
						string.delete(0, 2);
						out.println(string.toString());
						out.flush();
					} else if(command[0].equalsIgnoreCase("cancelConcert")) {
						// inputLine = cancelConcert/index
						Concert concert = manager.getCancelList().get(Integer.parseInt(command[1]));
						concert.getEventRegistrant().setMsgFromServer("취소 요청한 콘서트가 취소되었습니다. " + "제목 : " + concert.getTitle());
						manager.cancelConcert(concert);
					} else if(command[0].equalsIgnoreCase("addConcert")) {
						// inputLine = addConcert/index
						Concert concert = manager.getWaitingList().get(Integer.parseInt(command[1]));
						concert.getEventRegistrant().setMsgFromServer("요청한 콘서트가 등록되었습니다. " + "제목 : " + concert.getTitle());
						manager.addConcert(concert);
					} else if(command[0].equalsIgnoreCase("rejectAddRequest")) {
						// inputLine = rejectAddRequest/index
						Concert concert = manager.getWaitingList().get(Integer.parseInt(command[1]));
						concert.getEventRegistrant().setMsgFromServer("콘서트 등록 요청이 거부되었습니다. 제목 : " + concert.getTitle());
						out.println(manager.rejectAddRequest(concert));
					} else if(command[0].equalsIgnoreCase("rejectCancelRequest")) {
						Concert concert = manager.getCancelList().get(Integer.parseInt(command[1]));
						concert.getEventRegistrant().setMsgFromServer("콘서트 취소 요청이 거부되었습니다. 제목 : " + concert.getTitle());
						manager.rejectCancelRequest(concert);
					}
				} else if(user.getType().equals(Type.EventRegistrant.toString())) {
//					System.out.println("TEST 타입 인식:ER");
					if(command[0].equalsIgnoreCase("getRegisteredConcertList")) {
						StringBuilder string = new StringBuilder();
						for(int i = 0; i < eventRegistrant.getRegisteredConcertList().size(); i++) {
							string.append("//");
							string.append(eventRegistrant.getRegisteredConcertList().get(i).getTitle() + "/");
							string.append(eventRegistrant.getRegisteredConcertList().get(i).getSeat().getNumberOfSeat() + "/");
							string.append(eventRegistrant.getRegisteredConcertList().get(i).getDate() + "/");
							string.append(Arrays.toString(eventRegistrant.getRegisteredConcertList().get(i).getSeat().getSeats()) + "/");
							string.append(eventRegistrant.getRegisteredConcertList().get(i).getSeat().getReservedSeatCount() + "");
						}
						string.delete(0, 2);
						System.out.println(string.toString());
						out.println(string.toString());
						out.flush();
					} else if(command[0].equalsIgnoreCase("getConcertsWaitingForApproval")) {
//						TODO
					} else if(command[0].equalsIgnoreCase("getConcertsWaitingForCancel")) {
//						TODO
					} else if(command[0].equalsIgnoreCase("requestRegistration")) {
						// inputLine = requestRegistration/title/2019-02-12/numOfSeat
						Concert concert = new Concert(
									command[1], command[2],
									new Seat(Integer.parseInt(command[3])), eventRegistrant);
						int result;
						if((result = eventRegistrant.requestRegistration(concert)) >= 0 ) {
							manager.setMsgFromServer("콘서트 등록 요청이 있습니다. " + "제목 : " + concert.getTitle());
							manager.getWaitingList().add(concert);
						}
						out.println(result + "");
						out.flush();
					} else if(command[0].equalsIgnoreCase("cancelRequest")) {
						// inputLine = cancelRequest/index
						Concert concert = eventRegistrant.getRegisteredConcertList().get(Integer.parseInt(command[1]));
						if(eventRegistrant.cancelRequest(concert)) {
							manager.setMsgFromServer("콘서트 취소 요청이 있습니다. " + "제목 : " + concert.getTitle());
							manager.getCancelList().add(concert);
						}
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
						out.flush();
					} else if(command[0].equalsIgnoreCase("reserveSeat")) {
						// inputLine = reserveSeat/index/seatNum
						if(audience.reserveSeat(manager.getConcertList().get(Integer.parseInt(command[1])), Integer.parseInt(command[2]))) {
							out.println("1");
						} else {
							out.println("2");
						}
						out.flush();
					}
				}
				FileIO.updateUser(user);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
