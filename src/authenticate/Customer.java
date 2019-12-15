package authenticate;

import serverMain.Main;

public abstract class Customer extends User {

	public Customer(String name, String id, String pw, String contact, Type type) {
		super(name, id, pw, contact, type, 0);
	}
	
	public Customer(String name, String id, String pw, String contact, Type type, int balance) {
		super(name, id, pw, contact, type, balance);
		this.balance = balance;
	}
	
	public int pay(int amount) {
		if(amount <= balance) {
			balance -= amount;
			Main.getManager().receivePayment(amount);
			return amount;
		} else {
			return balance - amount;
		}
	}
	
	public void receivePayment(int amount) {
		balance += amount;
	}
}
