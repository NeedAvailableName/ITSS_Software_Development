package entities;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import utils.FunctionalUtils;

public class TransactionMessage extends BankMessage {
	private String command;
	private String createTime;
	private String transactionContent;
	private String version;
	private String appCode;
	private String hashCode;
	private double amount;
	private JSONObject jsonObj;
	
	public TransactionMessage(String version, String appCode, String command, String cardCode, String owner, String cvvCode, String dateExpried, String content, double amount) throws JSONException {
		super(cardCode, owner, cvvCode, dateExpried);
		this.version = version;
		this.command = command;
		this.transactionContent = content;
		this.appCode = appCode;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date date = new Date();
	    this.createTime = dateFormat.format(date);
		this.amount = amount;
		toJSONObject();
		
	}
	
	private void setHashCode(String hashCode) {
		this.hashCode = hashCode;
	}
	
	
	private JSONObject toJSONObject() throws JSONException {
		jsonObj = new JSONObject();
		JSONObject transaction = new JSONObject();
		jsonObj.put("version", this.version);
		jsonObj.put("appCode", this.appCode);
		transaction.put("command", this.command);
		transaction.put("cardCode", this.cardCode);
		transaction.put("owner", this.owner);
		transaction.put("cvvCode", this.cvvCode);
		transaction.put("dateExpried", this.dateExpired);
		transaction.put("transactionContent", this.transactionContent);
		transaction.put("amount", Double.toString(this.amount));
		transaction.put("createAt", this.createTime);
		jsonObj.put("transaction", transaction);
		this.setHashCode(FunctionalUtils.md5(transaction.toString()));
		jsonObj.put("hashCode", this.hashCode);
		return jsonObj;
	}
	
	@Override
	public String toJSONString() {
		return jsonObj.toString();
	}
}