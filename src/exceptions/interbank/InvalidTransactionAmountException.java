package exceptions.interbank;

public class InvalidTransactionAmountException extends InterbankException {

	public InvalidTransactionAmountException(String err) {
		super(err);
	}

}
