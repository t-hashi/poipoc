package test.controller;

import org.springframework.stereotype.Controller;

@Controller
public class BaseController {
	
	/**
	 * for naming rule. thymeleaf form name must be (formClass:XXXForm -> thyleaf key:xXXForm)
	 * @param obj
	 * @return
	 */
	public String getFormName(Object obj) {
		String formClassName = obj.getClass().getName();
		return formClassName.substring(0, 1).toLowerCase() + formClassName.substring(1);
	}
	
}
