package entities.strategies;

import interfaces.DepositStrategyInterface;

public class NormalDepositStrategy implements DepositStrategyInterface {
	private static float factor = 0.4f;
	public NormalDepositStrategy() {
		
	}
	@Override
	public float getDepositPrice(float bikeCost) {
		return bikeCost * factor;
	}
	
	public float getFactor() {
		return factor;
	}
	
}
