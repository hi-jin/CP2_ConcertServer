package authenticate;

import java.util.Vector;

import concertManagement.Concert;

public class Audience extends Customer {

	Vector<Concert> reservedConcertList;
	Vector<Integer> seatNumList;
	
	public Audience(String name, String id, String pw, String contact) {
		super(name, id, pw, contact, Type.Audience);
		reservedConcertList = new Vector<>();
		seatNumList = new Vector<>();
	}
	
	public int reserveSeat(Concert concert, int seatNum) {
		if(concert.getSeatPrice() - balance >= 0) {
			if(concert.getSeat().setSeat(seatNum, this)) {
				pay(concert.getSeatPrice());
				reservedConcertList.add(concert);
				seatNumList.add(seatNum);
				concert.getEventRegistrant().receivePayment(concert.getSeatPrice());
				return 1;
			} else {
				return 0;
			}
		} else {
			return -1;
		}
		
	}
	
	public boolean cancelSeat(int index) {
		if(reservedConcertList.get(index).getSeat().cancelSeat(seatNumList.get(index))) {
			reservedConcertList.get(index).getEventRegistrant().pay(reservedConcertList.get(index).getSeatPrice());
			receivePayment(reservedConcertList.get(index).getSeatPrice());
			reservedConcertList.remove(index);
			seatNumList.remove(index);
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
