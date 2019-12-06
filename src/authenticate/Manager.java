	package authenticate;

import java.util.Vector;
import java.util.Iterator;
import java.util.List;

import concertManagement.Concert;

//TODO 기능 추가하기.
public class Manager extends User{

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

	public Vector<Concert> addConcert(Concert concert) {
		this.getWaitingList();
		Iterator<EventRegistrant> it = eventRegistrantList.iterator();
		int index = -1;
		while(it.hasNext()) {
			EventRegistrant eventRegistrant = it.next();
			System.out.println(eventRegistrant);
			if((index = eventRegistrant.concertsWaitingForApproval.indexOf(concert)) != -1) {
				eventRegistrant.concertsWaitingForApproval.remove(index);
				eventRegistrant.registeredConcertList.add(concert);
				
				index = this.concertsWaitingForApproval.indexOf(concert);
				this.concertsWaitingForApproval.remove(index);
				this.registeredConcertList.add(concert);
				System.out.println("추가 완료\n" + concert);
				return this.registeredConcertList;
			}
		}
		System.out.println("index : " + index);
		System.out.println("콘서트 추가 오류");
		return null;
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
		Iterator<Concert> it = registeredConcertList.iterator();
//		while(it.hasNext()) {
//			System.out.println(it.next());
//		}
		
		return registeredConcertList;
	}
	
//	TODO printWaitingList method
	public Vector<Concert> getWaitingList() {
		List<User> userList = FileIO.getUserList();
		Iterator<User> it = userList.iterator();

		EventRegistrant eventRegistrant;
		while(it.hasNext()) {
			User user = it.next();
			if(user.type.equals(Type.EventRegistrant)) {
				eventRegistrant = (EventRegistrant) user;
				eventRegistrantList.add(eventRegistrant);
				for(int i = 0; i < eventRegistrant.concertsWaitingForApproval.size(); i++) {
					concertsWaitingForApproval.add(eventRegistrant.concertsWaitingForApproval.get(i));
				}
			}
		}
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
