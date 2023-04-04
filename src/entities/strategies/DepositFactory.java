package entities.strategies;

import interfaces.DepositStrategyInterface;

public class DepositFactory {
	public static DepositStrategyInterface getDepositStrategy() {
		return new NormalDepositStrategy();
	}
}
