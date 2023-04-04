package entities;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class BankResponse {
	protected int errorCode;
	protected String cardCode;
	protected String owner;
	protected String cvvCode;
	protected String command;
	protected String dateExpired;	
	protected JSONObject jsonObj;
	
	protected BankResponse(String response) throws JSONException {
		System.out.println("Here we have our response:"+response);
		jsonObj = new JSONObject(response);
		this.errorCode = jsonObj.getInt("errorCode");
		this.cardCode = jsonObj.getString("cardCode");
		this.owner = jsonObj.getString("owner");
		this.owner = jsonObj.getString("cvvCode");
		this.owner = jsonObj.getString("command");
		this.owner = jsonObj.getString("dateExpired");
	}

	public int getErrorCode() {
		return errorCode;
	}

	public String getCardCode() {
		return cardCode;
	}

	public String getOwner() {
		return owner;
	}

	public String getCvvCode() {
		return cvvCode;
	}

	public String getCommand() {
		return command;
	}

	public String getDateExpired() {
		return dateExpired;
	}

	public JSONObject getJsonObj() {
		return jsonObj;
	}
	
	public String toJSONString() {
		return jsonObj.toString();
	}
}
