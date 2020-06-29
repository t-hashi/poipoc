package test.controller;

import java.lang.invoke.MethodHandles;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import form.ExcelDiffForm;

@Controller
public class TextDiffController extends BaseController {

private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@RequestMapping(value="/textdiff", method = RequestMethod.GET)
	public ModelAndView diff(ExcelDiffForm form, ModelAndView mav) {
		long start = System.currentTimeMillis();
		mav.setViewName("textdiff");
		long end = System.currentTimeMillis();
		log.info(new Object(){}.getClass().getEnclosingMethod().getName() + "() in " + (end - start) + " ms");
		return mav; 
	}

	@RequestMapping(value="/textdiff", method = RequestMethod.POST)
	public ModelAndView diff(@Valid ExcelDiffForm form, BindingResult bindingResult, ModelAndView mav) {
		long start = System.currentTimeMillis();
		mav.setViewName("textdiff");
		long end = System.currentTimeMillis();
		log.info(new Object(){}.getClass().getEnclosingMethod().getName() + "() in " + (end - start) + " ms");
		return mav; 
	}
}
