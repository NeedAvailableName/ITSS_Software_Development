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
import entities.Dock;
import entities.Invoice;
import exceptions.ecobike.EcoBikeException;
import javafx.stage.Stage;
import views.screen.popup.PopupScreen;

public class PayForRentScreenHandler extends PaymentScreenHandler {
	
    
    private static PayForRentScreenHandler paymentScreenHandler;

    
    
    private Bike bikeToRent;
    private Dock dockToReturn;
    
    private PayForRentScreenHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }
    
    public static PayForRentScreenHandler getPayForRentScreenHandler(Stage stage, String screenPath, EcoBikeBaseScreenHandler prevScreen, Bike bike, Dock dock) throws IOException {
    	if (paymentScreenHandler == null) {
    		paymentScreenHandler = new PayForRentScreenHandler(stage, screenPath);
    		paymentScreenHandler.setScreenTitle("Register payment method");
    	}
    	if (bike != null) {
    		paymentScreenHandler.bikeToRent = bike;
    		paymentScreenHandler.dockToReturn = dock;
    		paymentScreenHandler.renderBikeRentInformation();
    		
    	}
    	paymentScreenHandler.initialize();
    	return paymentScreenHandler;
    	
    }
    
    private void renderBikeRentInformation() {
    	
    }
    
    protected void initialize(){
    	super.initializeComponent();
    	bikeName.setText(this.bikeToRent.getName());
    	bikeType.setText(this.bikeToRent.getBikeType());
    	rentalTime.setText(Integer.toString(RentBikeController.getRentBikeServiceController(null).pauseBikeRental(bikeToRent))+" mins");
    	rentalPrice.setText(Float.toString(RentBikeController.getRentBikeServiceController(null).getRentalFee(bikeToRent)) + this.bikeToRent.getCurrency());
    }

    
    public void confirmPaymentMethod() throws EcoBikeException, SQLException, IOException, ParseException {
    	if(validateInput()) {
    		System.out.println("Confirm successfully");
    		CreditCard card = new CreditCard(cardHolderName.getText(), cardNumber.getText(), "MB", securityCode.getText(), expirationDate.getText());
    		Invoice invoice = RentBikeController.getRentBikeServiceController(new InterbankBoundary("MB")).returnBike(bikeToRent, dockToReturn, card);
    		if (invoice != null){
    			PopupScreen.success("Return bike successfully");    			
    			InvoiceScreenHandler invoiceScreen = InvoiceScreenHandler.getInvoiceScreenHandler(this.stage, this, invoice); 
    			invoiceScreen.show();
    		} else {	
    			PopupScreen.error("Cannot perform transaction for paying rental");
    		}
    	} else {
    		PopupScreen.error("Invalid input, please check again");
    	}
    }

}
