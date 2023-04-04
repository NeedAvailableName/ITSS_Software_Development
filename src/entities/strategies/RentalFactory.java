package entities.strategies;

import interfaces.RentalStrategyInterface;

public class RentalFactory {
	public static RentalStrategyInterface getRentalStrategy(int time) {
		if(time < 1440) {
			return new NormalRentalStrategy();
		} 
		else {
			return new DayRentalStrategy();
		}
	}
}
