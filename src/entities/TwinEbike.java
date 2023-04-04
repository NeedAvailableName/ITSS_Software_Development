package entities;

import exceptions.ecobike.InvalidEcoBikeInformationException;
import utils.Configs;

public class TwinEbike extends EBike {

	public TwinEbike(String name, String licensePlateCode, String bikeImage, String bikeBarcode, String currencyUnit, String createDate, float battery) throws InvalidEcoBikeInformationException {
		super(name, licensePlateCode, bikeImage, bikeBarcode, currencyUnit, createDate, battery);
		this.setBikeType(Configs.BikeType.TwinEBike);
	}

}
