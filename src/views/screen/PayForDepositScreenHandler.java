package views.screen;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ResourceBundle;

import boundaries.InterbankBoundary;
import controllers.RentBikeController;
import entities.Bike;
import entities.CreditCard;
import entities.strategies.DepositFactory;
import exceptions.ecobike.EcoBikeException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import views.screen.popup.PopupScreen;


public class PayForDepositScreenHandler extends PaymentScreenHandler {

    private static PayForDepositScreenHandler paymentScreenHandler;
    
    private Bike bikeToRent;
    
    private PayForDepositScreenHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }
    
    public static PayForDepositScreenHandler getPayForDepositScreenHandler(Stage stage, String screenPath, EcoBikeBaseScreenHandler prevScreen, Bike bike) throws IOException {
    	if (paymentScreenHandler == null) {
    		paymentScreenHandler = new PayForDepositScreenHandler(stage, screenPath);
    		paymentScreenHandler.setScreenTitle("Register payment method");
    	}
    	if (bike != null) {
    		paymentScreenHandler.bikeToRent = bike;
    	}
    	paymentScreenHandler.initialize();
    	return paymentScreenHandler;
    	
    }
    
    protected void initialize() {    
    	super.initializeComponent();
    }
    

    
    public void confirmPaymentMethod() throws EcoBikeException, SQLException, IOException, ParseException {
    	if(validateInput()) {
    		System.out.println("Confirm successfully");
    		CreditCard card = new CreditCard(cardHolderName.getText(), cardNumber.getText(), "MB", securityCode.getText(), expirationDate.getText());
    		InterbankBoundary interbank = new InterbankBoundary("MB");
    		if (RentBikeController.getRentBikeServiceController(interbank).rentBike(bikeToRent, card)) {
    			PopupScreen.success("You have successfully rented bike "+ bikeToRent.getName());
    			this.stage.hide();
    		} else {
    			PopupScreen.error("Error performing transaction");
    		}
    	} else {
    		PopupScreen.error("Invalid input, please check again");
    	}
    }
}