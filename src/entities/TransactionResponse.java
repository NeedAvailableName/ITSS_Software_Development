package entities;

import java.util.Date;

import org.json.JSONException;

public class TransactionResponse extends BankResponse {
	private String transactionID;
	private String transactionContent;
	private double amount;
	private Date createAt;
	
	public TransactionResponse(String response) throws JSONException {
		super(response);
		this.transactionID = this.jsonObj.getString("transactionID");
		this.transactionContent = this.jsonObj.getString("transactionContent");
		this.amount = this.jsonObj.getDouble("amount");
	}

	public String getTransactionID() {
		return transactionID;
	}

	public String getTransactionContent() {
		return transactionContent;
	}

	public double getAmount() {
		return amount;
	}

	public Date getCreateAt() {
		return createAt;
	}

}
