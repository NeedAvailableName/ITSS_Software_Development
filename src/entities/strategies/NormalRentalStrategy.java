package entities.strategies;

import interfaces.RentalStrategyInterface;

public class NormalRentalStrategy implements RentalStrategyInterface {
	private static int freeChargeMins = 10;
	private static int startingMins = 30;
	private static int startingPrice = 10000;
	private static int rentalPrice = 3000;
	@Override
	public float getRentalPrice(float factor, int rentalTime) {
		if(rentalTime <= freeChargeMins) {
			return 0f;
		}
		
		if(rentalTime > freeChargeMins && rentalTime <= startingMins) {
			return (startingPrice * factor) ;
		}
		
		return (float)(factor * (startingPrice + (rentalTime - startingMins)/15 * rentalPrice));
	}

}
