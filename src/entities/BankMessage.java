package entities;

import org.json.JSONException;

public abstract class BankMessage {
	protected String cardCode;
	protected String owner;
	protected String cvvCode;
	protected String dateExpired;
	
	protected BankMessage(String cardCode, String ownner, String cvvCode, String dateExpried) {
		this.cardCode = cardCode;
		this.owner = ownner;
		this.cvvCode = cvvCode;
		this.dateExpired = dateExpried;
	}
	
	public abstract String toJSONString() throws JSONException;
}