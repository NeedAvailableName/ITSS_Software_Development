package entities;

import java.sql.SQLException;
import java.util.ArrayList;

import utils.Configs;
import utils.FunctionalUtils;
import exceptions.ecobike.InvalidEcoBikeInformationException;
import exceptions.ecobike.EcoBikeException;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class Dock implements PropertyChangeListener {
	private String name;
	private int dockID;
	private double dockArea;
	private int numAvailableBike;
	private String dockAddress;
	private int numDockSpaceFree;
	private String dockImage;
	private int totalSpace;
	private PropertyChangeSupport propertyNotifier;
	private ArrayList<Bike> bikeInDock;
	
	public Dock(String name, int dockID, String dockAddress, double dock_area, int totalSpace, String dockImage) throws SQLException, EcoBikeException {
		this.setName(name);
		this.setDockID(dockID);
		this.setDockAddress(dockAddress);
		this.setDockArea(dock_area);
		this.totalSpace = totalSpace;
		this.numDockSpaceFree = totalSpace;
		this.setDockImage(dockImage);
		this.bikeInDock = new ArrayList<Bike>();
		this.propertyNotifier = new PropertyChangeSupport(this);
	}

	public void addObserver(PropertyChangeListener e) {
		this.propertyNotifier.addPropertyChangeListener(e);
	}
	
	public void removeObserver(PropertyChangeListener e) {
		this.propertyNotifier.removePropertyChangeListener(e);
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) throws InvalidEcoBikeInformationException {
		if(name == null) {
			throw new InvalidEcoBikeInformationException("Dock name must not be null!");
		}
		
		if(!Character.isLetter(name.charAt(0))) {
			throw new InvalidEcoBikeInformationException("Dock name must start with a letter!");
		} 
		
		if(FunctionalUtils.contains(name, "[^a-z0-9 -_]")) {
			throw new InvalidEcoBikeInformationException("Dock name only contain letters, digits, space and underscore!");
		}
		this.name = name;
	}

	public String getDockAddress() {
		return dockAddress;
	}

	public void setDockAddress(String dockAddress) throws InvalidEcoBikeInformationException {
		if(dockAddress == null) {
			throw new InvalidEcoBikeInformationException("Dock address must not be null!");
		}
		
		if(!Character.isLetterOrDigit(dockAddress.charAt(0))) {
			throw new InvalidEcoBikeInformationException("Dock address must start with a letter!");
		} 
		
		if(FunctionalUtils.contains(dockAddress, "[^a-z0-9 -/,]")) {
			throw new InvalidEcoBikeInformationException("Dock address can only contain letters, digits, space, comma and slash!");
		}

		this.dockAddress = dockAddress;
	}

	public double getDockArea() {
		return dockArea;
	}

	public void setDockArea(double dockArea) throws InvalidEcoBikeInformationException {
		if(dockArea <= 0) {
			throw new InvalidEcoBikeInformationException("Area of dock must be positive!");
		}
		this.dockArea = dockArea;
	}
	
	public int getDockID() {
		return dockID;
	}

	public void setDockID(int dockID) {
		this.dockID = dockID;
	}

	public int getNumAvailableBike() {
		return numAvailableBike;
	}

	public void setNumAvailableBike(int numAvailableBike) throws InvalidEcoBikeInformationException {
		if(numAvailableBike < 0) {
			throw new InvalidEcoBikeInformationException("Number of available bike in dock must be non negative!");
		}
		this.numAvailableBike = numAvailableBike;
	}

	public int getNumDockSpaceFree() {
		return numDockSpaceFree;
	}

	public void setNumDockSpaceFree(int numDockSpaceFree) throws InvalidEcoBikeInformationException {
		if(numDockSpaceFree < 0) {
			throw new InvalidEcoBikeInformationException("Number of free space in dock must be non-negative!");
		}
		this.numDockSpaceFree = numDockSpaceFree;
	}
	
	public String getDockImage() {
		return dockImage;
	}
	
	private void setDockImage(String dockImage) {
		this.dockImage = dockImage;
	}
	
	public int getTotalSpace() {
		return this.totalSpace;
	}
	
	public ArrayList<Bike> getAllBikeInDock() {
		return this.bikeInDock;
	}
	
	public void addBikeToDock(Bike bike) {
		if(!this.bikeInDock.contains(bike)) {
			this.bikeInDock.add(bike);
			this.numDockSpaceFree -= 1;
			this.numAvailableBike += 1;
			this.propertyNotifier.firePropertyChange("numDockSpaceFree", this.numDockSpaceFree + 1, this.numDockSpaceFree);	
		}
	}
	
	public boolean isOKToAddBike() {
		System.out.println("Num free space of dock " + this.getName() + " is:" + this.getNumDockSpaceFree());
		return this.numDockSpaceFree > 0;
	}
	
	public void removeBikeFromDock(Bike bike) {
		if(this.bikeInDock.contains(bike)) {
			this.bikeInDock.remove(bike);
			this.numDockSpaceFree += 1;
			this.numAvailableBike -= 1;
			this.propertyNotifier.firePropertyChange("numDockSpaceFree", this.numDockSpaceFree - 1, this.bikeInDock);
		}
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		Configs.BIKE_STATUS newStatus = (Configs.BIKE_STATUS) evt.getNewValue();
		System.out.println("Dock listened evt from bike");
		if (newStatus == Configs.BIKE_STATUS.RENTED) {
//			this.removeBikeFromDock((Bike)evt.getSource());
		}
	}
}
