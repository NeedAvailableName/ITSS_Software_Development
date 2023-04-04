package views.screen.popup;

import entities.Bike;
import entities.NormalBike;
import entities.strategies.DepositFactory;
import exceptions.ecobike.EcoBikeException;
import exceptions.ecobike.EcoBikeUndefinedException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import utils.Configs;
import utils.DBUtils;
import views.screen.BikeInformationScreenHandler;
import views.screen.EcoBikeBaseScreenHandler;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class BikeInDockHandler extends EcoBikeBaseScreenHandler {

    private Bike currentBike;
    @FXML
    private ImageView bikeImage;
    @FXML
    private Label bikeName;
    @FXML
    private Label bikeType;
    @FXML
    private Label depositPrice;
    @FXML
    private Button viewBikeButton;

    public BikeInDockHandler(Stage stage, Bike bike, String screenPath, EcoBikeBaseScreenHandler prevScreen) throws IOException {
        super(stage, screenPath);
        this.currentBike = bike;
        if(prevScreen != null) {
        	this.setPreviousScreen(prevScreen);
        }
        initialize();
    }
    
    protected void initialize() {
    	if(currentBike.getBikeImage() != null && currentBike.getBikeImage().length() != 0) {
    		bikeImage.setImage(new Image((new File(Configs.BIKE_IMAGE_LIB + "/" +currentBike.getBikeImage())).toURI().toString()));    		
    	}
    	// testing information
        bikeName.setText(currentBike.getName());
        bikeType.setText(currentBike.getBikeType() + "");
        depositPrice.setText(DepositFactory.getDepositStrategy().getDepositPrice((float)currentBike.getDeposit()) + " " + currentBike.getCurrency());
        viewBikeButton.setOnMouseClicked(e -> viewBikeInformation());
    }

    public void viewBikeInformation() {
    	BikeInformationScreenHandler bikeInfHandler = BikeInformationScreenHandler.getBikeInformationScreenHandler(this.stage, this.getPreviousScreen(), this.currentBike);
    	bikeInfHandler.show();
    }

}
