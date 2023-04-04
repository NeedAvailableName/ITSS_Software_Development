package exceptions.ecobike;

@SuppressWarnings("serial")
public class InvalidEcoBikeInformationException extends EcoBikeException {
	
	private ECOBIKE_INFORMATION_ERROR_CODE errCode;

	public InvalidEcoBikeInformationException(String err) {
		super(err);
	}
	
	public InvalidEcoBikeInformationException(String string, ECOBIKE_INFORMATION_ERROR_CODE err) {
		super(string);
		this.errCode = err;
	}
	
	public enum ECOBIKE_INFORMATION_ERROR_CODE {
		ERROR_INVALID_FORMAT,
		ERROR_INVALID_PRICE,
		ERROR_NULL_INFORMATION,
	}
	
}
