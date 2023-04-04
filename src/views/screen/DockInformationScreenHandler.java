package views.screen;

import controllers.EcoBikeInformationController;
import exceptions.ecobike.EcoBikeException;
import entities.Bike;
import entities.Dock;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.popup.BikeInDockHandler;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class DockInformationScreenHandler extends EcoBikeBaseScreenHandler implements PropertyChangeListener {

    private static DockInformationScreenHandler dockInformationScreenHandler = null;
    private Dock currentDock = null;
    private ArrayList<Bike> currentBikeList = null;
    @FXML
    private Label dockNameInformation;
    @FXML
    private ImageView dockImageView;
    @FXML
    private Label dockNameText;
    @FXML
    private Label dockAddressText;
    @FXML
    private Label dockAreaText;
    @FXML
    private Label dockCount;
    @FXML
    private Label availableBikeCount;
    @FXML
    private Label availableDocksCount;
    @FXML
    private Label distance;
    @FXML
    private Label estimateWalkTime;
    @FXML
    private Button returnBikeButton;
    @FXML
    private VBox bikeVBox;
    @FXML
    private ImageView mainScreenIcon;
    @FXML
    private ImageView backIcon;
    @FXML
    private Button getBikeButton;

    private DockInformationScreenHandler(Stage stage, String screenPath, EcoBikeBaseScreenHandler prevScreen) throws IOException {
        super(stage, screenPath);
        this.setPreviousScreen(prevScreen);
    }

   
    public static DockInformationScreenHandler getDockInformationScreenHandler(Stage stage, EcoBikeBaseScreenHandler prevScreen, Dock dock) {
    	if (dockInformationScreenHandler == null) {
            try {
                dockInformationScreenHandler = new DockInformationScreenHandler(stage, Configs.VIEW_DOCK_SCREEN_PATH, prevScreen);
                dockInformationScreenHandler.setScreenTitle("Dock information screen");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (prevScreen != null) {
            dockInformationScreenHandler.setPreviousScreen(prevScreen);
        }

        if (dock != null) {
            dockInformationScreenHandler.currentDock = dock;
            dockInformationScreenHandler.currentDock.addObserver(dockInformationScreenHandler);
        }

        ArrayList<Bike> bikeList = dockInformationScreenHandler.currentDock.getAllBikeInDock();
        if (bikeList != null && bikeList.size() != 0) {
            dockInformationScreenHandler.currentBikeList = bikeList;
        }
        dockInformationScreenHandler.initialize();
        return dockInformationScreenHandler;
    }

    protected void initialize() {    	
        returnBikeButton.setOnMouseClicked(e -> {
			try {
				returnBike();
			} catch (IOException | SQLException | EcoBikeException e1) {
				e1.printStackTrace();
			}
		});
        mainScreenIcon.setOnMouseClicked(e -> EcoBikeMainScreenHandler.getEcoBikeMainScreenHandler(this.stage, null).show());
        backIcon.setOnMouseClicked(e -> {
            if (this.getPreviousScreen() != null)
                this.getPreviousScreen().show();
        });
        getBikeButton.setOnMouseClicked(e -> {
			try {
				getBike();
			} catch (IOException | SQLException | EcoBikeException e1) {
				e1.printStackTrace();
			}
		});
        renderDockInformation();
    }

    
    private void renderDockInformation() {
    	if (currentDock.getDockImage() != null && currentDock.getDockImage().length() != 0) {
    		dockImageView.setImage(new Image(new File(Configs.DOCK_IMAGE_LIB + "/" + currentDock.getDockImage()).toURI().toString()));
    	}
    	dockNameInformation.setText(currentDock.getName() + " Information");
        dockNameText.setText(currentDock.getName());
        dockAddressText.setText(currentDock.getDockAddress());
        dockAreaText.setText(currentDock.getDockArea() + "");
        dockCount.setText(currentDock.getTotalSpace() + "");
        availableDocksCount.setText(currentDock.getNumDockSpaceFree() + "");
        availableBikeCount.setText(currentDock.getNumAvailableBike() + "");
        distance.setText("1000");
        estimateWalkTime.setText("10");
        
    	if (currentDock.isOKToAddBike()) {
    		returnBikeButton.setDisable(false);
    	} else {
    		returnBikeButton.setDisable(true);
    	}
    }

   
    private void returnBike() throws IOException, SQLException, EcoBikeException {
    	SelectBikeToReturnScreenHandler bikeToReturnHandler = new SelectBikeToReturnScreenHandler(new Stage(), this, EcoBikeInformationController.getEcoBikeInformationController().getAllRentedBikes(), this.currentDock);
    	bikeToReturnHandler.show();
    }
    
    private void getBike() throws IOException, SQLException, EcoBikeException {
    	AvailableBikesInDockHandler AvailableBikesInDockHandler = new AvailableBikesInDockHandler(new Stage(), this, dockInformationScreenHandler.currentDock.getAllBikeInDock(), this.currentDock);
    	 System.out.println(this.currentBikeList);
    	AvailableBikesInDockHandler.show();
    }
    

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		System.out.println("The dock has changed....");
		renderDockInformation();
	}


}
