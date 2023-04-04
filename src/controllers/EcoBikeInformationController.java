package controllers;

import java.util.ArrayList;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.SQLException;
import boundaries.RentBikeServiceBoundary;
import exceptions.ecobike.NoInformationException;
import exceptions.ecobike.EcoBikeException;
import interfaces.RentBikeServiceInterface;
import utils.Configs;
import utils.DBUtils;
import entities.Bike;
import entities.EBike;
import entities.Dock;

public class EcoBikeInformationController extends EcoBikeBaseController implements PropertyChangeListener {
	private ArrayList<Bike> bikeList = new ArrayList<Bike>();
	private ArrayList<Dock> dockList = new ArrayList<Dock>();
	private ArrayList<Bike> freeBikeList = new ArrayList<Bike>();
	private ArrayList<Bike> rentedBikeList = new ArrayList<Bike>();
	private static EcoBikeInformationController ecoBikeInforController;
	private RentBikeServiceInterface rentBikeServiceInterface;
	
	public ArrayList<Dock> getAllDocks() {
		return this.dockList;
	}
	
	public ArrayList<Bike> getBike() {
		return this.bikeList;
	}
	
	public ArrayList<Bike> getFreeBike() {
		return this.freeBikeList;
	}
	
	public ArrayList<Bike> getAllRentedBikes() {
		return this.rentedBikeList;
	}
	
	public static EcoBikeInformationController getEcoBikeInformationController() throws EcoBikeException, SQLException {
		if(ecoBikeInforController == null) {
			ecoBikeInforController = new EcoBikeInformationController();
			ecoBikeInforController.dockList = DBUtils.getAllDock();
			ecoBikeInforController.rentBikeServiceInterface = new RentBikeServiceBoundary();
			for(Dock d : ecoBikeInforController.dockList) {
				for(Bike b: d.getAllBikeInDock()) {
					ecoBikeInforController.freeBikeList.add(b);
					ecoBikeInforController.bikeList.add(b);
					b.addStatusObserver(ecoBikeInforController);
				}
			}
			
			ArrayList<Bike> rentedBikes = DBUtils.getAllRentedBike();
			for(Bike b : rentedBikes) {
				ecoBikeInforController.rentedBikeList.add(b);
				ecoBikeInforController.bikeList.add(b);
				b.addStatusObserver(ecoBikeInforController);
			}
		}
		return ecoBikeInforController;
	}
	
	public static RentBikeServiceInterface getRentBikeService() {
		return ecoBikeInforController.rentBikeServiceInterface;
	}
	
	public Dock getDockInformationByID(int dockID) throws EcoBikeException, SQLException {
		if(String.valueOf(dockID) == null | String.valueOf(dockID).length() == 0) {
			throw new NoInformationException("Enter keyword to search!");
		}
		
		for(Dock dock : dockList) {
			if(dock.getDockID() == dockID) {
				return dock;
			}
		}
		return null;
	}
	
	public Dock getDockInformationByName(String name) throws EcoBikeException, SQLException {
		if(String.valueOf(name) == null | String.valueOf(name).length() == 0) {
			throw new NoInformationException("Enter keyword to search!");
		}
		
		for(Dock dock: dockList) {
			if(dock.getName().toLowerCase().contains(name.toLowerCase())) {
				return dock;
			}
		}
		return null;
	}

	public Bike getBikeInformationByBikecode(String bikeCode) throws EcoBikeException, SQLException {
		if(String.valueOf(bikeCode) == null | String.valueOf(bikeCode).length() == 0) {
			throw new NoInformationException("Enter keyword to search!");
		}
		
		for(Bike bike : bikeList) {
			if(bike.getBikeBarCode().equals(bikeCode)) {
				return bike;
			}
		}
		return null;
	}
	
	public Bike getBikeInformationByName(String name) throws EcoBikeException, SQLException {
		if(String.valueOf(name) == null | String.valueOf(name).length() == 0) {
			throw new NoInformationException("Enter keyword to search!");
		}
				
		for(Bike bike: bikeList) {
			if(bike.getName().toLowerCase().contains(name.toLowerCase())) {
				return bike;
			}
		}
		return null;
	}
	
	public String getBikeLocation(String bikeCode) throws EcoBikeException, SQLException {
		if(String.valueOf(bikeCode) == null | String.valueOf(bikeCode).length() == 0) {
			throw new NoInformationException("Enter keyword to search!");
		}	
		
		for(Bike bike: bikeList) {
			if(bike.getBikeBarCode().equals(bikeCode)) {
				return bike.getCurrentDock().getName();
			}
		}
		return null;
	}
	
	public float getBikeBattery(String bikeCode) throws EcoBikeException, SQLException {
		if(String.valueOf(bikeCode) == null | String.valueOf(bikeCode).length() == 0) {
			throw new NoInformationException("Enter keyword to search!");
		}
		
		for(Bike bike : this.bikeList) {
			if(bike.getBikeType().equals(Configs.BikeType.EBike.toString())) {
				return (float)((EBike) bike).getBattery();
			}
		}
		return -1;
	}

	@Override
	public void propertyChange(PropertyChangeEvent e) {
		Object value = e.getNewValue();
		if (value instanceof Configs.BIKE_STATUS) {
			Configs.BIKE_STATUS newBikeStatus = (Configs.BIKE_STATUS) value;
			Configs.BIKE_STATUS oldBikeStatus = (Configs.BIKE_STATUS) e.getOldValue();
			Bike sourceBike = (Bike)e.getSource();
			if(newBikeStatus == Configs.BIKE_STATUS.RENTED && oldBikeStatus == Configs.BIKE_STATUS.FREE) {
				this.freeBikeList.remove(sourceBike);
				this.rentedBikeList.add(sourceBike);
			} 
			else if(newBikeStatus == Configs.BIKE_STATUS.FREE) {
				this.freeBikeList.add(sourceBike);
				this.rentedBikeList.remove(sourceBike);
			}
		}
	}
}
