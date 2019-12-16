package authenticate;

import java.io.Serializable;

public abstract class User implements Serializable {

	String 	name;
	String 	id;
	String 	pw;
	String 	contact; // phone-number
	Type 	type;
	int		balance;
	String	msgFromServer = "";
	
	public abstract int pay(int amount);
	
	public User(String name, String id, String pw, String contact, Type type, int balance) {
		this.name = name;
		this.id = id;
		this.pw = pw;
		this.contact = contact;
		this.type = type;
		this.balance = balance;
	}
	
	public int chargeMoney(int amount) {
		if(amount > 0) {
			balance += amount;
			return balance;
		} else {
			return -1;
		}
	}
	
	public int refundMoney(int amount) {
		if(balance - amount >= 0 && amount > 0) {
			balance -= amount;
			return balance;
		} else {
			return -1;
		}
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
	
	public String getMsgFromServer() {
		return msgFromServer;
	}

	public int getBalance() {
		return balance;
	}
	
	public void setMsgFromServer(String msg) {
		this.msgFromServer += msg + "=";
	}
	
	public void clearMsg() {
		this.msgFromServer = "";
	}
	
	@Override
	public boolean equals(Object o) {
		User c = (User) o;
		if(this.id.equals(c.id) && this.pw.equals(c.pw))
			return true;
		return false;
	}
}
