package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class StringUtil {

	public static boolean isNull(String input) {
		if(input == null || input.equals("")) {
			return true;
		} else {
			return false;
		}
	}
	
	
	public static String getCurrentDateString() {
		LocalDateTime currentDate = LocalDateTime.now();
		DateTimeFormatter dtformat1 = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");
		return dtformat1.format(currentDate);
	}
}
