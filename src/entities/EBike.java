package entities;

import exceptions.ecobike.InvalidEcoBikeInformationException;
import interfaces.Chargeable;
import utils.Configs;

public class EBike extends Bike implements Chargeable {
	
	private double battery;
	
	public EBike(String name, String licensePlateCode, String bikeImage, String bikeBarcode, String currencyUnit, String createDate, float battery) throws InvalidEcoBikeInformationException {
		super(name, licensePlateCode, bikeImage, bikeBarcode, currencyUnit, createDate);
		this.setBikeType(Configs.BikeType.EBike);
		this.setBattery(battery);
	}	

	public float getBattery() {
		return (float)this.battery;
	}

	public void setBattery(double battery) throws InvalidEcoBikeInformationException {
		if(battery < 0 || battery > 100) {
			throw new InvalidEcoBikeInformationException("Invalid bike battery!");
		}
		this.battery = battery;
	}
}
