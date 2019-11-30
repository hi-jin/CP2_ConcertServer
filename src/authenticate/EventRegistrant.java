package authenticate;

import java.util.ArrayList;

import concertManagement.Concert;

public class EventRegistrant extends Customer {

	ArrayList<Concert> registeredConcertList;
	ArrayList<Concert> concertsWaitingForApproval;
	ArrayList<Concert> concertsWaitingForCancel;
	
	public EventRegistrant(String name, String id, String pw, String contact) {
		super(name, id, pw, contact, Type.EventRegistrant);
		
		registeredConcertList = new ArrayList<>();
		concertsWaitingForApproval = new ArrayList<>();
	}
	
	public EventRegistrant(String name, String id, String pw, String contact, int balance) {
		super(name, id, pw, contact, Type.EventRegistrant, balance);

		registeredConcertList = new ArrayList<>();
		concertsWaitingForApproval = new ArrayList<>();
	}
	
	public ArrayList<Concert> requestRegistration(Concert concert) {
		if(balance > 500) {
			concertsWaitingForApproval.add(concert);
		} else {
			return null;
		}
		return concertsWaitingForApproval;
	}
	
	public ArrayList<Concert> cancleRequest(Concert concert) {
		if(concertsWaitingForApproval.contains(concert) ) {
			concertsWaitingForApproval.remove(concert);
		}
		concertsWaitingForCancel.add(concert);
		
		return concertsWaitingForCancel;
	}
}
