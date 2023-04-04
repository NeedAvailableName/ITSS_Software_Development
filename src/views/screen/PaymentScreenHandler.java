package views.screen;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

import exceptions.ecobike.EcoBikeException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utils.FunctionalUtils;
import views.screen.popup.PopupScreen;

/**
 * This class provides a base for all payment screen handlers
 *
 */
public abstract class PaymentScreenHandler extends EcoBikeBaseScreenHandler {
	@FXML
    protected Label bikeName;
	@FXML
	protected Label bikeType;
	@FXML
	protected Label rentalTime;
	@FXML
	protected Label rentalPrice;	
    @FXML
    protected TextField cardHolderName;
    @FXML
    protected TextField cardNumber;
    @FXML
    protected TextField expirationDate;
    @FXML
    protected TextField securityCode;
    @FXML
    protected Button confirmPaymentButton;
    @FXML
    protected Button cancelPaymentButton;
	
	public PaymentScreenHandler(Stage stage, String screenPath) throws IOException {
		super(stage, screenPath);
	}

	protected void initializeComponent() {
    	confirmPaymentButton.setOnMouseClicked(e->{
			try {
				confirmPaymentMethod();
			} catch (EcoBikeException | SQLException | IOException | ParseException e1) {
				e1.printStackTrace();
			}
		});
    	cancelPaymentButton.setOnMouseClicked(e->{
		
				this.stage.close();
			
		});
	}

	protected boolean validateInput() throws IOException {
		if(validateCardHolderName(cardHolderName.getText()) == false){
			PopupScreen.error("Invalid card holder name!");
            return false;
        }
        if(validateCardNumber(cardNumber.getText()) == false){
        	PopupScreen.error("Invalid card number!");
            return false;
        }
        if(validateExpirationDate(expirationDate.getText()) == false){
        	PopupScreen.error("Invalid expiration date!");
            return false;
        }
        if(validateCardSecurity(securityCode.getText()) == false){
        	PopupScreen.error("Invalid security code!");
            return false;
        }
        return true;
	}

	public abstract void confirmPaymentMethod() throws EcoBikeException, SQLException, IOException, ParseException;
	
	private boolean validateCardNumber(String cardNumber) {
        return true;
    }

	private boolean validateCardHolderName(String cardHolderName) {
        return FunctionalUtils.contains(cardHolderName, "^[a-zA-Z ]");
    }

	private boolean validateIssueBank(String issueBank) {
        return true;
    }

	private boolean validateExpirationDate(String expirationDate) {
        SimpleDateFormat format = new java.text.SimpleDateFormat("mm/yy");
        try {
            format.parse(expirationDate);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    public boolean validateCardSecurity(String cardSecurity) {
    	if (cardSecurity.length() != 3) {
    		return false;
    	}
        return FunctionalUtils.contains(cardSecurity, "^[0-9]");
    }
}
