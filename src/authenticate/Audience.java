package authenticate;

import java.util.Vector;

import concertManagement.Concert;

public class Audience extends Customer {

	Vector<Concert> reservedConcertList;
	Vector<Integer> seatNumList;
	
	public Audience(String name, String id, String pw, String contact) {
		super(name, id, pw, contact, Type.Audience, 10000);
		reservedConcertList = new Vector<>();
		seatNumList = new Vector<>();
	}
	
	public boolean reserveSeat(Concert concert, int seatNum) {
		if(concert.getSeat().setSeat(seatNum)) {
			reservedConcertList.add(concert);
			seatNumList.add(seatNum);
			return true;
		} else {
			return false;
		}
	}
	
	public Vector<Concert> getReservedConcertList() {
		return reservedConcertList;
	}
	
	public Vector<Integer> getSeatNumList() {
		return seatNumList;
	}
}
