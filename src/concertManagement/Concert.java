package concertManagement;

import java.util.Date;

public class Concert {

	private String title;
	private String description;
	private String date;
	private Seat seat;
	
	public Concert(String title, String description, String date, Seat seat) {
		super();
		this.title = title;
		this.description = description;
		this.date = date; // 20190212
		this.seat = seat;
	}
	
	public String getTitle() {
		return title;
	}
	public String getDate() {
		return date;
	}
	public Seat getSeat() {
		return seat;
	}
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public void setSeat(Seat seat) {
		this.seat = seat;
	}
	
	public String toString() {
		return "title : " + this.title + "\ndescription : " + this.description + "\ndate : " + this.date + "\nseat : " + this.seat;
	}
}
