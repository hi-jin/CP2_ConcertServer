	package authenticate;

import java.util.Vector;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import concertManagement.Concert;
import serverMain.Main;

//TODO 기능 추가하기.
public class Manager extends User {

	Vector<Concert> registeredConcertList;
	Vector<Concert> concertsWaitingForApproval;
	Vector<Concert> concertsWaitingForCancel;
	Vector<EventRegistrant> eventRegistrantList;
	
	public Manager(String name, String id, String pw, String contact) {
		super(name, id, pw, contact, Type.ServerManager, 0);
		registeredConcertList = new Vector<>();
		concertsWaitingForApproval = new Vector<>();
		concertsWaitingForCancel = new Vector<>();
		eventRegistrantList = new Vector<>();
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setPw(String pw) {
		this.pw = pw;
	}
	
	public void setContact(String contact) {
		this.contact = contact;
	}

	public void receivePayment(int amount) {
		this.balance += amount;
	}
	
	public void pay(int amount) {
		this.balance -= amount;
	}
	
	public Vector<Concert> addConcert(Concert concert) {
		EventRegistrant er = concert.getEventRegistrant();
		System.out.println(er);
		er.getRegisteredConcertList().add(concert);
		er.getConcertsWaitingForApproval().remove(concert);
		this.concertsWaitingForApproval.remove(concert);
		this.registeredConcertList.add(concert);
		
		System.out.println(Arrays.deepToString(registeredConcertList.toArray()));
		return registeredConcertList;
	}
	
	public Vector<Concert> cancelConcert(Concert concert) {
		EventRegistrant er = concert.getEventRegistrant();
		System.out.println(er);
		er.getRegisteredConcertList().remove(concert);
		er.getConcertsWaitingForCancel().remove(concert);
		this.concertsWaitingForCancel.remove(concert);
		this.registeredConcertList.remove(concert);
		int receiveAmount = (int)(concert.getSeat().getNumberOfSeat() * Main.getEventFee() * 2 / 3.0);
		this.pay(receiveAmount);
		er.receivePayment(receiveAmount);
		
		Audience[] audienceList = concert.getSeat().getAudienceList();
		for(int i = 0; i < audienceList.length; i++) {
			if(audienceList[i] != null) {
				audienceList[i].cancelSeat(i);
			}
		}
		
		System.out.println(Arrays.deepToString(registeredConcertList.toArray()));
		return registeredConcertList;
	}
	
	public Vector<Concert> getConcertList() {
		return registeredConcertList;
	}
	
//	TODO printWaitingList method
	public Vector<Concert> getWaitingList() {
		return concertsWaitingForApproval;
	}
	
	public Vector<Concert> getCancelList() {
		return concertsWaitingForCancel;
	}
	
	public int rejectAddRequest(Concert concert) {
		EventRegistrant er = concert.getEventRegistrant();
		this.concertsWaitingForApproval.remove(concert);
		er.concertsWaitingForApproval.remove(concert);
		int receiveAmount = (int)(concert.getSeat().getNumberOfSeat() * Main.getEventFee());
		this.pay(receiveAmount);
		er.receivePayment(receiveAmount);
		
		return balance;
	}

	public void rejectCancelRequest(Concert concert) {
		EventRegistrant er = concert.getEventRegistrant();
		this.concertsWaitingForCancel.remove(concert);
		er.concertsWaitingForCancel.remove(concert);
	}
}
