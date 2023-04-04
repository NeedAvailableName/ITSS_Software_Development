package views.screen;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import entities.Bike;
import entities.Dock;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.popup.BikeToReturnHandler;

public class SelectBikeToReturnScreenHandler extends EcoBikeBaseScreenHandler {
	@FXML
	private VBox bikeVBox;
	private Dock dockToReturn;
	
    public SelectBikeToReturnScreenHandler(Stage stage, EcoBikeBaseScreenHandler prevScreen, ArrayList<Bike> listRentedBike, Dock dock) throws IOException {
    	super(stage, Configs.LIST_RENTED_BIKE_SCREEN_PATH);
    	this.dockToReturn = dock;
    	this.setScreenTitle("Select bike to return in " + dockToReturn.getName());
    	addBike(listRentedBike);
    	initialize();
    }
    
    protected void initialize(){
    	
    }

    private void addBike(ArrayList<Bike> listBike) {
    	bikeVBox.getChildren().clear();
    	for (Bike b : listBike) {
    		BikeToReturnHandler bikeHandler;
			try {
				// error in this constructor
				bikeHandler = new BikeToReturnHandler(this.stage, dockToReturn, b, Configs.BIKE_TO_RETURN_SCREEN_PATH, this);
				bikeVBox.getChildren().add(bikeHandler.getContent());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
}
