package entities;

import exceptions.ecobike.InvalidEcoBikeInformationException;
import utils.Configs;

public class TwinBike extends Bike {
	protected int motor;
	public TwinBike(String name, String licensePlateCode, String bikeImage, String bikeBarcode, String currencyUnit, String createDate) throws InvalidEcoBikeInformationException {
		super(name, licensePlateCode, bikeImage, bikeBarcode, currencyUnit, createDate);
		this.setBikeType(Configs.BikeType.TwinBike);
		
	}
}
