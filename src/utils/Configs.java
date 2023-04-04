package utils;

public class Configs {
    public enum BikeType{
        NormalBike,
        EBike,
        TwinBike,
        TwinEBike,
        Others;
    	
    	public static BikeType toBikeType(String string) {
    		if(string.equalsIgnoreCase(NormalBike.toString())) {
    			return NormalBike;
    		} 
    		else if(string.equalsIgnoreCase(EBike.toString())) {
    			return EBike;
    		} 
    		else if(string.equalsIgnoreCase(TwinBike.toString())) {
    			return TwinBike;
    		}
    		else if(string.equalsIgnoreCase(TwinEBike.toString())) {
    			return TwinEBike;
    		} 
    		else {
    			return Others;
    		}
    	}
    	private static java.util.Map<BikeType, Float> chargeMultiplierDictionary = new java.util.HashMap<BikeType, Float>();
    	private static java.util.Map<BikeType, Float> priceDict = new java.util.HashMap<BikeType, Float>();
    	private static java.util.Map<BikeType, Integer> saddleDict = new java.util.HashMap<BikeType, Integer>();
    	private static java.util.Map<BikeType, Integer> pedalDict = new java.util.HashMap<BikeType, Integer>();
    	private static java.util.Map<BikeType, Integer> rearSeatDict = new java.util.HashMap<BikeType, Integer>();
    	private static java.util.Map<BikeType, Integer> motorDict = new java.util.HashMap<BikeType, Integer>();
    	
    	public static void setMultiplier(Configs.BikeType type, float multiplier) {
    		chargeMultiplierDictionary.put(type, multiplier);
    	}
    	
    	public static float getMultiplier(Configs.BikeType type) {
    		return chargeMultiplierDictionary.get(type);
    	}
    	
    	public static void setTypePrice(Configs.BikeType type, float price) {
    		priceDict.put(type, price);
    	}
    	
    	public static float getTypePrice(Configs.BikeType type) {
    		return priceDict.get(type);
    	}
    	
    	public static void setTypeSaddle(Configs.BikeType type, int num) {
    		saddleDict.put(type, num);
    	}
    	
    	public static int getTypeSadde(Configs.BikeType type) {
    		return saddleDict.get(type);
    	}
    	
    	public static void setTypePedals(Configs.BikeType type, int num) {
    		pedalDict.put(type, num);
    	}
    	
    	public static int getTypePedals(Configs.BikeType type) {
    		return pedalDict.get(type);
    	}
    	
    	public static void setTypeMotor(Configs.BikeType type, int num) {
    		motorDict.put(type, num);
    	}
    	
    	public static int getTypeMotor(Configs.BikeType type) {
    		return motorDict.get(type);
    	}
    	
    	public static void setTypeRearSeat(Configs.BikeType type, int num) {
    		rearSeatDict.put(type, num);
    	}
    	
    	public static int getTypeRearSeat(Configs.BikeType type) {
    		return rearSeatDict.get(type);
    	}
        
    }


    public static float freeOfChargeTimeInMinute = 10f;

    public static float firstChargeTimeIntervalInMinute = 30f;
    public static float firstChargeTimeIntervalCost = 10000f;

    public static float chargeTimeIntervalInMinute = 15f;
    public static float chargeTimeIntervalCost = 3000f;
    
    public static String CURRENCY = "VND";
    public static float PERCENT_VAT = 10;
    
    // static resource
    public static final String IMAGE_PATH = "assets/icons";
    public static final String BIKE_IMAGE_LIB = "assets/bikes";
    public static final String DOCK_IMAGE_LIB = "assets/docks";
    public static final String LIST_DOCK_SCREEN_PATH = "/views/fxml/FXML_ListDockScreen.fxml";
    public static final String MAIN_SCREEN_PATH = "/views/fxml/FXML_MainScreen.fxml";
    public static final String PAYING_FOR_DEPOSIT_SCREEN_PATH = "/views/fxml/FXML_PayForDepositScreen.fxml";
    public static final String PAYING_FOR_RENTAL_SCREEN_PATH = "/views/fxml/FXML_PayForRentalScreen.fxml";
    public static final String PAYMENT_SCREEN_PATH = "/views/fxml/FXML_PaymentScreen.fxml";
    public static final String POPUP_SCREEN_PATH = "/views/fxml/FXML_PopupScreen.fxml";
    public static final String RETURN_BIKE_SCREEN_PATH = "/views/fxml/FXML_ReturnBikeScreen.fxml";
    public static final String SPLASH_SCREEN_PATH = "/views/fxml/FXML_SplashScreen.fxml";
    public static final String VIEW_BIKE_SCREEN_PATH = "/views/fxml/FXML_ViewBikeScreen.fxml";
    public static final String VIEW_DOCK_SCREEN_PATH = "/views/fxml/FXML_ViewDockScreen.fxml";
    public static final String INVOICE_SCREEN_PATH = "/views/fxml/FXML_InvoiceScreen.fxml";
	public static final String BIKE_IN_DOCK_PATH = "/views/fxml/FXML_BikeInDock.fxml";
	public static final String BIKES_IN_DOCK_SCREEN_PATH = "/views/fxml/FXML_AvailableBikesInDock.fxml";
	public static final String PAYMENT_METHOD_SCREEN_PATH = "/views/fxml/FXML_PayingMethodScreen.fxml";
	public static final String SELECT_DOCK_SCREEN_PATH = "/views/fxml/FXML_ListDockScreen.fmxl";
	public static final String DOCK_FOR_RETURN_SCREEN_PATH = "/views/fxml/FXML_DockForReturn.fxml";
	public static final String LIST_RENTED_BIKE_SCREEN_PATH = "/views/fxml/FXML_ListRentedBikeScreen.fxml";
	public static final String BIKE_TO_RETURN_SCREEN_PATH = "/views/fxml/FXML_BikeToReturn.fxml";


	public enum BIKE_STATUS {
		FREE,
		RENTED,		
		PAUSED
	}
	
	public enum TransactionType {
		PAY_DEPOSIT("PAY_DEPOSIT"),
		PAY_RENTAL("PAY_RENTAL"),
		RETURN_DEPOSIT("RETURN_DEPOSIT");

		private String transactionType;
		TransactionType(String string) {
			this.transactionType = string;
		}
		
		public String toString() {
			return this.transactionType;
		}
	}
	public static final String ENTITIES_PKG = "entities.";
	public static final String API_BASE_URL = "https://ecopark-system-api.herokuapp.com";
	public static final String API_TRANSACTION = "/api/card/processTransaction";
	public static final String API_RESET_BALANCE = "/api/card/reset-balance";
}
