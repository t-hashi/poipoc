package test.service;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import util.FileUtil;
import util.StringUtil;

@Service
public class FileReapTaskService {

	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
    @Async("FileReapTaskExecutor")
    public void fileReap() {
        try {
            log.info(new Object(){}.getClass().getEnclosingMethod().getName() + " start");
            while(true) {
            	// delete old file.
            	FileUtil.deleteOldFile(FileUtil.getFileCreateTempDirectory(), StringUtil.getCurrentDateString());
            	TimeUnit.SECONDS.sleep(60);
            }
            
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
        	log.info(new Object(){}.getClass().getEnclosingMethod().getName() + " end");
        }
    }

}