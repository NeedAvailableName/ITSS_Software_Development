package controllers;

import java.io.IOException;
import org.json.JSONException;
import entities.PaymentTransaction;
import entities.CreditCard;
import entities.RefreshAccountResponse;
import entities.RefreshAccountMessage;
import entities.TransactionResponse;
import entities.TransactionMessage;
import exceptions.ecobike.InvalidEcoBikeInformationException;
import exceptions.interbank.InterbankUndefinedException;
import exceptions.interbank.InterbankException;
import exceptions.interbank.InvalidCardException;
import exceptions.interbank.InternalServerException;
import exceptions.interbank.NotEnoughBalanceException;
import exceptions.interbank.InvalidTransactionAmountException;
import exceptions.interbank.VersionMissingException;
import exceptions.interbank.SuspectTransactionException;
import exceptions.interbank.NotEnoughInformationException;
import utils.API;
import utils.Configs;

public class InterbankController {	
	
	public InterbankController() {
		super();
	}
	public PaymentTransaction payRental(CreditCard card, double amount, String content) throws InvalidEcoBikeInformationException, IOException {
		PaymentTransaction transaction = new PaymentTransaction(123, "135393_group2_2022", amount, content);
		return transaction;
	}
	
	public PaymentTransaction payDeposit(CreditCard card, double amount, String content) throws InvalidEcoBikeInformationException, IOException {
		PaymentTransaction transaction = new PaymentTransaction(123, "135393_group2_2022", amount, content);
		return transaction;
	}
	
	public PaymentTransaction refund(CreditCard card, double amount, String content) throws InvalidEcoBikeInformationException, IOException {
		PaymentTransaction transaction = new PaymentTransaction(123, "135393_group2_2022", amount, content);
		return transaction;
	}
	
	public void resetBalance(CreditCard card) throws IOException, InterbankException, JSONException {
		RefreshAccountMessage msgToSend = new RefreshAccountMessage(card.getCardNumber(), card.getCardHolderName(), card.getCardSecurity(), card.getExpirationDate());
		String jsonMsg = msgToSend.toJSONString();
		String result = API.patch(Configs.API_BASE_URL + Configs.API_RESET_BALANCE, jsonMsg);
		if (result == null & result.length() == 0) {
			throw handlingExceptionCode(-1);
		}
		RefreshAccountResponse resp = new RefreshAccountResponse(result);
		InterbankException exception = handlingExceptionCode(resp.getErrorCode());
		if (exception != null) {
			throw exception;
		}
	}
	
	private InterbankException handlingExceptionCode(int exceptionCode) {
		switch(exceptionCode) {
			case 0: {
				return null;
			}
			case 1: {
				return new InvalidCardException("Invalid card!");
			}
			case 2: {
				return new NotEnoughBalanceException("Not enough balance to pay transaction!");
			}
			case 3: {
				return new InternalServerException("Internal Server Error!");
			}
			case 4: {
				return new SuspectTransactionException("Suspect transaction!");
			}
			case 5: {
				return new NotEnoughInformationException("Not enough information to pay transaction!");
			}
			case 6: {
				return new VersionMissingException("Missing version information!");
			}
			case 7: {
				return new InvalidTransactionAmountException("Invalid transaction amount!");
			}
			default: {
				return new InterbankUndefinedException("Undefined error!");
			}
		}
	}

}