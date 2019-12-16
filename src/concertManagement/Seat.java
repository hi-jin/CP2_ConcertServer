package concertManagement;

import java.io.Serializable;
import java.util.Arrays;

import authenticate.Audience;
import authenticate.User;

public class Seat implements Serializable {

	private int 		numberOfSeat;
	private int			reservedSeatCount;
	private int[] 		seats;
	private Audience[] 	audienceList;

	public Seat(int numberOfSeat) {
		super();
		this.numberOfSeat = numberOfSeat;
		this.seats = new int[numberOfSeat];
		this.reservedSeatCount = 0;
		this.audienceList = new Audience[numberOfSeat];
		for(int i = 0; i < numberOfSeat; i++) {
			seats[i] = 0;
		}
	}

	public Audience[] getAudienceList() {
		return this.audienceList;
	}
	
	public int getNumberOfSeat() {
		return numberOfSeat;
	}

	public void setNumberOfSeat(int numberOfSeat) {
		this.numberOfSeat = numberOfSeat;
	}
	
	public int[] getSeats() {
		return seats;
	}
	
	public int getReservedSeatCount() {
		return this.reservedSeatCount;
	}
	
	public synchronized boolean setSeat(int i, Audience audience) {
		if(seats[i] != 1) {
			seats[i] = 1;
			this.reservedSeatCount++;
			this.audienceList[i] = new Audience(null, null, null, null);
			this.audienceList[i] = audience;
			return true;
		}
		return false;
	}
	
	public synchronized boolean cancelSeat(int i) {
		if(seats[i] == 1) {
			seats[i] = 0;
			this.reservedSeatCount--;
			this.audienceList[i] = null;
			return true;
		} else {
			return false;
		}
	}
	
	public String toString() {
		return "numberOfSeat : " + this.numberOfSeat + "\nSeats : \n" + Arrays.toString(this.seats);
	}
	
	@Override
	public boolean equals(Object o) {
		Seat s = (Seat) o;
		if(this.numberOfSeat == s.numberOfSeat)
			return true;
		return false;
	}
}
