package test.service;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import form.ExcelDiffByUploadForm;
import util.Consts;
import util.ExcelUtil;
import util.HTMLUtil;

@Service
public class ExcelDiffByUploadService {
	
	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Async("DiffFileTaskExecutor")
	public Future<ExcelDiffByUploadForm> asyncExcelDiff(ExcelDiffByUploadForm form) throws Exception {
		return new AsyncResult<ExcelDiffByUploadForm>(excelDiff(form));
    }
	
	
	public ExcelDiffByUploadForm excelDiff(ExcelDiffByUploadForm form) throws Exception {
		try {
			ExcelUtil excelUtil = new ExcelUtil();
			
			// POI DOM version:DOM(uncompressed OOXML file) allocate huge memory.
			// POI SAX version:low memory footprint.
			// https://poi.apache.org/components/spreadsheet/
			if(System.getProperty(Consts.USER_XML_PARSER) != null && System.getProperty(Consts.USER_XML_PARSER).equals(Consts.XML_PARSER_SAX)) {
				excelUtil.excelDiffBySAX(form.getNewfile().getInputStream(), form.getOldfile().getInputStream(), form.getNewfile().getOriginalFilename(), form.getOldfile().getOriginalFilename(), form);
			} else { // default DOM
				excelUtil.excelDiff(form.getNewfile().getInputStream(), form.getOldfile().getInputStream(), form.getNewfile().getOriginalFilename(), form.getOldfile().getOriginalFilename(), form);
			}
			
			// Excel Macro Source differences by java-diff-utils(https://github.com/java-diff-utils/java-diff-utils)
			excelUtil.excelMacroDiff(form.getNewfile().getInputStream(), form.getOldfile().getInputStream(), form.getNewfile().getOriginalFilename(), form.getOldfile().getOriginalFilename(), form);
			HTMLUtil.convertDiffResultToHTMLFormat(form);
			
		} catch(Exception e) {
			log.error(new Object(){}.getClass().getEnclosingMethod().getName(), e);
			throw e;
		}
		return form;
    }
}
