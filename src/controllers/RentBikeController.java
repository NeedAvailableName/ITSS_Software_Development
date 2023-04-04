package controllers;

import utils.*;
import java.io.IOException;
import java.text.ParseException;
import java.sql.SQLException;
import java.util.HashMap;
import exceptions.ecobike.EcoBikeException;
import interfaces.InterbankInterface;
import entities.Bike;
import entities.Dock;
import entities.BikeTracker;
import entities.CreditCard;
import entities.PaymentTransaction;
import entities.Invoice;
import entities.strategies.DepositFactory;
import entities.strategies.RentalFactory;

public class RentBikeController extends EcoBikeBaseController {
	private static int invoiceCounter = 1;

	private static RentBikeController rentBikeServiceController;
	private HashMap<String, BikeTracker> listBikeTracker;
	private InterbankInterface interbankSystem;
	
	public static int getInvoiceCounter() {
		return invoiceCounter;
	}
	
	public static void setInvoiceCounter(int invoiceCounter) {
		RentBikeController.invoiceCounter = invoiceCounter;
	}
	
	public static RentBikeController getRentBikeServiceController() {
		return rentBikeServiceController;
	}
	
	public static void setRentBikeServiceController(RentBikeController rentBikeServiceController) {
		RentBikeController.rentBikeServiceController = rentBikeServiceController;
	}
	
	public HashMap<String, BikeTracker> getListBikeTracker() {
		return listBikeTracker;
	}
	
	public void setListBikeTracker(HashMap<String, BikeTracker> listBikeTracker) {
		this.listBikeTracker = listBikeTracker;
	}
	
	public InterbankInterface getInterbankSystem() {
		return interbankSystem;
	}
	
	public void setInterbankSystem(InterbankInterface interbankSystem) {
		this.interbankSystem = interbankSystem;
	}
	
	private boolean checkCardIdent(CreditCard card) {
		if(!card.getCardHolderName().equals("Group 2")) {
			return false;
		}
		
		if(!card.getCardNumber().equals("135393_group2_2022")) {
			return false;
		}
		
		if(!card.getExpirationDate().equals("11/25")) {
			return false;
		}
		
		if(!card.getCardSecurity().equals("101")) {
			return false;
		}
		
		return true;
	}
	
	private RentBikeController() {
		this.listBikeTracker = new HashMap<String, BikeTracker>();
	}

	public static RentBikeController getRentBikeServiceController(InterbankInterface interbankSystem){
		if(rentBikeServiceController == null) {
			rentBikeServiceController = new RentBikeController();
		}
		if(interbankSystem != null) {
			rentBikeServiceController.interbankSystem = interbankSystem;
		}
		return rentBikeServiceController;
	}

	public boolean rentBike(Bike bikeToRent, CreditCard card) throws EcoBikeException, IOException, SQLException {
		if(!checkCardIdent(card)) {
			return false;
		}
		
		PaymentTransaction trans = interbankSystem.payDeposit(card, DepositFactory.getDepositStrategy().getDepositPrice((float)bikeToRent.getDeposit()), "PAY_DEPOSIT");
		
		if(trans == null) {
			return false;
		}
		
		int rentID = DBUtils.addStartRentBikeRecord(bikeToRent.getBikeBarCode());
		trans.setRentID(rentID);
		DBUtils.removeBikeFromDock(bikeToRent.getBikeBarCode());
		bikeToRent.getOutOfDock();

		BikeTracker newTracker = new BikeTracker(bikeToRent, rentID);
		DBUtils.addTransaction(trans, rentID);
		newTracker.startCountingRentTime();
		newTracker.addTransaction(trans);
		
		this.listBikeTracker.put(bikeToRent.getBikeBarCode(), newTracker);
		DBUtils.changeBikeStatus(bikeToRent.getBikeBarCode(), Configs.BIKE_STATUS.RENTED.toString());
		return true;

	}
	
	private float calculateFee(float factor, int rentTime) {
		return RentalFactory.getRentalStrategy(rentTime).getRentalPrice(factor, rentTime);
	}
	
	public float getRentalFee(Bike bike)  {
		BikeTracker tracker = this.listBikeTracker.get(bike.getBikeBarCode());
		if(tracker == null) {
			try {
				tracker = DBUtils.getCurrentBikeRenting(bike);
				if(tracker == null) {
					return -1;
				}
			} 
			catch(SQLException | EcoBikeException e) {
				e.printStackTrace();
			}
		}
		
		return calculateFee(bike.getRentFactor(), tracker.getRentedTime());
	}
	
	public Invoice returnBike(Bike bikeToRent, Dock dockToReturn, CreditCard card) throws SQLException, EcoBikeException, ParseException, IOException {
		if(!checkCardIdent(card)) {
			return null;
		}
		
		BikeTracker tracker = this.listBikeTracker.get(bikeToRent.getBikeBarCode());
		if (tracker == null) {
			tracker = DBUtils.getCurrentBikeRenting(bikeToRent);
		}
		
		int time = tracker.stopCountingRentTime();
		float rentCost = calculateFee(bikeToRent.getRentFactor(), time);
		PaymentTransaction trans = interbankSystem.payRental(card, rentCost, "PAY_RENTAL");

		if(trans == null) {
			return null;
		}
		
		DBUtils.addEndRentBikeRecord(tracker.getRentID(), time);
		trans.setRentID(tracker.getRentID());;
		DBUtils.addTransaction(trans, tracker.getRentID());
		tracker.addTransaction(trans);
		Invoice invoice = tracker.createInvoice(invoiceCounter);
		invoiceCounter++;
		DBUtils.changeBikeStatus(bikeToRent.getBikeBarCode(), Configs.BIKE_STATUS.FREE.toString());
		bikeToRent.goToDock(dockToReturn);
		DBUtils.addBikeToDock(bikeToRent.getBikeBarCode(), dockToReturn.getDockID());
		this.listBikeTracker.remove(bikeToRent.getBikeBarCode());
		return invoice;
	}
	
	public int pauseBikeRental(Bike bike) {
		BikeTracker tracker = this.listBikeTracker.get(bike.getBikeBarCode());
		if(tracker == null) {
			try {
				tracker = DBUtils.getCurrentBikeRenting(bike);
				if(tracker != null) {
					listBikeTracker.put(bike.getBikeBarCode(), tracker);
				}
			} 
			catch(SQLException | EcoBikeException e) {
				e.printStackTrace();
			}
		}
		
		try {
			bike.setCurrentStatus(Configs.BIKE_STATUS.PAUSED);
			tracker.stopCountingRentTime();
		} 
		catch (SQLException | EcoBikeException e) {
			e.printStackTrace();
		}
		
		return tracker.getRentedTime();
		
	}
	
	public void resumeBikeRental(Bike bike) {
		BikeTracker tracker = this.listBikeTracker.get(bike.getBikeBarCode());
		if(tracker == null) {
			try {
				int rentTime = DBUtils.getCurrentRentPeriodOfBike(bike.getBikeBarCode());
				if(rentTime != 0) {
					tracker = DBUtils.getCurrentBikeRenting(bike);
					tracker.resumeCountingRentTime();
				}
			} catch (SQLException | EcoBikeException e) {
				e.printStackTrace();
			}
		}
		
		try {
			tracker.resumeCountingRentTime();
			bike.setCurrentStatus(Configs.BIKE_STATUS.RENTED);
		} 
		catch (SQLException | EcoBikeException e) {
			e.printStackTrace();
		}
	}
	
}
