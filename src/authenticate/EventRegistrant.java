package authenticate;

import java.util.Vector;

import concertManagement.Concert;

public class EventRegistrant extends Customer {

	Vector<Concert> registeredConcertList;
	Vector<Concert> concertsWaitingForApproval;
	Vector<Concert> concertsWaitingForCancel;
	
	public EventRegistrant(String name, String id, String pw, String contact) {
		super(name, id, pw, contact, Type.EventRegistrant);
		
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
			return null;
		}
		return concertsWaitingForApproval;
	}
	
	public Vector<Concert> cancleRequest(Concert concert) {
		if(concertsWaitingForApproval.contains(concert) ) {
			concertsWaitingForApproval.remove(concert);
		}
		concertsWaitingForCancel.add(concert);
		
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
