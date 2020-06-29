package test.controller;

import java.lang.invoke.MethodHandles;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import form.CreateExcelForm;
import test.service.CreateExcelService;
import util.Consts;
import util.SpringUtil;

@Controller
public class CreateExcelController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Autowired
	private CreateExcelService createExcelService;
	
	@Autowired
    protected ResourceLoader resourceLoader;

	@RequestMapping(value="/createexcel", method = RequestMethod.GET)
	public ModelAndView create(CreateExcelForm form, ModelAndView mav) {
		initialize(form);
		mav.addObject(getFormName(form), form);
		mav.setViewName("createexcel");
		return mav;
	}

	
	@RequestMapping(value="/createexcel", method = RequestMethod.POST)
	public ModelAndView create(@Valid CreateExcelForm form, BindingResult bindingResult, ModelAndView mav) {
		long start = System.currentTimeMillis();
		try {			
			if (bindingResult.hasErrors()) {
				SpringUtil.debug(bindingResult, log);
				mav.addObject(getFormName(form), form);
				mav.setViewName("createexcel");
			} else {
				String pathAndFile = "static/images/excel.jpg"; // Insert image file to excel sheet.
	            Resource resource = resourceLoader.getResource("classpath:" + pathAndFile);
	            form.setImageFilePath(resource.getFile().getAbsolutePath());
				form = createExcelService.createExcel(form);
				mav.addObject(getFormName(form), form);
				mav.addObject("hasResult", true);
				mav.setViewName("createexcel");
			}
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
			mav.setViewName("error");
		}
		long end = System.currentTimeMillis();
		log.info(new Object(){}.getClass().getEnclosingMethod().getName() + "() in " + (end - start) + " ms");
		return mav; 
	}
	
	
	private void initialize(CreateExcelForm form) {	
		form.setSheetNumber(String.valueOf(Consts.DEFAULT_CREATE_SHEET_NUMBER));
		form.setRawNumber(String.valueOf(Consts.DEFAULT_CREATE_RAW_NUMBER));
		form.setColumnNumber(String.valueOf(Consts.DEFAULT_CREATE_COLUMN_NUMBER));
		form.setCellStringLength(String.valueOf(Consts.DEFAULT_CREATE_STRING_LENGTH));
		form.setNumericalRatio(String.valueOf(Consts.DEFAULT_CREATE_NUMERICAL_RATIO));
		form.setAlphabetRatio(String.valueOf(Consts.DEFAULT_CREATE_ALPHABET_RATIO));
		form.setKanjiRatio(String.valueOf(Consts.DEFAULT_CREATE_KANJI_RATIO));
		form.setInsertImage(Consts.CHECKBOX_ON);
	}
}
