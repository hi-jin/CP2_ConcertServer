package authenticate;

public abstract class Customer extends User {

	protected int balance;

	public Customer(String name, String id, String pw, String contact, Type type) {
		super(name, id, pw, contact, type);
		balance = 0;
	}
	
	public Customer(String name, String id, String pw, String contact, Type type, int balance) {
		super(name, id, pw, contact, type);
		this.balance = balance;
	}
	
	public int chargeBalance(int amount) {
		if(balance > 0) {
			balance += amount;
			return balance;
		} else {
			return -1;
		}
		
	}
	
	public int pay(int amount) {
		if(amount <= balance && amount > 0) {
			balance -= amount;
			return balance;
		} else {
			return -1;
		}
	}
	
	public int printBalance() {
		return balance;
	}
}
