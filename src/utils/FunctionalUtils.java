package utils;

import java.util.Locale;
import java.util.Date;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import exceptions.ecobike.EcoBikeException;

public class FunctionalUtils {
	
	  @SuppressWarnings("unused")
	  private static Logger LOGGER = getLogger(FunctionalUtils.class.getName());

	  static {
	    System.setProperty("java.util.logging.SimpleFormatter.format", "[%4$-4s] [%1$tF %1$tT] [%2$-7s] %5$s %n");
	  }

	  public static Logger getLogger(String className) {
	    return Logger.getLogger(className);
	  }
	  
	  public static Date stringToDate(String string) throws EcoBikeException {
		  try {
			  SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd hh:mm:ss zzz yyyy", Locale.ENGLISH);
			  Date parsedDate = (Date) dateFormat.parse(string);
			  return parsedDate;	
		  } 
		  catch(Exception err) { 
			  throw new EcoBikeException(err.toString());
		  }
	  }

	public static boolean contains(String str, String regex) {
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(str);
		if(m.find()) {
			return true;
		}
		return false;
	}
	
	
	public static String md5(String message) {
		String digest = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			byte[] hash = md.digest(message.getBytes("UTF-8"));
			// converting byte array to Hexadecimal String
			StringBuilder sb = new StringBuilder(2 * hash.length);
			for(byte b : hash) {
				sb.append(String.format("%02x", b & 0xff));
			}
			digest = sb.toString();
		} 
		catch(UnsupportedEncodingException | NoSuchAlgorithmException ex) {
			FunctionalUtils.getLogger(FunctionalUtils.class.getName());
			digest = "";
		}
		return digest;
	}
}
