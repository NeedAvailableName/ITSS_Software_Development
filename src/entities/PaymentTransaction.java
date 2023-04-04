package entities;

import java.util.Calendar;
import utils.FunctionalUtils;
import java.util.Date;
import exceptions.ecobike.InvalidEcoBikeInformationException;
import exceptions.interbank.InvalidCardException;

public class PaymentTransaction {
	private int rentID;
	private String transactionId;
	private String creditCardNumber;
	private double amount;
	private Date transactionTime;
	private String content;
	private String err;
	
	public PaymentTransaction() {
		
	}
	
	public PaymentTransaction(int transactionId, String creditCardNumber, double amount, String content) throws InvalidEcoBikeInformationException, InvalidCardException {
		this.setAmount(amount);
		this.setTransactionId(transactionId);
		this.setCreditCardNumber(creditCardNumber);
		this.setContent(content);
		this.setTransactionTime("");
	}
	
	public String getTransactionId() {
		return transactionId;
	}
	
	public void setTransactionId(int transactionId) {
		this.transactionId = Integer.toString(transactionId);
	}
	
	public String getCreditCardNumber() {
		return this.creditCardNumber;
	}
	
	public void setCreditCardNumber(String creditCardNumber) throws InvalidCardException {
		if(creditCardNumber == null || creditCardNumber.length() == 0) {
			throw new InvalidCardException("Card number must not be null!");
		}
		
//		if(FunctionalUtils.contains(creditCardNumber, "^[0-9 ]")) {
//			throw new InvalidCardException("Card number must contain digits!");
//		}
		
		this.creditCardNumber = creditCardNumber;
	}
	
	public double getAmount() {
		return amount;
	}
	
	public void setAmount(double amount) {
		this.amount = amount;
	}
	
	public Date getTransactionTime() {
		return transactionTime;
	}
	
	public void setTransactionTime(String transactionTime) throws InvalidEcoBikeInformationException {
		try {			
			if(transactionTime == null || transactionTime.length() == 0) {
				this.transactionTime = FunctionalUtils.stringToDate(Calendar.getInstance().getTime().toString());			
			}
			else {
				this.transactionTime = FunctionalUtils.stringToDate(transactionTime);
			}
		} 
		catch(Exception err) {
			err.printStackTrace();
			throw new InvalidEcoBikeInformationException("Invalid time!");
		}
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public String getErrorMessage() {
		return err;
	}
	
	public void setErrorMessage(String errorMessage) {
		this.err = errorMessage;
	}
	
	public void setRentID(int rentID) {
		this.rentID = rentID;
	}

	public int getRentID() { 
		return this.rentID;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public void setTransactionTime(Date transactionTime) {
		this.transactionTime = transactionTime;
	}
}