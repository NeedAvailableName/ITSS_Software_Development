package entities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import exceptions.ecobike.InvalidEcoBikeInformationException;

public class Invoice {
	private int invoiceID;
	private int rentID;
	private Bike bike;
	private Date dateIssued;
	private double total;
	private double deposit;
	private Date startTime;
	private Date endTime;
	private int totalRentTime;
	private ArrayList<PaymentTransaction> listTransaction;
	
	public int getRentID() {
		return rentID;
	}
	
	public void setRentID(int rentID) {
		this.rentID = rentID;
	}
	
	public Invoice() {
		listTransaction = new ArrayList<PaymentTransaction>();
	}
	
	public Invoice(int invoiceID, Bike bike, Date startTime, Date endTime, int totalRentTime) throws InvalidEcoBikeInformationException {
		this.setInvoiceID(invoiceID);
		this.setBike(bike);
		this.setTotalRentTime(totalRentTime);
		this.dateIssued = Calendar.getInstance().getTime();
		this.rentID = -1;
		listTransaction = new ArrayList<PaymentTransaction>();
	}
	
	public String getIssuedDate() {
		return this.dateIssued.toString();
	}
	
	private void setTotalRentTime(int time) {
		this.totalRentTime = time;
	}
	
	public int getTotalRentTime() {
		return this.totalRentTime;
	}
	
	private void setBike(Bike bike) {
		this.bike = bike;
	}
	
	public void addTransaction(PaymentTransaction transaction) {
		if (this.listTransaction.contains(transaction)) {
			System.out.println("Hey the transaction is duplicated!:" + transaction.getTransactionId());
			return;
		}
		this.listTransaction.add(transaction);
		if (this.rentID == -1) {
			this.rentID = transaction.getRentID();
		}
		this.setTotal();
	}
	
	public void removeTransaction(PaymentTransaction transaction) {
		if (this.listTransaction.contains(transaction)) {
			this.listTransaction.remove(transaction);				
		}
	}
	
	public ArrayList<PaymentTransaction> getTransactionList() {
		return this.listTransaction;
	}

	public int getInvoiceID() {
		return invoiceID;
	}

	public Bike getBike() {
		return this.bike;
	}

	public double getDeposit() {
		return deposit;
	}
	
	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) throws InvalidEcoBikeInformationException {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) throws InvalidEcoBikeInformationException {
		this.endTime = endTime;
	}

	public double getTotal() {
		return total;
	}

	private void setTotal() {
		this.total = 0;
		for (PaymentTransaction trans : this.listTransaction) {
			this.total += trans.getAmount();
		}
	}

	public void setInvoiceID(int invoiceID) {
		this.invoiceID = invoiceID;
	}

	public Date getDateIssued() {
		return dateIssued;
	}

	public void setDateIssued(Date dateIssued) {
		this.dateIssued = dateIssued;
	}

	public ArrayList<PaymentTransaction> getListTransaction() {
		return listTransaction;
	}

	public void setListTransaction(ArrayList<PaymentTransaction> listTransaction) {
		this.listTransaction = listTransaction;
	}

	public void setTotal(double total) {
		this.total = total;
	}
	
}
