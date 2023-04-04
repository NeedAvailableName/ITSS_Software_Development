package exceptions.interbank;

public class NotEnoughBalanceException extends InterbankException {

	public NotEnoughBalanceException(String err) {
		super(err);
	}

}
