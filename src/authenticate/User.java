package authenticate;

import java.io.Serializable;

public abstract class User implements Serializable {

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
	
	public String getType() {
		return type.toString();
	}
	
	public String getName() {
		return name;
	}

	public String getId() {
		return id;
	}

	public String getPw() {
		return pw;
	}

	public String getContact() {
		return contact;
	}
}
