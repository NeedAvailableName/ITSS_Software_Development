package boundaries;

import java.io.IOException;
import entities.PaymentTransaction;
import controllers.InterbankController;
import entities.CreditCard;
import exceptions.ecobike.InvalidEcoBikeInformationException;
import interfaces.InterbankInterface;

public class InterbankBoundary implements InterbankInterface {
	private String bankIssueName;
	private InterbankController interbankController; 
	
	public InterbankBoundary(String issuingBank) {
		super();
		interbankController = new InterbankController(); 
		this.bankIssueName = issuingBank;
	}

	@Override
	public PaymentTransaction payRental(CreditCard creditCard, double amount, String content) {
		PaymentTransaction transaction = null;
		try {
			transaction = interbankController.payRental(creditCard, amount, content);
		} 
		catch(IOException | InvalidEcoBikeInformationException e) {
			e.printStackTrace();
		} 
		return transaction;		
	}

	@Override
	public PaymentTransaction payDeposit(CreditCard creditCard, double amount, String content) {
		PaymentTransaction transaction = null;
		try {
			transaction = interbankController.payDeposit(creditCard, amount, content);
		} 
		catch(IOException | InvalidEcoBikeInformationException e) {
			e.printStackTrace();
		} 
		return transaction;
	}
	
	@Override
	public PaymentTransaction returnDeposit(CreditCard creditCard, double amount, String content) {
		PaymentTransaction transaction = null;
		try {
			transaction = interbankController.refund(creditCard, amount, content);
		} 
		catch(IOException | InvalidEcoBikeInformationException e) {
			e.printStackTrace();
		} 
		return transaction;	
	}
	
	public double getBalance(CreditCard creditCard) {
		return 0;
	}

}
