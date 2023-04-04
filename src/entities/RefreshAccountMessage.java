package entities;

import org.json.JSONException;
import org.json.JSONObject;

public class RefreshAccountMessage extends BankMessage {

	public RefreshAccountMessage(String cardCode, String ownner, String cvvCode, String dateExpried) {
		super(cardCode, ownner, cvvCode, dateExpried);
	}

	@Override
	public String toJSONString() throws JSONException {
		JSONObject jsonMsg = new JSONObject();
		jsonMsg.put("cardCode", this.cardCode);
		jsonMsg.put("owner", this.owner);
		jsonMsg.put("cvvCode", this.cvvCode);
		jsonMsg.put("dateExpried", this.dateExpired);
		return jsonMsg.toString();
	}

}