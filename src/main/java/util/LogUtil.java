package util;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {
	
	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	public static void info(String message) {
		log.info(message);
	}
	
	public static void trace(String message) {
		log.trace(message);
	}
	
	public static void trace(String message, Exception e) {
		log.trace(message, e);
	}
	
	public static void error(String message) {
		log.error(message);
	}
	
	public static void error(String message, Exception e) {
		log.error(message, e);
	}
	
	
	
	
}
