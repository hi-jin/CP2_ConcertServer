package authenticate;

import java.util.Vector;

import concertManagement.Concert;

public class EventRegistrant extends Customer {

	Vector<Concert> registeredConcertList;
	Vector<Concert> concertsWaitingForApproval;
	Vector<Concert> concertsWaitingForCancel;
	
	public EventRegistrant(String name, String id, String pw, String contact) {
		super(name, id, pw, contact, Type.EventRegistrant, 10000);
		registeredConcertList = new Vector<>();
		concertsWaitingForApproval = new Vector<>();
	}
	
	public EventRegistrant(String name, String id, String pw, String contact, int balance) {
		super(name, id, pw, contact, Type.EventRegistrant, balance);

		concertsWaitingForApproval = new Vector<>();
	}
	
	public Vector<Concert> requestRegistration(Concert concert) {
		if(balance > 500) {
			concertsWaitingForApproval.add(concert);
		} else {
			System.out.println("잔액부족" + balance);
			return null;
		}
		System.out.println("등록 요청\n" + concert);
		return concertsWaitingForApproval;
	}
	
	public Vector<Concert> cancelRequest(Concert concert) {
		if(concertsWaitingForApproval.contains(concert) ) {
			concertsWaitingForApproval.remove(concert);
		} else {
			concertsWaitingForCancel.add(concert);
		}
		System.out.println("취소 요청\n" + concert);
		return concertsWaitingForCancel;
	}
	
	public Vector<Concert> getRegisteredConcertList() {
		return registeredConcertList;
	}
	
	public Vector<Concert> getConcertsWaitingForApproval() {
		return concertsWaitingForApproval;
	}
	
	public Vector<Concert> getConcertsWaitingForCancel() {
		return concertsWaitingForCancel;
	}
}
