package util;

import java.io.File;
import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtil {
	
	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public static void deleteOldFile(String targetDirectory, String currentDateString) {
		log.info(new Object(){}.getClass().getEnclosingMethod().getName() + "() current date:" + currentDateString);
		File dir = new File(targetDirectory);
		File[] files = dir.listFiles();
		for(File file : files) {
			if(file.isFile()) {
				String fileName = file.getName();
				if(fileName.startsWith(Consts.CREATE_EXCEL_FILE_PREIX) && fileName.endsWith(Consts.CREATE_EXCEL_FILE_EXTENSION)) {
					int beginIndex = fileName.indexOf(Consts.CREATE_EXCEL_FILE_PREIX) + Consts.CREATE_EXCEL_FILE_PREIX.length();
					int endIndex = fileName.indexOf(Consts.CREATE_EXCEL_FILE_EXTENSION);
					//System.out.println("file timestamp:" + fileName.substring(beginIndex, endIndex));
					long lifeTime = Long.parseLong(currentDateString) - Long.parseLong(fileName.substring(beginIndex, endIndex));
					//System.out.println(" file lifetime:" + lifeTime + " ms");
					if(lifeTime > Long.parseLong(Consts.FILE_DELETE_INTERVAL)) {
						if(file.delete()) {
							log.info(new Object(){}.getClass().getEnclosingMethod().getName() + "() delete [" + fileName + "] successfully.");
						}
					}
				}	
			}
		}
	}
	
	
	public static String getFileCreateTempDirectory() {
		return System.getProperty("java.io.tmpdir");
	}
	
}
