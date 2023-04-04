package exceptions.interbank;

@SuppressWarnings("serial")
public class InvalidCardException extends InterbankException {
	public InvalidCardException(String err) {
		super(err);
	}
}
