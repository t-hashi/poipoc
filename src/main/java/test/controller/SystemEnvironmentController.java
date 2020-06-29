package test.controller;

import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import form.SystemEnvironmentForm;
import test.service.SystemEnvironmentService;

@Controller
public class SystemEnvironmentController extends BaseController {

	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Autowired
	private SystemEnvironmentService systemEnvironmentService;
	
	@RequestMapping(value="/systemenvironment", method = RequestMethod.GET)
	public ModelAndView showEnvironment(SystemEnvironmentForm form, ModelAndView mav) {
		long start = System.currentTimeMillis();
		try {			
			initialize(form);
			mav.addObject(getFormName(form), form);
			mav.setViewName("systemenvironment");
		} catch (Exception e) {
			log.error(this.getClass().getName(), e);
			mav.setViewName("error");
		}
		long end = System.currentTimeMillis();
		log.info(new Object(){}.getClass().getEnclosingMethod().getName() + "() in " + (end - start) + " ms");
		return mav; 
	}

	private void initialize(SystemEnvironmentForm form) {
		systemEnvironmentService.getSystemEnvironment(form);
	}
}
