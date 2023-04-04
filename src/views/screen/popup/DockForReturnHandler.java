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

public class DockForReturnHandler extends EcoBikeBaseScreenHandler {

    private Dock currentDock;
    private Bike currentBike;
    @FXML
    private ImageView dockImage;
    @FXML
    private Label dockName;
    @FXML
    private Label dockAddressTxt;
    @FXML
    private Button returnBtn;
    @FXML
    private Label availableSlotsTxt;

    public DockForReturnHandler(Stage stage, Dock dock, Bike bike, String screenPath, EcoBikeBaseScreenHandler prevScreen) throws IOException {
        super(stage, screenPath);
        this.currentDock = dock;
        this.currentBike = bike;
        if(prevScreen != null) {
        	this.setPreviousScreen(prevScreen);
        }
        initialize();
    }

    protected void initialize() {
    	if(currentDock.getDockImage() != null && currentDock.getDockImage().length() != 0) {
    		dockImage.setImage(new Image((new File(Configs.DOCK_IMAGE_LIB + "/" +currentDock.getDockImage())).toURI().toString()));    		
    	}
        dockName.setText(currentDock.getName());
        dockAddressTxt.setText(currentDock.getDockAddress() + "");
        availableSlotsTxt.setText(Integer.toString(currentDock.getNumDockSpaceFree()));
        if (currentDock.isOKToAddBike()) {
        	returnBtn.setDisable(false);
        } else {
        	returnBtn.setDisable(true);
        }
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
