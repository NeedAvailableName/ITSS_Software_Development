package utils;

import java.sql.Connection; 
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import entities.Bike;
import entities.BikeTracker;
import entities.CreditCard;
import entities.Dock;
import entities.Invoice;
import entities.PaymentTransaction;
import exceptions.ecobike.EcoBikeException;
import exceptions.ecobike.InvalidEcoBikeInformationException;
import utils.Configs.BikeType;

public class DBUtils {
	private static Connection connection;
	
	public static Connection getConnection() throws EcoBikeException {
        if(connection != null) return connection;
        try {
            Class.forName("org.sqlite.JDBC");
            String url = "jdbc:sqlite:src/lib/ecobikedb.db";
            connection = DriverManager.getConnection(url);
            System.out.println("Connect successfully");
            initializeConfigs();
        } 
        catch(Exception e) {
        	e.printStackTrace();
        }
        return connection;
	}
	
	private static void initializeConfigs() throws SQLException {
		String sql = "Select * from Configs";
		PreparedStatement sqlStm = connection.prepareStatement(sql);
		ResultSet result = sqlStm.executeQuery();
		while(result.next()) {
			BikeType type = Configs.BikeType.toBikeType(result.getString("bike_type"));
			Configs.BikeType.setMultiplier(type, result.getFloat("rent_factor"));
			Configs.BikeType.setTypeMotor(type, result.getInt("motors"));
			Configs.BikeType.setTypePedals(type, result.getInt("pedals"));
			Configs.BikeType.setTypePrice(type, result.getFloat("bike_price"));
			Configs.BikeType.setTypeRearSeat(type, result.getInt("rear_seats"));
			Configs.BikeType.setTypeSaddle(type, result.getInt("saddles"));
		}
	}
	
	public static Bike getBikeByName(String name) throws SQLException, EcoBikeException {
		String sql = "SELECT Bike.name as name,"
				+ "Bike.bike_type as bike_type,"
				+ "Bike.license_plate_code as license_plate_code,"
				+ "Bike.bike_image as bike_image,"
				+ "Bike.bike_barcode as bike_barcode,"
				+ "Bike.currency_unit as currency_unit,"
				+ "Bike.create_date as create_date,"
				+ "BikeStatus.total_rent_time as total_rent_time,"
				+ "BikeStatus.current_battery as current_battery,"
				+ "BikeStatus.current_status as current_status FROM Bike, BikeStatus, BikeInDock WHERE UPPER(Bike.name) = UPPER(?) "
				+ "And Bike.bike_barcode = BikeStatus.bike_barcode And "
				+ "BikeStatus.bike_barcode = BikeInDock.bike_barcode";
		PreparedStatement stm = DBUtils.getConnection().prepareStatement(sql);
		stm.setString(1, name);
		ResultSet result = stm.executeQuery();
		Bike bikeRes = null;
		try {
			bikeRes = BikeFactory.getBikeWithInformation(result);
		} 
		catch(IllegalArgumentException | SecurityException e) {
			e.printStackTrace();
		}
		
		String bikeStatus = result.getString("current_status");
		Configs.BIKE_STATUS bikeStat;
		if(bikeStatus.equalsIgnoreCase("FREE")) {
			bikeStat = Configs.BIKE_STATUS.FREE;
		} 
		else if(bikeStatus.equalsIgnoreCase("RENTED")) {
			bikeStat = Configs.BIKE_STATUS.RENTED;
		} 
		else {
			throw new InvalidEcoBikeInformationException("Invalid status of bike!");
		}
		bikeRes.setCurrentStatus(bikeStat);
		
		return bikeRes;

	}
	
	public static Dock getDockByName(String dockName) throws SQLException, EcoBikeException {
		PreparedStatement stm = DBUtils.getConnection().prepareStatement(
				"SELECT * FROM Dock WHERE UPPER(name) = UPPER(?)");
		stm.setString(1, dockName);
		ResultSet result = stm.executeQuery();
		Dock dockRes = new Dock(result.getString("name"), 
				result.getInt("dock_id"), 
				result.getString("dock_address"), 
				result.getDouble("dock_area"),
				result.getInt("num_dock"),
				result.getString("dock_image"));
		ArrayList<Bike> listBike = getAllBikeByDockId(result.getInt("dock_id"));
		for(Bike b : listBike) {
			b.goToDock(dockRes);
		}
		return dockRes;
	}
	

	public static Invoice getInvoiceById(int invoiceID) throws EcoBikeException, SQLException, ParseException {
		String stmI = "SELECT * FROM Invoice WHERE invoice_id=?";
		PreparedStatement sqlStmI = DBUtils.getConnection().prepareStatement(stmI);
		sqlStmI.setInt(1, invoiceID);
		ResultSet resultI = sqlStmI.executeQuery();
		int rentID = resultI.getInt("rent_id");
		String stmR = "SELECT * FROM RentBike WHERE rent_id=?";
		PreparedStatement sqlStmR = DBUtils.getConnection().prepareStatement(stmR);
		sqlStmR.setInt(1, rentID);
		ResultSet resultR = sqlStmR.executeQuery();
		String bikeBarcode = resultR.getString("bike_barcode");
		Bike bikeRes = getBikeByBarcode(bikeBarcode);
		Date startTime = FunctionalUtils.stringToDate(resultR.getString("start_time"));
		Date endTime = FunctionalUtils.stringToDate(resultR.getString("end_time"));
		int totalRentTime = resultR.getInt("rent_period");
		Invoice invoiceRes = new Invoice(invoiceID, bikeRes, startTime, endTime, totalRentTime);
		do {
			int transactionID = resultI.getInt("transaction_id");
			PaymentTransaction trans = getTransactionById(transactionID);
			if(trans != null) {
				invoiceRes.addTransaction(trans);
			}
		} while(resultI.next());
		return invoiceRes;		
	}	
	
	public static PaymentTransaction getTransactionById(int transactionID) throws SQLException, EcoBikeException {
		PreparedStatement stm = DBUtils.getConnection().prepareStatement(
				"SELECT * FROM EcoBikeTransaction WHERE transaction_id=?");
		stm.setInt(1, transactionID);
		ResultSet result = stm.executeQuery();
		PaymentTransaction transaction = new PaymentTransaction();
		transaction.setCreditCardNumber(result.getString("creditcard_number"));
		transaction.setTransactionId(transactionID);
		transaction.setAmount(result.getDouble("transaction_amount"));
		transaction.setTransactionTime(result.getDate("transaction_time").toString());
		String transactionDetails = result.getString("transaction_detail");
		if(transactionDetails.contains(Configs.TransactionType.PAY_DEPOSIT.toString()) ||
				transactionDetails.contains(Configs.TransactionType.PAY_RENTAL.toString()) || 
						transactionDetails.contains(Configs.TransactionType.RETURN_DEPOSIT.toString())) {
			transaction.setContent(transactionDetails);
		} 
		else {
			throw new InvalidEcoBikeInformationException("Invalid transaction message!");
		}
		return transaction;
	}
	
	public static Bike getBikeByBarcode(String bikeBarcode) throws EcoBikeException, SQLException {
		String sql = "SELECT Bike.name as name,"
				+ "Bike.bike_type as bike_type,"
				+ "Bike.license_plate_code as license_plate_code,"
				+ "Bike.bike_image as bike_image,"
				+ "Bike.bike_barcode as bike_barcode,"
				+ "Bike.currency_unit as currency_unit,"
				+ "Bike.create_date as create_date,"
				+ "BikeStatus.total_rent_time as total_rent_time,"
				+ "BikeStatus.current_battery as current_battery,"
				+ "BikeStatus.current_status as current_status FROM Bike, BikeStatus, BikeInDock WHERE Bike.bike_barcode=? "
				+ "And Bike.bike_barcode = BikeStatus.bike_barcode And "
				+ "BikeStatus.bike_barcode = BikeInDock.bike_barcode";
		PreparedStatement stm = DBUtils.getConnection().prepareStatement(sql);
		stm.setString(1, bikeBarcode);
		ResultSet result = stm.executeQuery();
		Bike bikeRes = null;
		try {
			bikeRes = BikeFactory.getBikeWithInformation(result);
		} 
		catch(IllegalArgumentException | SecurityException e) {
			e.printStackTrace();
		}
		
		String bikeStatus = result.getString("current_status");
		Configs.BIKE_STATUS bikeStat;
		if(bikeStatus.equalsIgnoreCase("FREE")) {
			bikeStat = Configs.BIKE_STATUS.FREE;
		} 
		else if(bikeStatus.equalsIgnoreCase("RENTED")) {
			bikeStat = Configs.BIKE_STATUS.RENTED;
		} 
		else {
			throw new InvalidEcoBikeInformationException("Invalid status of bike!");
		}
		bikeRes.setCurrentStatus(bikeStat);
		
		return bikeRes;

	}
	
	public static  ArrayList<Bike> getAllBikeByDockId(int dockId) throws SQLException, EcoBikeException {
		String sql = "SELECT Bike.name as name,"
				+ "Bike.bike_type as bike_type,"
				+ "Bike.license_plate_code as license_plate_code,"
				+ "Bike.bike_image as bike_image,"
				+ "Bike.bike_barcode as bike_barcode,"
				+ "Bike.currency_unit as currency_unit,"
				+ "Bike.create_date as create_date,"
				+ "BikeStatus.total_rent_time as total_rent_time,"
				+ "BikeStatus.current_battery as current_battery,"
				+ "BikeStatus.current_status as current_status FROM Bike, BikeStatus, BikeInDock WHERE dock_id=? "
				+ "And Bike.bike_barcode = BikeStatus.bike_barcode And "
				+ "BikeStatus.bike_barcode = BikeInDock.bike_barcode";
		PreparedStatement stm = DBUtils.getConnection().prepareStatement(sql);
		stm.setInt(1, dockId);
		ResultSet result = stm.executeQuery();
		ArrayList<Bike> list = new ArrayList<Bike>();
		while(result.next()) {
			Bike bikeRes = null;
			try {
				bikeRes = BikeFactory.getBikeWithInformation(result);
			} 
			catch (IllegalArgumentException | SecurityException e) {
				e.printStackTrace();
			}
			
			String bikeStatus = result.getString("current_status");
			Configs.BIKE_STATUS bikeStat;
			if(bikeStatus.equalsIgnoreCase("FREE")) {
				bikeStat = Configs.BIKE_STATUS.FREE;
			} else if (bikeStatus.equalsIgnoreCase("RENTED")) {
				bikeStat = Configs.BIKE_STATUS.RENTED;
			} else {
				throw new InvalidEcoBikeInformationException("Invalid status of bike!");
			}
			bikeRes.setCurrentStatus(bikeStat);
			list.add(bikeRes);
		}
		
		return list;
	}
	
	public static ArrayList<Bike> getAllBike() throws SQLException, EcoBikeException {
		String sql = "SELECT Bike.name as name,"
				+ "Bike.bike_type as bike_type,"
				+ "Bike.license_plate_code as license_plate_code,"
				+ "Bike.bike_image as bike_image,"
				+ "Bike.bike_barcode as bike_barcode,"
				+ "Bike.currency_unit as currency_unit,"
				+ "Bike.create_date as create_date,"
				+ "BikeStatus.total_rent_time as total_rent_time,"
				+ "BikeStatus.current_battery as current_battery,"
				+ "BikeStatus.current_status as current_status FROM Bike, BikeStatus WHERE Bike.bike_barcode = BikeStatus.bike_barcode";
		Statement stm = DBUtils.getConnection().createStatement();
		ResultSet result = stm.executeQuery(sql);
		ArrayList<Bike> bikes = new ArrayList<Bike>();
		while(result.next()) {
			Bike bikeRes = null;
			try {
				bikeRes = BikeFactory.getBikeWithInformation(result);
			} 
			catch(IllegalArgumentException | SecurityException e) {
				e.printStackTrace();
			}
			
			String bikeStatus = result.getString("current_status");
			Configs.BIKE_STATUS bikeStat;
			if(bikeStatus.equalsIgnoreCase("FREE")) {
				bikeStat = Configs.BIKE_STATUS.FREE;
			} else if (bikeStatus.equalsIgnoreCase("RENTED")) {
				bikeStat = Configs.BIKE_STATUS.RENTED;
			} else {
				throw new InvalidEcoBikeInformationException("Invalid status of bike!");
			}
			bikeRes.setCurrentStatus(bikeStat);
			bikes.add(bikeRes);
		}
		return bikes;
	}
	
	public static ArrayList<Bike> getAllRentedBike() throws SQLException, EcoBikeException {
		System.out.println("Get into getAllRentedBike");
		ArrayList<Bike> allBikes = getAllBike();
		ArrayList<Bike> result = new ArrayList<Bike>();
		for(Bike b : allBikes) {
			if(b.getCurrentStatus() == Configs.BIKE_STATUS.RENTED) {
				System.out.println("There is a bike in rent: " + b.getName());
				result.add(b);
			}
		}
		return result;
	}

	public static Dock getDockInformationByID(int dockID) throws SQLException, EcoBikeException {
		PreparedStatement stm = DBUtils.getConnection().prepareStatement("SELECT * FROM Dock WHERE dock_id=?");
		stm.setInt(1, dockID);
		ResultSet result = stm.executeQuery();
		Dock dockRes = new Dock(result.getString("name"), 
				result.getInt("dock_id"), 
				result.getString("dock_address"), 
				result.getDouble("dock_area"),
				result.getInt("num_dock"),
				result.getString("dock_image"));
		ArrayList<Bike> listBike = getAllBikeByDockId(result.getInt("dock_id"));
		for(Bike b : listBike) {
			b.goToDock(dockRes);
		}
		return dockRes;
	}
	
	public static ArrayList<Dock> getAllDock() throws SQLException, EcoBikeException {
		String sql = "Select * from Dock";
		Statement stm = DBUtils.getConnection().createStatement();
		ResultSet result = stm.executeQuery(sql);
		ArrayList<Dock> docks = new ArrayList<Dock>();
		while(result.next()) {
			Dock dockRes = new Dock(result.getString("name"), 
					result.getInt("dock_id"), 
					result.getString("dock_address"), 
					result.getDouble("dock_area"),
					result.getInt("num_dock"),
					result.getString("dock_image"));
			ArrayList<Bike> listBike = getAllBikeByDockId(result.getInt("dock_id"));
			for(Bike b : listBike) {
				b.goToDock(dockRes);
			}
			docks.add(dockRes);
		}
		return docks;
	}
	
	public static int saveCustomer(String customerName) throws SQLException, EcoBikeException {
		String stm = "INSERT INTO Customer(customer_name) VALUES (?)";
		PreparedStatement sqlStm = DBUtils.getConnection().prepareStatement(stm);
		sqlStm.setString(1, customerName);
		sqlStm.execute();
		stm = "SELECT customer_id FROM Customer WHERE customer_name=?";
		sqlStm = DBUtils.getConnection().prepareStatement(stm);
		sqlStm.setString(1, customerName);
		ResultSet result = sqlStm.executeQuery();
		return result.getInt("customer_id");
	}
	
	
	public static void changeBikeStatus(String bikeBarcode, String newStatus) throws SQLException, EcoBikeException {
		String stm = "UPDATE BikeStatus SET current_status=? WHERE bike_barcode=?";
		PreparedStatement sqlStm = DBUtils.getConnection().prepareStatement(stm);
		sqlStm.setString(1, newStatus);
		sqlStm.setString(2, bikeBarcode);
		sqlStm.executeUpdate();
	}
	
	public static void removeBikeFromDock(String bikeBarcode) throws SQLException, EcoBikeException {
		String stm = "DELETE FROM BikeInDock WHERE bike_barcode=?";
		PreparedStatement sqlStm = DBUtils.getConnection().prepareStatement(stm);
		sqlStm.setString(1, bikeBarcode);
		sqlStm.execute();
	}
	
	public static void addBikeToDock(String bikeBarcode, int dockID) throws SQLException, EcoBikeException {
		String stm = "INSERT INTO BikeInDock(dock_id, bike_barcode) VALUES (?,?)";
		PreparedStatement sqlStm = DBUtils.getConnection().prepareStatement(stm);
		sqlStm.setInt(1, dockID);
		sqlStm.setString(2, bikeBarcode);
		sqlStm.execute();
	}
	
	public static int getCurrentRentTimeByBikeBarcode(String bikeBarcode) throws SQLException, EcoBikeException, ParseException {
		String stm = "SELECT rent_period FROM RentBike WHERE bike_barcode=?";
		PreparedStatement sqlStm = DBUtils.getConnection().prepareStatement(stm);
		sqlStm.setString(1, bikeBarcode);
		ResultSet result = sqlStm.executeQuery();
		result.last();
		int period = 0;
		String endTimeStr = result.getString("end_time");
		if(endTimeStr == null || endTimeStr.length() == 0) {
			 String startTimeStr = result.getString("start_time");
			 Date endTime = FunctionalUtils.stringToDate(endTimeStr);
			 Date startTime = FunctionalUtils.stringToDate(startTimeStr);
			 period = (int)((endTime.getTime() - startTime.getTime()) / (1000 * 60) ) % 60;
		 } 
		else {
			 period = result.getInt("rent_period");
		 }
		 return period;
	}
	
	public static int addStartRentBikeRecord(String bikeBarcode) throws SQLException, EcoBikeException {
		String stm = "INSERT INTO RentBike(bike_barcode, start_time)" + "VALUES (?,?)";
		PreparedStatement sqlStm = DBUtils.getConnection().prepareStatement(stm, Statement.RETURN_GENERATED_KEYS);
		sqlStm.setString(1, bikeBarcode);
		sqlStm.setString(2, Calendar.getInstance().getTime().toString());
		sqlStm.execute();
		
		ResultSet result = sqlStm.getGeneratedKeys();
		if(result.next()) {
			return result.getInt(1);
		}
		return -1;	
	}
	
	public static void addEndRentBikeRecord(int rentID, int rentPeriod) throws SQLException, EcoBikeException, ParseException {
		String stm = "UPDATE RentBike SET end_time=? WHERE rent_id=?";
		PreparedStatement sqlStm = DBUtils.getConnection().prepareStatement(stm);
		sqlStm.setInt(1, rentID);
		Date end = Calendar.getInstance().getTime();
		sqlStm = DBUtils.getConnection().prepareStatement(stm);
		sqlStm.setString(1, end.toString());
		sqlStm.setInt(2, rentID);
		sqlStm.execute();
		stm = "UPDATE RentBike SET rent_period=? WHERE rent_id=?";
		sqlStm = DBUtils.getConnection().prepareStatement(stm);
		sqlStm.setInt(1, rentPeriod);
		sqlStm.setInt(2, rentID);
		sqlStm.execute();
	}
	
	public static int addTransaction(PaymentTransaction transaction, int rentID) throws SQLException, EcoBikeException {
		String stm = "INSERT INTO EcoBikeTransaction (transaction_amount, transaction_time, transaction_detail, creditcard_number, rent_id) VALUES (?,?,?,?,?)";
		PreparedStatement sqlStm = DBUtils.getConnection().prepareStatement(stm, Statement.RETURN_GENERATED_KEYS);
		sqlStm.setFloat(1, (float)transaction.getAmount());
		sqlStm.setString(2, transaction.getTransactionTime().toString());
		sqlStm.setString(3, transaction.getContent());
		sqlStm.setString(4, transaction.getCreditCardNumber());
		sqlStm.setInt(5, rentID);
		sqlStm.execute();
		ResultSet result = sqlStm.getGeneratedKeys();
		if(result.next()) {
			int id = result.getInt(1);
			transaction.setTransactionId(id);
			return id;
		}
		return -1;		
	}
	
	public static void addInvoice(Invoice invoice) throws SQLException, EcoBikeException {
		String stm = "INSERT INTO Invoice (invoice_id, transaction_id, rent_id, date_issued) VALUES (?,?,?,?)";
		PreparedStatement sqlStm = DBUtils.getConnection().prepareStatement(stm);
		for (PaymentTransaction trans : invoice.getTransactionList()) {
			sqlStm.setInt(1, invoice.getInvoiceID());
			sqlStm.setInt(2, Integer.parseInt(trans.getTransactionId()));
			sqlStm.setInt(3, trans.getRentID());
			sqlStm.setString(4, invoice.getIssuedDate());
			sqlStm.execute();
		}
	}

	public static String getBikeLocation(String bikeBarcode) throws SQLException, EcoBikeException {
		String stm = "SELECT dock_id FROM BikeInDock WHERE bike_barcode=?";
		PreparedStatement sqlStm = DBUtils.getConnection().prepareStatement(stm);
		sqlStm.setString(1, bikeBarcode);
		ResultSet result = sqlStm.executeQuery();
		int dockID = result.getInt("dock_id");
		stm = "SELECT name FROM Dock WHERE dock_id=?";
		sqlStm = DBUtils.getConnection().prepareStatement(stm);
		sqlStm.setInt(1, dockID);
		result = sqlStm.executeQuery();
		String location = result.getString("name");
		return location;
	}
	
	public static float getBikeBattery(String bikeBarcode) throws SQLException, EcoBikeException {
		String stm = "SELECT current_battery FROM BikeStatus WHERE bike_barcode=?";
		PreparedStatement sqlStm = DBUtils.getConnection().prepareStatement(stm);
		sqlStm.setString(1, bikeBarcode);
		ResultSet result = sqlStm.executeQuery();
		float battery = result.getFloat("current_battery");
		return battery;
	}
	
	public static void saveRentPeriod(int rentID, int period) throws SQLException, EcoBikeException {
		String stm = "UPDATE RentBike SET rent_period=? WHERE rent_id=?";
		PreparedStatement sqlStm = DBUtils.getConnection().prepareStatement(stm);
		sqlStm.setInt(1, rentID);
		sqlStm.setInt(2, period);
		sqlStm.execute();
	}
	
	public static int getCurrentRentPeriodOfBike(String bikeBarcode) throws SQLException, EcoBikeException {
		String stm = "SELECT rent_period FROM RentBike WHERE bike_barcode=? ORDER BY rent_id DESC LIMIT 1";
		PreparedStatement sqlStm = DBUtils.getConnection().prepareStatement(stm);
		sqlStm.setString(1, bikeBarcode);
		ResultSet result = sqlStm.executeQuery();
		return result.getInt("rent_period");
	}
	
	public static BikeTracker getCurrentBikeRenting(Bike bike) throws SQLException, EcoBikeException {
		System.out.println("Going to get current bike renting");
		String stm = "SELECT * FROM RentBike WHERE bike_barcode=? ORDER BY rent_id DESC LIMIT 1";
		PreparedStatement sqlStm = DBUtils.getConnection().prepareStatement(stm);
		sqlStm.setString(1, bike.getBikeBarCode());
		ResultSet resultI = sqlStm.executeQuery();
		int period = resultI.getInt("rent_period");
		int rentID = resultI.getInt("rent_id");
		stm = "SELECT * FROM EcoBikeTransaction WHERE rent_id=?";
		sqlStm = DBUtils.getConnection().prepareStatement(stm);
		sqlStm.setInt(1, rentID);
		ResultSet result = sqlStm.executeQuery();
		BikeTracker tracker = new BikeTracker(bike, rentID);
		tracker.setRentedTime(period);
		tracker.setStartTime(resultI.getString("start_time"));
		tracker.setActive(true);
		System.out.println("Getting transaction");
		while(result.next()) {
			PaymentTransaction trans = new PaymentTransaction();
			trans.setTransactionId(result.getInt("transaction_id"));
			trans.setAmount(result.getFloat("transaction_amount"));
			trans.setTransactionTime(result.getString("transaction_time"));
			trans.setCreditCardNumber(result.getString("creditcard_number"));
			trans.setRentID(result.getInt("rent_id"));
			trans.setContent(result.getString("transaction_detail"));
			tracker.addTransaction(trans);
		}
		return tracker;
	}
}

