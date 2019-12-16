package authenticate;

import java.util.Vector;

import concertManagement.Concert;
import serverMain.Main;

public class EventRegistrant extends Customer {

	Vector<Concert> registeredConcertList;
	Vector<Concert> concertsWaitingForApproval;
	Vector<Concert> concertsWaitingForCancel;
	
	public EventRegistrant(String name, String id, String pw, String contact) {
		super(name, id, pw, contact, Type.EventRegistrant);
		registeredConcertList = new Vector<>();
		concertsWaitingForApproval = new Vector<>();
		concertsWaitingForCancel = new Vector<>();
	}
	
	public void addConcert(Concert concert) {
		this.registeredConcertList.add(concert);
		this.concertsWaitingForApproval.remove(concert);
	}
	
	public int requestRegistration(Concert concert) {
		int result;
		if(concertsWaitingForApproval.contains(concert)) {
			return -1;
		}
		if((result = this.pay(concert.getSeat().getNumberOfSeat() * Main.getEventFee())) < 0) {
			System.out.println("잔액부족 : " + result);
		} else {
			concertsWaitingForApproval.add(concert);
			Main.getManager().receivePayment(concert.getSeat().getNumberOfSeat() * Main.getEventFee());
			System.out.println("등록 요청\n" + concert);
		}
		return result;
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
	
	@Override
	public int pay(int amount) {
		if(amount <= balance) {
			balance -= amount;
			return amount;
		} else {
			return balance - amount;
		}
	}
}
