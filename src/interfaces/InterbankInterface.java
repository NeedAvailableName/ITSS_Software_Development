package interfaces;

import entities.CreditCard;
import entities.PaymentTransaction;

public interface InterbankInterface {
	public PaymentTransaction payDeposit(CreditCard creditCard, double amount, String content);

	public PaymentTransaction returnDeposit(CreditCard creditCard, double amount, String content);
	
	public PaymentTransaction payRental(CreditCard creditCard, double amount, String content);
	
	public double getBalance(CreditCard creditCard);
}
