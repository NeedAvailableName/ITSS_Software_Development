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
import views.screen.popup.DockForReturnHandler;

public class SelectDockToReturnBikeScreenHandler extends EcoBikeBaseScreenHandler {
	@FXML
	private VBox dockVBox;
	
	private Bike bikeToReturn;

    public SelectDockToReturnBikeScreenHandler(Stage stage, EcoBikeBaseScreenHandler prevScreen, ArrayList<Dock> listDock, Bike bike) throws IOException {
    	super(stage, Configs.LIST_DOCK_SCREEN_PATH);
    	this.setScreenTitle("Select dock to return bike");
    	this.bikeToReturn = bike;
    	addDock(listDock);
    	initialize();
    }
    
    protected void initialize() {
    	
    }

    private void addDock(ArrayList<Dock> listDock) {
    	dockVBox.getChildren().clear();
    	for (Dock d : listDock) {
    		DockForReturnHandler dockHandler;
			try {
				// error in this constructor
				dockHandler = new DockForReturnHandler(this.stage, d, this.bikeToReturn, Configs.DOCK_FOR_RETURN_SCREEN_PATH, this);
				dockVBox.getChildren().add(dockHandler.getContent());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    }
    
}
