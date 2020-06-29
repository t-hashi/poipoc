package test.controller;

import java.lang.invoke.MethodHandles;
import java.util.concurrent.Future;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import form.ExcelDiffByPathForm;
import test.service.ExcelDiffByPathService;
import util.Consts;
import util.SpringUtil;

@Controller
public class ExcelDiffByPathController extends BaseController {
	
	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Autowired
	private ExcelDiffByPathService excelDiffByPathService;

	@RequestMapping(value="/exceldiffbypath", method = RequestMethod.GET)
	public ModelAndView diff(ExcelDiffByPathForm form, ModelAndView mav) {
		initialize(form);
		mav.addObject(getFormName(form), form);
		mav.setViewName("exceldiffbypath");
		return mav;
	}

	@RequestMapping(value="/exceldiffbypath", method = RequestMethod.POST)
	public ModelAndView diff(@Valid ExcelDiffByPathForm form, BindingResult bindingResult, ModelAndView mav) {
		long start = System.currentTimeMillis();
		try {
			if (bindingResult.hasErrors()) {
				SpringUtil.debug(bindingResult, log);
				mav.addObject(getFormName(form), form);
				mav.setViewName("exceldiffbypath");
			} else {
				
				if(System.getProperty(Consts.USER_EXCEL_DIFF_TASK) != null && System.getProperty(Consts.USER_EXCEL_DIFF_TASK).equals(Consts.ASYNC_TASK)) {
					Future<ExcelDiffByPathForm> future = excelDiffByPathService.asyncExcelDiff(form);
					long waitTime = 0;
				    while (true) {
				        if (future.isDone()) {
				            mav.addObject(getFormName(future.get()), future.get());
				            break;
				        }
				        Thread.sleep(Consts.ASYNC_TASK_WAIT_INTERVAL);
				        waitTime = waitTime + Consts.ASYNC_TASK_WAIT_INTERVAL;
				        log.info("Waiting for asysnc task result. Total " + waitTime + " ms");
				    }
				} else { // default SYNC
					form = excelDiffByPathService.excelDiff(form);
					mav.addObject(getFormName(form), form);
				}
				
				mav.addObject("hasResult", true);
				mav.setViewName("exceldiffbypath");
			}
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
			mav.setViewName("error");
		}
		long end = System.currentTimeMillis();
		log.info(new Object(){}.getClass().getEnclosingMethod().getName() + "() in " + (end - start) + " ms");
		return mav; 
	}
	
	private void initialize(ExcelDiffByPathForm form) {	
		form.setNewfilePath(Consts.DEFAULT_NEW_FILE_PATH);
		form.setOldfilePath(Consts.DEFAULT_OLD_FILE_PATH);
	}
}
