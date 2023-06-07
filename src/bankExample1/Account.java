package bankExample1;

public class Account {
	private double balance;
	
	public Account (double _balance) {
		balance = _balance;
	}
	
	public void deposit (double amount) {
		balance +=amount;
	}
	
	public void withdraw (double amount) {
		balance -=amount;
	}
	
	public double getBalance () {
		return balance;
	}
	
	public void setBalance (double _balance) {
		balance = _balance;
	}
	
	public void update (double amount) {
		
		balance += amount;
		
		try {
			Thread.sleep(0); // lo usamos para simular que estar operación tarda un poco. Jugaremos con el parámetro en las clases
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}