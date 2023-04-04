package boundaries;

import java.io.IOException;
import java.sql.SQLException;
import controllers.RentBikeController;
import utils.Configs;
import entities.Bike;
import entities.Dock;
import interfaces.RentBikeServiceInterface;
import views.screen.PayForRentScreenHandler;
import views.screen.PayForDepositScreenHandler;
import javafx.stage.Stage;
import exceptions.ecobike.EcoBikeException;
import exceptions.ecobike.EcoBikeUndefinedException;
import exceptions.ecobike.RentBikeException;

public class RentBikeServiceBoundary implements RentBikeServiceInterface {
	
	public RentBikeServiceBoundary() {
		super();
	}
	
	public void rentBike(Bike bike) throws EcoBikeException, SQLException, IOException {
		PayForDepositScreenHandler paymentScreenHandler = PayForDepositScreenHandler.getPayForDepositScreenHandler(new Stage(), Configs.PAYING_FOR_DEPOSIT_SCREEN_PATH, null, bike);
		paymentScreenHandler.show();
	}
	
	public void pauseBikeRental(Bike bike) throws SQLException, EcoBikeException {
		RentBikeController.getRentBikeServiceController(null).pauseBikeRental(bike);	
	}
	
	public void resumeBikeRental(Bike bike) throws SQLException, EcoBikeException {
		RentBikeController.getRentBikeServiceController(null).resumeBikeRental(bike);	
	}
	
	public void returnBike(Bike bike, Dock dock) throws RentBikeException, EcoBikeUndefinedException, IOException {
		PayForRentScreenHandler paymentScreenHandler = PayForRentScreenHandler.getPayForRentScreenHandler(new Stage(), Configs.PAYING_FOR_RENTAL_SCREEN_PATH, null, bike, dock);
		paymentScreenHandler.show();
	}
}
