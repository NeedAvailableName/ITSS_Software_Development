package utils;

import java.sql.SQLException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entities.Bike;
import entities.CreditCard;
import entities.Dock;
import entities.Invoice;
import entities.PaymentTransaction;
import exceptions.ecobike.EcoBikeException;
import exceptions.ecobike.InvalidEcoBikeInformationException;

public class JSONUtils {
	public static String serializeBikeInformation(Bike bike) throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("bike_name", bike.getName());
		obj.put("bike_type", bike.getBikeType());
		obj.put("license_plate_code", bike.getLicensePlateCode());
		obj.put("bike_image", bike.getBikeImage());
		obj.put("bike_barcode", bike.getBikeBarCode());
//		obj.put("bike_rental_price", bike.getBikeRentalPrice());
		obj.put("currency_unit", bike.getCurrency());
		obj.put("deposit_price", bike.getDeposit());
		obj.put("create_date", bike.getCreateDate().toString());
		obj.put("current_status", bike.getCurrentStatus().toString());
		return obj.toString();
	}
	
	public static Bike toBike(String bikeStr) throws JSONException, EcoBikeException, SQLException {
		JSONObject result = new JSONObject(bikeStr);
		if (!result.has("bike_barcode")) {
			throw new InvalidEcoBikeInformationException("invalid JSON string to parse to Bike");
		}
		Bike bikeRes = DBUtils.getBikeByBarcode(result.getString("bike_barcode"));
		String bikeStatus = result.getString("current_status");
		Configs.BIKE_STATUS bikeStat;
		if(bikeStatus.equalsIgnoreCase("FREE")) {
			bikeStat = Configs.BIKE_STATUS.FREE;
		} else if (bikeStatus.equalsIgnoreCase("RENTED")) {
			bikeStat = Configs.BIKE_STATUS.RENTED;
		} else {
			throw new InvalidEcoBikeInformationException("invalid status of bike in database");
		}
		bikeRes.setCurrentStatus(bikeStat);
		return bikeRes;
	}
	
	public static String serializeDockInformation(Dock dock) throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("name", dock.getName());
		obj.put("dock_id", dock.getDockID());
		obj.put("dock_address", dock.getDockAddress());
		obj.put("dock_area", dock.getDockArea());
		obj.put("num_available_bike", dock.getNumAvailableBike());
		obj.put("num_free_dock", dock.getNumDockSpaceFree());
		obj.put("dock_image", dock.getDockImage());
		return obj.toString();
	}
	
	public static Dock toDock(String dockStr) throws JSONException, SQLException, EcoBikeException {
		JSONObject result = new JSONObject(dockStr);
		if (!result.has("dock_id")) {
			throw new InvalidEcoBikeInformationException("invalid JSON string to parse to Dock");
		}
		return DBUtils.getDockInformationByID(result.getInt("dock_id"));
	}
	
	public static String serializeInvoiceInformation(Invoice invoice) throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("invoice_id", invoice.getInvoiceID());
		obj.put("rent_id", invoice.getRentID());
		ArrayList<PaymentTransaction> transactionList = invoice.getTransactionList();
		JSONArray transArr = new JSONArray();
		for(PaymentTransaction t : transactionList) {
			JSONObject objt = new JSONObject();
			objt.put("transaction_id", t.getTransactionId());
			objt.put("transaction_amount", t.getAmount());
			objt.put("transaction_time", t.getTransactionTime());
			objt.put("transaction_detail", t.getContent());
			objt.put("creditcard_number", t.getCreditCardNumber());
			transArr.put(objt);
		}
		obj.put("transactions", transArr);
		return obj.toString();
	}
	
	public static Invoice toInvoice(String invoiceStr) throws JSONException, EcoBikeException {
		JSONObject result = new JSONObject(invoiceStr);
		if(!result.has("invoice_id")) {
			throw new InvalidEcoBikeInformationException("invalid JSON string to parse to Invoice");
		}
		Invoice invoice = new Invoice();
		invoice.setInvoiceID(result.getInt("invoice_id"));
		invoice.setRentID(result.getInt("rent_id"));
		JSONArray transArr = result.getJSONArray("transactions");
		for(int i=0;i<transArr.length();i++) {
			PaymentTransaction transaction = new PaymentTransaction();
			JSONObject jsonTrans = (JSONObject) transArr.get(i);
			transaction.setTransactionId(jsonTrans.getInt("transaction_id"));
			transaction.setAmount(jsonTrans.getInt("transaction_amount"));
			transaction.setContent(jsonTrans.getString("transaction_detail"));
			transaction.setCreditCardNumber(jsonTrans.getString("creditcard_number"));
			transaction.setTransactionTime(jsonTrans.getString("transaction_time"));
			invoice.addTransaction(transaction);
		}
		invoice.setStartTime(FunctionalUtils.stringToDate(result.getString("start_time")));
		invoice.setEndTime(FunctionalUtils.stringToDate(result.getString("end_time")));
		return invoice;
	}
	
	public static String serializeTransactionInformation(PaymentTransaction transaction) throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("transaction_id", transaction.getTransactionId());
		obj.put("transaction_amount", transaction.getAmount());
		obj.put("transaction_time", transaction.getTransactionTime());
		obj.put("transation_detail", transaction.getContent());
		obj.put("creditcard_number", transaction.getCreditCardNumber());
		return obj.toString();
	}

	public static PaymentTransaction toTransaction(String transactionStr) throws JSONException, EcoBikeException {
		JSONObject result = new JSONObject(transactionStr);
		if(!result.has("transaction_id")) {
			throw new InvalidEcoBikeInformationException("invalid JSON string to parse to Transaction");
		}
		PaymentTransaction transaction = new PaymentTransaction();
		transaction.setAmount(result.getDouble("transaction_amount"));
		transaction.setContent(result.getString("transaction_detail"));
		transaction.setCreditCardNumber(result.getString("creditcard_number"));
		transaction.setTransactionId(result.getInt("transaction_id"));
		transaction.setTransactionTime(result.getString("transaction_time"));
		return transaction;
	}
	
	public static String serializeCreditCardInformation(CreditCard card) throws JSONException {
		JSONObject obj = new JSONObject();
		obj.put("cardholder_name", card.getCardHolderName());
		obj.put("creditcard_number", card.getCardNumber());
		obj.put("issusing_bank", card.getIssueBank());
		obj.put("security_code", card.getCardSecurity());
		obj.put("balance", card.getBalance());
		obj.put("expiration_date", card.getExpirationDate().toString());
		return obj.toString();
	}
	
}
