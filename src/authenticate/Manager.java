	package authenticate;

import java.util.Vector;
import java.util.Iterator;
import java.util.List;

import concertManagement.Concert;

//TODO 기능 추가하기.
public class Manager extends User {

	Vector<Concert> registeredConcertList;
	Vector<Concert> concertsWaitingForApproval;
	Vector<Concert> concertsWaitingForCancel;
	Vector<EventRegistrant> eventRegistrantList;
	
	public Manager(String name, String id, String pw, String contact) {
		super(name, id, pw, contact, Type.ServerManager);
		registeredConcertList = new Vector<>();
		concertsWaitingForApproval = new Vector<>();
		eventRegistrantList = new Vector<>();
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public void setPw(String pw) {
		this.pw = pw;
	}
	
	public void setContact(String contact) {
		this.contact = contact;
	}

	public Vector<Concert> addConcert(Concert concert) {
		EventRegistrant er = concert.getEventRegistrant();
		er.getRegisteredConcertList().add(concert);
		er.getConcertsWaitingForApproval().remove(concert);
		this.concertsWaitingForApproval.remove(concert);
		this.registeredConcertList.add(concert);
		
		return registeredConcertList;
	}
	
	public Vector<Concert> cancelConcert(Concert concert) {
		Iterator<EventRegistrant> it = eventRegistrantList.iterator();
		int index = -1;
		while(it.hasNext()) {
			EventRegistrant eventRegistrant = it.next();
			if((index = eventRegistrant.concertsWaitingForCancel.indexOf(concert)) != -1) {
				eventRegistrant.concertsWaitingForCancel.remove(index);
				eventRegistrant.registeredConcertList.remove(concert);
				
				index = this.concertsWaitingForCancel.indexOf(concert);
				this.concertsWaitingForCancel.remove(index);
				this.registeredConcertList.remove(concert);
				System.out.println("취소 완료\n" + concert);
				break;
			}
		}
		return this.registeredConcertList;
	}
	
	public Vector<Concert> getConcertList() {
		return registeredConcertList;
	}
	
//	TODO printWaitingList method
	public Vector<Concert> getWaitingList() {
		return concertsWaitingForApproval;
	}
	
	public Vector<Concert> getCancelList() {
		List<User> userList = FileIO.getUserList();
		Iterator<User> it = userList.iterator();

		EventRegistrant eventRegistrant;
		while(it.hasNext()) {
			User user = it.next();
			if(user.type.equals(Type.EventRegistrant)) {
				eventRegistrant = (EventRegistrant) user;
				for(int i = 0; i < eventRegistrant.concertsWaitingForCancel.size(); i++) {
					concertsWaitingForApproval.add(eventRegistrant.concertsWaitingForCancel.get(i));
				}
			}
		}
		return concertsWaitingForCancel;
	}
}
