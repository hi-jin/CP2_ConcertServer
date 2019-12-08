package concertManagement;

import java.io.Serializable;
import java.util.Arrays;

public class Seat implements Serializable {

	private int 	numberOfSeat;
	private int[] 	seats;

	public Seat(int numberOfSeat) {
		super();
		this.numberOfSeat = numberOfSeat;
		this.seats = new int[numberOfSeat];
		for(int i = 0; i < numberOfSeat; i++) {
			seats[i] = 0;
		}
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
	
	public synchronized boolean setSeat(int i) {
		if(seats[i] != 1) {
			seats[i] = 1;
			return true;
		}
		return false;
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
