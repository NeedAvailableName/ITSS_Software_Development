package exceptions.ecobike;

public class RentBikeException extends EcoBikeException {

	private RENT_BIKE_ERROR_CODE errCode;
	
	public RentBikeException(String err) {
		super(err);
	}

	public RentBikeException(String string, RentBikeException.RENT_BIKE_ERROR_CODE err) {
		super(string);
		this.errCode = err;
	}
	
	public enum RENT_BIKE_ERROR_CODE {
		ERROR_BIKE_BEING_RENTED,
		ERROR_BIKE_NOT_BEING_RENTED
	}


}
