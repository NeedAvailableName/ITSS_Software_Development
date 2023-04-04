package entities;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import entities.strategies.DepositFactory;
import exceptions.ecobike.InvalidEcoBikeInformationException;
import utils.Configs;
import utils.FunctionalUtils;

public abstract class Bike {
	
	private String name;
	private String bikeBarCode;
	private Configs.BikeType bikeType;
	protected int saddles, pedals, rearSeats;
	private String licensePlateCode;
	private Configs.BIKE_STATUS currentStatus;
	private Dock currentDock;
	private String bikeImage;
	private String currencyUnit;
	private double deposit;
	private String createDate;
	private String creator;
	protected float rentFactor;
	private PropertyChangeSupport statusNotifier;
	private PropertyChangeSupport dockNotifier;
	
	public String getLicensePlateCode() {
		return licensePlateCode;
	}
	
	private void setLicensePlateCode(String licensePlateCode) {
		this.licensePlateCode = licensePlateCode;
	}
	
	protected Bike(String name, String licensePlateCode, String bikeImage, String bikeCode, String currencyUnit, String createDate) throws InvalidEcoBikeInformationException {
		this.setName(name);
		this.setLicensePlateCode(licensePlateCode);
		this.setBikeImage(bikeImage);
		this.setBikeBarCode(bikeCode);
		this.setCurrency(currencyUnit);
		this.setCreateDate(createDate);
		this.statusNotifier = new PropertyChangeSupport(this);
		this.dockNotifier = new PropertyChangeSupport(this);
	}
	
	public void addStatusObserver(PropertyChangeListener e) {
		this.statusNotifier.addPropertyChangeListener(e);
	}
	
	public void removeStatusObserver(PropertyChangeListener e) {
		this.statusNotifier.removePropertyChangeListener(e);
	}
	
	public void addDockObserver(PropertyChangeListener e) {
		this.dockNotifier.addPropertyChangeListener(e);
	}
	
	public void removeDockObserver(PropertyChangeListener e) {
		this.dockNotifier.removePropertyChangeListener(e);
	}
	
	public String getName() {
		return name;
	}

	private void setName(String name) throws InvalidEcoBikeInformationException {
		if(name == null) {
			throw new InvalidEcoBikeInformationException("Bike name must not be null!");
		}
		
		if(!Character.isLetter(name.charAt(0))) {
			throw new InvalidEcoBikeInformationException("Bike name must start with a letter!");
		} 
		
		if(FunctionalUtils.contains(name, "[^a-z0-9 -_]")) {
			throw new InvalidEcoBikeInformationException("Bike name only contain letters, digits, space and underscore!");
		}
		this.name = name;
	}

	public String getBikeType() {
		return bikeType.toString();
	}

	protected void setBikeType(Configs.BikeType bikeType) throws InvalidEcoBikeInformationException {				
		this.statusNotifier.firePropertyChange("bikeType", this.bikeType, bikeType);
		this.bikeType = bikeType;
		this.saddles = Configs.BikeType.getTypeSadde(bikeType);
		this.rearSeats = Configs.BikeType.getTypeRearSeat(bikeType);
		this.rentFactor = Configs.BikeType.getMultiplier(bikeType);
		this.deposit = Configs.BikeType.getTypePrice(bikeType);
		this.pedals = Configs.BikeType.getTypePedals(bikeType);
	}
	
	
	public String getBikeImage() {
		return bikeImage;
	}

	private void setBikeImage(String bikeImage) {
		this.bikeImage = bikeImage;
	}

	public String getBikeBarCode() {
		return bikeBarCode;
	}

	private void setBikeBarCode(String bikeCode) throws InvalidEcoBikeInformationException {
		if(String.valueOf(bikeCode) == null) {
			throw new InvalidEcoBikeInformationException("Bikecode must not be null!");
		}
		
		this.bikeBarCode = bikeCode;
	}

	public double getDeposit() {
		return deposit;
	}

	private void setDeposit(double deposit) throws InvalidEcoBikeInformationException {
		if(deposit <= 0) {
			throw new InvalidEcoBikeInformationException("Bike deposit must be greater than 0!");
		}
		this.deposit = deposit;
	}

	public String getCurrency() {
		return currencyUnit;
	}

	private void setCurrency(String currency) {
		this.currencyUnit = currency;
	}

	public String getCreateDate() {
		return createDate;
	}

	private void setCreateDate(String createDate) throws InvalidEcoBikeInformationException {
		this.createDate = createDate.toString();
	}

	public Configs.BIKE_STATUS getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(Configs.BIKE_STATUS currentStatus) {
		this.statusNotifier.firePropertyChange("currentStatus", this.currentStatus, currentStatus);
		this.currentStatus = currentStatus;
	}
	
	public void goToDock(Dock dock) {
		this.currentDock = dock;
		this.addDockObserver(dock);
		dock.addBikeToDock(this);
		this.setCurrentStatus(Configs.BIKE_STATUS.FREE);
		this.dockNotifier.firePropertyChange("currentStatus", this.currentStatus, currentStatus);
	}
	
	public void getOutOfDock() {
		this.setCurrentStatus(Configs.BIKE_STATUS.RENTED);
		this.currentDock.removeBikeFromDock(this);
		this.removeDockObserver(this.currentDock);
		this.currentDock = null;
	}
	
	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}
	
	public Dock getCurrentDock() {
		return this.currentDock;
	}
	
	public float getRentFactor() {
		return this.rentFactor;
	}
	
	public int getRearSeats() {
		return this.rearSeats;
	}
	
	public int getSaddle() {
		return this.saddles;
	}
	
	public int getPedals() {
		return this.pedals;
	}
}
