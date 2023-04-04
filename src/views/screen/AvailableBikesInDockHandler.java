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
import views.screen.popup.BikeInDockHandler;
import views.screen.popup.BikeToReturnHandler;

public class AvailableBikesInDockHandler extends EcoBikeBaseScreenHandler {
	@FXML
	private Dock currentDock;
	@FXML
	private VBox bikeVBox;

    public AvailableBikesInDockHandler(Stage stage, EcoBikeBaseScreenHandler prevScreen, ArrayList<Bike> listRentedBike, Dock dock) throws IOException {
    	super(stage, Configs.BIKES_IN_DOCK_SCREEN_PATH);
    	this.currentDock = dock;
    	this.setScreenTitle("Avaiable Bikes " + currentDock.getName());
    	System.out.println(listRentedBike);
    	addBike(listRentedBike);
    	initialize();
    }
    
    protected void initialize(){
    	
    }
    
    private void addBike(ArrayList<Bike> bikeList) {
    	bikeVBox.getChildren().clear();
    	for (Bike b : bikeList) {
    		BikeInDockHandler bikeHandler;
			try {
				bikeHandler = new BikeInDockHandler(this.stage, b, Configs.BIKE_IN_DOCK_PATH, this);
				bikeVBox.getChildren().add(bikeHandler.getContent());
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    }
}
