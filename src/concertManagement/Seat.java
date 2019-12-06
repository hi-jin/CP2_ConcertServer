package concertManagement;

public class Seat {

	private int numberOfSeat;

	public Seat(int numberOfSeat) {
		super();
		this.numberOfSeat = numberOfSeat;
	}

	public int getNumberOfSeat() {
		return numberOfSeat;
	}

	public void setNumberOfSeat(int numberOfSeat) {
		this.numberOfSeat = numberOfSeat;
	}
	
	public String toString() {
		return "numberOfSeat : " + this.numberOfSeat;
	}
	
	@Override
	public boolean equals(Object o) {
		Seat s = (Seat) o;
		if(this.numberOfSeat == s.numberOfSeat)
			return true;
		return false;
	}
}
