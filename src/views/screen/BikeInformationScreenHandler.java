package views.screen;


import controllers.EcoBikeInformationController;
import entities.Bike;
import entities.strategies.DepositFactory;
import exceptions.ecobike.EcoBikeException;
import interfaces.Chargeable;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.popup.PopupScreen;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;


public class BikeInformationScreenHandler extends EcoBikeBaseScreenHandler implements PropertyChangeListener {

    private static BikeInformationScreenHandler bikeInformationScreenHandler = null;
    private Bike currentBike = null;

    @FXML
    private Label bikeNameText;
    @FXML
    private Label bikeTypeText;
    @FXML
    private Label bikeStatusText;
    @FXML
    private Label bikeDistanceText, bikeDistanceLabel;
    @FXML
    private Label bikeDepositText;
    @FXML
    private Button rentBikeButton;
    @FXML
    private Button returnBikeButton;
    @FXML
    private Button pauseBikeButton;
    @FXML
    private ImageView bikeImage;
    @FXML
    private ImageView mainScreenIcon;
    @FXML
    private ImageView backIcon;
    @FXML
    private Label bikeLocationTxt;
    @FXML
    private Label batteryTxt, batteryLabel;

    private BikeInformationScreenHandler(Stage stage, String screenPath) throws IOException {
        super(stage, screenPath);
    }

    public static BikeInformationScreenHandler getBikeInformationScreenHandler(Stage stage, EcoBikeBaseScreenHandler prevScreen, Bike bike) {
        if(bikeInformationScreenHandler == null) {
            try {
                bikeInformationScreenHandler = new BikeInformationScreenHandler(stage, Configs.VIEW_BIKE_SCREEN_PATH);
                bikeInformationScreenHandler.setScreenTitle("Bike information screen");
            } 
            catch(Exception e) {
                e.printStackTrace();
            }
        }

        if(prevScreen != null) {
            bikeInformationScreenHandler.setPreviousScreen(prevScreen);
        }

        if(bike != null) {
        	bikeInformationScreenHandler.currentBike = bike;
        	bikeInformationScreenHandler.currentBike.addStatusObserver(bikeInformationScreenHandler);
        }

        bikeInformationScreenHandler.initialize();

        return bikeInformationScreenHandler;
    }

    public void propertyChange(PropertyChangeEvent evt) {
    	Object val = evt.getNewValue();
    	if(val instanceof Configs.BIKE_STATUS) {
    		Configs.BIKE_STATUS newStatus = (Configs.BIKE_STATUS) val;
    		System.out.println("Got new event change: bike status is now:"+newStatus.toString());
        	bikeStatusText.setText(newStatus.toString());
        	if(newStatus == Configs.BIKE_STATUS.FREE) {
        		rentBikeButton.setDisable(false);
        		pauseBikeButton.setDisable(true);
        		returnBikeButton.setDisable(true);
        	} 
        	else if(newStatus == Configs.BIKE_STATUS.PAUSED) {
        		rentBikeButton.setDisable(true);
        		pauseBikeButton.setText("Continue rental");
        		pauseBikeButton.setDisable(false);
        		returnBikeButton.setDisable(false);
        	} 
        	else if(newStatus == Configs.BIKE_STATUS.RENTED) {
        		rentBikeButton.setDisable(true);
        		pauseBikeButton.setText("Pause rental");
        		pauseBikeButton.setDisable(false);
        		returnBikeButton.setDisable(false);
        	}
        	
            returnBikeButton.setDisable((Configs.BIKE_STATUS)evt.getNewValue() == Configs.BIKE_STATUS.FREE ? true : false);
    	}
    	
    }
    
    protected void initialize() {
        rentBikeButton.setOnMouseClicked(e -> rentBike());
        pauseBikeButton.setOnMouseClicked(e->pauseBikeRental());
        returnBikeButton.setOnMouseClicked(e -> returnBike());
        mainScreenIcon.setOnMouseClicked(e -> EcoBikeMainScreenHandler.getEcoBikeMainScreenHandler(this.stage, null).show());
        backIcon.setOnMouseClicked(e -> {
            if(this.getPreviousScreen() != null)
                this.getPreviousScreen().show();
        });
        
        if(currentBike.getBikeImage() != null && currentBike.getBikeImage().length() != 0) {
    		bikeImage.setImage(new Image(new File(Configs.BIKE_IMAGE_LIB + "/" + currentBike.getBikeImage()).toURI().toString()));
    	}
        bikeNameText.setText(currentBike.getName());
        bikeTypeText.setText(currentBike.getBikeType());
        bikeStatusText.setText(currentBike.getCurrentStatus().toString());
        if(currentBike instanceof Chargeable) {
        	batteryLabel.setVisible(true);
        	batteryTxt.setVisible(true);
        	batteryTxt.setText(Float.toString(((Chargeable)currentBike).getBattery()));
        	
        	bikeDistanceLabel.setVisible(true);
        	bikeDistanceText.setText("100 kms");
        	try {
				batteryTxt.setText(Float.toString(EcoBikeInformationController.getEcoBikeInformationController().getBikeBattery(currentBike.getBikeBarCode())) + " %");
			} 
        	catch (SQLException | EcoBikeException e1) {
				e1.printStackTrace();
			}
        	batteryTxt.setVisible(true);
        } 
        else {
        	batteryLabel.setVisible(false);
        	batteryTxt.setVisible(false);
        	
        	bikeDistanceLabel.setVisible(false);
        	bikeDistanceText.setVisible(false);
        }
        bikeDepositText.setText(DepositFactory.getDepositStrategy().getDepositPrice((float)currentBike.getDeposit()) + " " + currentBike.getCurrency());
        
        if(currentBike.getCurrentDock() != null) {
        	bikeLocationTxt.setText(currentBike.getCurrentDock().getName());        	
        } 
        else {
        	bikeLocationTxt.setText("No information");
        }

        rentBikeButton.setDisable(currentBike.getCurrentStatus() == Configs.BIKE_STATUS.FREE ? false : true);
        returnBikeButton.setDisable(currentBike.getCurrentStatus() == Configs.BIKE_STATUS.FREE ? true : false);
        pauseBikeButton.setDisable(currentBike.getCurrentStatus() == Configs.BIKE_STATUS.FREE ? true : false);
    }

    public void rentBike() {
        try {
            System.out.println("rent bike");
            EcoBikeInformationController.getRentBikeService().rentBike(this.currentBike);
        } 
        catch(EcoBikeException e) {
            try {
				PopupScreen.error(e.getMessage());
			} 
            catch(IOException e1) {
				e1.printStackTrace();
			}
        } 
        catch(Exception e1) {
        	e1.printStackTrace();
        }
    }

    public void returnBike() {
        try {
            System.out.println("return bike");
            SelectDockToReturnBikeScreenHandler selectDockHandler = new SelectDockToReturnBikeScreenHandler(new Stage(), this, EcoBikeInformationController.getEcoBikeInformationController().getAllDocks(), this.currentBike);
            selectDockHandler.show();
        } 
        catch(Exception e) {
            e.printStackTrace();
            try {
				PopupScreen.error("An error occured when returning the bike");
			} 
            catch(IOException e1) {
				e1.printStackTrace();
			}
        }
    }

    public void pauseBikeRental() {
        try {
        	System.out.println("pause bike rental");
        	if(this.currentBike.getCurrentStatus() == Configs.BIKE_STATUS.RENTED) {
        		EcoBikeInformationController.getRentBikeService().pauseBikeRental(this.currentBike);
    			PopupScreen.success("Pause bike rental successfully!");
        	} 
        	else if(this.currentBike.getCurrentStatus() == Configs.BIKE_STATUS.PAUSED) {
        		EcoBikeInformationController.getRentBikeService().resumeBikeRental(this.currentBike);
    			PopupScreen.success("Resume bike rental successfully!");
        	}
        } 
        catch(Exception e) {
        	try {
				PopupScreen.error("An error occured when pausing renting the bike");
			} 
        	catch(IOException e1) {
				e1.printStackTrace();
			}
            e.printStackTrace();
        }
    }

}
