package test.service;

import java.io.FileInputStream;
import java.lang.invoke.MethodHandles;
import java.util.concurrent.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import form.ExcelDiffByPathForm;
import util.Consts;
import util.ExcelUtil;
import util.HTMLUtil;

@Service
public class ExcelDiffByPathService {
	
	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Async("DiffFileTaskExecutor")
	public Future<ExcelDiffByPathForm> asyncExcelDiff(ExcelDiffByPathForm form) throws Exception {
		return new AsyncResult<ExcelDiffByPathForm>(excelDiff(form));
    }


	public ExcelDiffByPathForm excelDiff(ExcelDiffByPathForm form) throws Exception {
		FileInputStream newis = null;
		FileInputStream oldis = null;
		try {
			ExcelUtil excelUtil = new ExcelUtil();
			
			// POI SAX version:low memory footprint.
			// DOM(unzip OOXML file) allocate huge memory.
			// https://poi.apache.org/components/spreadsheet/
			newis= new FileInputStream(form.getNewfilePath());
			oldis= new FileInputStream(form.getOldfilePath());
			if(System.getProperty(Consts.USER_XML_PARSER) != null && System.getProperty(Consts.USER_XML_PARSER).equals(Consts.XML_PARSER_SAX)) {
				excelUtil.excelDiffBySAX(newis, oldis, form.getNewfilePath(), form.getOldfilePath(), form);
			} else { // default DOM
				excelUtil.excelDiff(newis, oldis, form.getNewfilePath(), form.getOldfilePath(), form);
			}
			// Excel Macro Source differences by java-diff-utils(https://github.com/java-diff-utils/java-diff-utils)
			newis= new FileInputStream(form.getNewfilePath());
			oldis= new FileInputStream(form.getOldfilePath());
			excelUtil.excelMacroDiff(newis, oldis, form.getNewfilePath(), form.getOldfilePath(), form);
			HTMLUtil.convertDiffResultToHTMLFormat(form);
			
		} catch(Exception e) {
			log.error(new Object(){}.getClass().getEnclosingMethod().getName(), e);
			throw e;
		} finally {
			if(newis != null) {
				newis.close();
			}
			if(oldis != null) {
				oldis.close();
			}
		}
		return form;
    }

}
