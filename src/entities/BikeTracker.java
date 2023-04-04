package entities;

import java.util.Date;
import java.util.ArrayList;
import java.sql.SQLException;
import utils.DBUtils;
import utils.FunctionalUtils;
import exceptions.ecobike.EcoBikeException;

public class BikeTracker {
	private int rentID;
	private Bike bike;
	private TimeCounter timeCounter;
	private Date startTime, endTime;
	private int timeRented;
	private Invoice invoice;
	private ArrayList<PaymentTransaction> transactionList;
	
	public BikeTracker(Bike bike, int rentID) {
		this.bike = bike;
		this.rentID = rentID;
		this.timeRented = 0;
		this.timeCounter = new TimeCounter();
		this.transactionList = new ArrayList<PaymentTransaction>();	
	}
	
	public void startCountingRentTime() throws EcoBikeException, SQLException {
		this.startTime = timeCounter.startCounter();
	}
	
	public int stopCountingRentTime() throws SQLException, EcoBikeException {
		this.endTime = timeCounter.stopCounter();
		this.timeRented = (int)((this.endTime.getTime() - this.startTime.getTime()) / (1000 * 60) % 60);
		System.out.println("Start at: " + startTime.toString());
		System.out.println("End at: " + endTime.toString());
		System.out.println("Rent period is: " + this.timeRented);
		DBUtils.saveRentPeriod(rentID, this.timeRented);
		return this.timeRented;
	}
	
	public void resumeCountingRentTime() throws SQLException, EcoBikeException {
		timeCounter.startCounter();
	}
	
	public void addTransaction(PaymentTransaction transaction) throws EcoBikeException, SQLException {
		if(!this.transactionList.contains(transaction)) {
			this.transactionList.add(transaction);			
		}
	}
	
	public int getRentID() {
		return this.rentID;
	}
	
	public void setRentedTime(int time) {
		this.timeRented = time;
	}
	
	public int getRentedTime() {
		return this.timeRented;
	}
	
	public void setStartTime(String time) {
		try {
			this.startTime = FunctionalUtils.stringToDate(time);
		} 
		catch(EcoBikeException e) {
			e.printStackTrace();
		}
	}
	
	public Invoice createInvoice(int invoiceID) throws EcoBikeException, SQLException {
		this.invoice = new Invoice(invoiceID, this.bike, this.startTime, this.endTime, this.timeRented);
		for(PaymentTransaction transaction : this.transactionList) {
			this.invoice.addTransaction(transaction);
		}
		return this.invoice;
	}
	
	public Invoice getInvoice() {
		return this.invoice;
	}
	
	public void setActive(boolean activate) {
		this.timeCounter.setActive(activate);
	}

}
