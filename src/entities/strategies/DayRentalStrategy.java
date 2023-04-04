package entities.strategies;

import interfaces.RentalStrategyInterface;

public class DayRentalStrategy implements RentalStrategyInterface {
	private static int basicPrice = 200000;
	private static int halfDay = 720;
	private static int refundPerHour = 10000;
	private static int fullDay = 1440;
	private static int additionalPrice = 2000;
	public DayRentalStrategy() {
		
	}

	@Override
	public float getRentalPrice(float factor, int rentalTime) {
		if(rentalTime < halfDay ) {
			return (float)(factor * (basicPrice - (rentalTime/60+1) * refundPerHour));
		} 
		else if(rentalTime >= halfDay && rentalTime <= fullDay) {
			return (float)(factor * basicPrice);
		} 
		else {
			return (float)(factor * (basicPrice + (rentalTime - fullDay)/15 * additionalPrice));
		}
	}
}