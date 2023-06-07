package bankExample1;
import java.util.*;
public class BankMain1 {
	// This is the sequential version of the Bank problem. 
	// The objective here is observing the performance of the solution.

	public static void main(String[] args) {
		int acc1,acc2;
		int bankSize = 1000;
		int initialAmount = 1000;
		
		Bank b = new Bank (bankSize,initialAmount);
		System.out.println ("Initial amount: "+initialAmount);
		
		// we obtain the current time
		Long t = new Date().getTime();

		// we perform bankSize transfers	
		b.randomTransfers(bankSize);
		
		// we perform 10 audits. We should obtain always the same amount
		for (int i=0;i<10;i++)
			System.out.println ("Audit: "+b.audit(0,bankSize));
		
		// obtaining the time elapsed in milliseconds
		System.out.println("Time elapsed in sequential version:"+ (new Date().getTime()-t));
		
		// performing a final audit
		System.out.println ("Final amount of money: "+b.audit(0, bankSize));
	}
}
