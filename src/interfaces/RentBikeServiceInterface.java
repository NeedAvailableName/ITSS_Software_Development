package interfaces;

import java.io.IOException;
import java.sql.SQLException;
import entities.Dock;
import entities.Bike;
import exceptions.ecobike.EcoBikeException;
import exceptions.ecobike.RentBikeException;
import exceptions.ecobike.EcoBikeUndefinedException;

public interface RentBikeServiceInterface {
	
	public void rentBike(Bike bike) throws IOException, RentBikeException, EcoBikeUndefinedException, EcoBikeException, SQLException;

	public void returnBike(Bike bike, Dock dock) throws IOException, RentBikeException, EcoBikeUndefinedException;
	
	public void pauseBikeRental(Bike bike) throws SQLException, RentBikeException, EcoBikeException, EcoBikeUndefinedException;
	
	public void resumeBikeRental(Bike bike) throws SQLException, EcoBikeException;
	
}
