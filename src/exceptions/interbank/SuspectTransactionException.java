package exceptions.interbank;

public class SuspectTransactionException extends InterbankException {

	public SuspectTransactionException(String err) {
		super(err);
	}

}
