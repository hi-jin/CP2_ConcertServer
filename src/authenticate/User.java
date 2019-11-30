package authenticate;

public abstract class User {

	String 	name;
	String 	id;
	String 	pw;
	String 	contact; // phone-number
	Type 	type;
	
	public User(String name, String id, String pw, String contact, Type type) {
		this.name = name;
		this.id = id;
		this.pw = pw;
		this.contact = contact;
		this.type = type;
	}
	
	public String toString() {
		return "user name : " + name + "\nuser type : " + type + "\nid : " + id + "\npw : " + pw + "\nphone-number : " + contact;
	}
}
