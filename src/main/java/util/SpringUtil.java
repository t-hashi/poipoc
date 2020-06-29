package util;

import org.slf4j.Logger;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class SpringUtil {

	public static void debug(BindingResult bindingResult, Logger log) {
		if (bindingResult.hasErrors()) {
			for(FieldError err: bindingResult.getFieldErrors()) {
				log.info("error field=[" + err.getField() + "] code=[" + err.getCode() + "] message=[" + bindingResult.getFieldError(err.getField()).getDefaultMessage() + "]");				
			}
		}
	}
}
