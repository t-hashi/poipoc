package test.controller;

import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import util.Consts;

@RestController
public class SetUserParameterRestController {
	
	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@GetMapping("/usesax")
	public String useSAX() {
    	System.setProperty(Consts.USER_XML_PARSER, Consts.XML_PARSER_SAX);
    	log.info(new Object(){}.getClass().getEnclosingMethod().getName() + "() java.lang.System#setProperties(key="+ Consts.USER_XML_PARSER + ", value=" + Consts.XML_PARSER_SAX + ")");
        return "success";
    }
	
	
	@GetMapping("/usedom")
	public String useDOM() {
    	System.setProperty(Consts.USER_XML_PARSER, Consts.XML_PARSER_DOM);
    	log.info(new Object(){}.getClass().getEnclosingMethod().getName() + "() java.lang.System#setProperties(key="+ Consts.USER_XML_PARSER + ", value=" + Consts.XML_PARSER_DOM + ")");
        return "success";
    }
	
	
	@GetMapping("/usesync")
	public String useSYNC() {
    	System.setProperty(Consts.USER_EXCEL_DIFF_TASK, Consts.SYNC_TASK);
    	log.info(new Object(){}.getClass().getEnclosingMethod().getName() + "() java.lang.System#setProperties(key="+ Consts.USER_EXCEL_DIFF_TASK + ", value=" + Consts.SYNC_TASK + ")");
        return "success";
    }
	
	
	@GetMapping("/useasync")
	public String useASYNC() {
    	System.setProperty(Consts.USER_EXCEL_DIFF_TASK, Consts.ASYNC_TASK);
    	log.info(new Object(){}.getClass().getEnclosingMethod().getName() + "() java.lang.System#setProperties(key="+ Consts.USER_EXCEL_DIFF_TASK + ", value=" + Consts.ASYNC_TASK + ")");
        return "success";
    }
	
}