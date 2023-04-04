package views.screen.popup;

import entities.Bike;
import entities.Dock;
import entities.NormalBike;
import exceptions.ecobike.EcoBikeException;
import exceptions.ecobike.EcoBikeUndefinedException;
import exceptions.ecobike.RentBikeException;
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

import boundaries.RentBikeServiceBoundary;
import controllers.EcoBikeInformationController;

public class BikeToReturnHandler extends EcoBikeBaseScreenHandler {

    private Dock currentDock;
    private Bike currentBike;
    @FXML
    private ImageView bikeImage;
    @FXML
    private Label bikeName;
    @FXML
    private Label bikeType;
    @FXML
    private Button returnBtn;

    public BikeToReturnHandler(Stage stage, Dock dock, Bike bike, String screenPath, EcoBikeBaseScreenHandler prevScreen) throws IOException {
        super(stage, screenPath);
        this.currentDock = dock;
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
        bikeName.setText(currentBike.getName());
        bikeType.setText(currentBike.getBikeType().toString());
        returnBtn.setOnMouseClicked(e -> {
        	try {
				EcoBikeInformationController.getRentBikeService().returnBike(currentBike, currentDock);
				this.stage.hide();
			} catch (RentBikeException | EcoBikeUndefinedException | IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        });
    }

}
