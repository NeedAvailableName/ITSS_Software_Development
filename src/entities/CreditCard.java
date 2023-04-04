package entities;

import java.text.SimpleDateFormat;
import java.text.DateFormat;
import exceptions.interbank.InvalidCardException;
import utils.FunctionalUtils;

public class CreditCard {
	private String cardHolderName;
	private String cardNumber;
	private String cardSecurity;
	private java.util.Date expirationDate;
	private double balance;
	private String issueBank;
	
	public CreditCard(String cardHolderName,String cardNumber, String issueBank, String cardSecurity, String expirationDate) throws InvalidCardException {
		super();
		this.setCardHolderName(cardHolderName);
		this.setCardNumber(cardNumber);
		this.setIssueBank(issueBank);
		this.setCardSecurity(cardSecurity);
		this.setExpirationDate(expirationDate);
	}
	
	public void setCardNumber(String cardNumber) throws InvalidCardException {
		if(cardNumber == null || cardNumber.length() == 0) {
			throw new InvalidCardException("Card number must not be null!");
		}
		this.cardNumber = cardNumber;
	}
	
	public String getCardNumber() {
		return cardNumber;
	}
	
	public String getCardHolderName() {
		return cardHolderName;
	}
	
	public void setCardHolderName(String cardHolderName) throws InvalidCardException {
		if(cardHolderName == null || cardHolderName.length() == 0) {
			throw new InvalidCardException("Card holder name must not be null!");
		}
		if(!FunctionalUtils.contains(cardHolderName, "^[a-zA-z ]")) {
			throw new InvalidCardException("Card number must be letters!");
		}
		this.cardHolderName = cardHolderName;
	}
	
	public String getIssueBank() {
		return issueBank;
	}
	
	public void setIssueBank(String issueBank) throws InvalidCardException {
		if(issueBank == null || issueBank.length() == 0) {
			throw new InvalidCardException("Issue bank must not be null!");
		}
		
		if(!FunctionalUtils.contains(issueBank, "^[a-zA-z ]")) {
			throw new InvalidCardException("Issue bank must not be null!");
		}
		this.issueBank = issueBank;
	}
	
	public String getExpirationDate() {
		DateFormat dateFormatter = new SimpleDateFormat("mm/yy");
		return dateFormatter.format(expirationDate);
	}
	
	public void setExpirationDate(String expirationDate) throws InvalidCardException {
		try {
			this.expirationDate = new SimpleDateFormat("mm/yy").parse(expirationDate);
		} 
		catch(Exception e) {
			throw new InvalidCardException("Invalid expiration date!");
		}
	}
	
	public String getCardSecurity() {
		return cardSecurity;
	}
	
	public void setCardSecurity(String cardSecurity) {
		this.cardSecurity = cardSecurity;
	}
	
	public double getBalance() {
		return balance;
	}
}
