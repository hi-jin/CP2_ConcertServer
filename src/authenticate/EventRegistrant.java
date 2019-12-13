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
		concertsWaitingForCancel = new Vector<>();
	}
	
	public void addConcert(Concert concert) {
		this.registeredConcertList.add(concert);
		this.concertsWaitingForApproval.remove(concert);
	}
	
	public boolean requestRegistration(Concert concert) {
		if(balance > 500) {
			if(concertsWaitingForApproval.contains(concert)) {
				return false;
			}
			concertsWaitingForApproval.add(concert);
			System.out.println("등록 요청\n" + concert);
			return true;
		} else {
			System.out.println("잔액부족" + balance);
			return false;
		}
	}
	
	public boolean cancelRequest(Concert concert) {
		if(concertsWaitingForCancel.contains(concert) ) {
			return false;
		} else {
			concertsWaitingForCancel.add(concert);
		}
		System.out.println("취소 요청\n" + concert);
		return true;
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
