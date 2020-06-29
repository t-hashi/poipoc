package test;

import java.lang.invoke.MethodHandles;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.fileupload.impl.SizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import util.ServletUtil;

@Component
public class ExceptionResolver implements HandlerExceptionResolver {
	
	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
        ModelAndView mav = new ModelAndView();
		if (e instanceof MultipartException && e.getCause() instanceof IllegalStateException && e.getCause().getCause() instanceof SizeLimitExceededException) {
			mav.addObject("message", getMessage(e));
			mav.setViewName(ServletUtil.getPathFromRequestURL(request));
			log.info(new Object(){}.getClass().getEnclosingMethod().getName() + " " + e.getCause().getMessage());
        } else {
        	mav.addObject("message", e.getCause().getMessage());
        	mav.setViewName("error");
			log.info(new Object(){}.getClass().getEnclosingMethod().getName() + " " + e.getCause().getMessage());
        }
        return mav;
    }
    
    private String getMessage(Exception e) {
    	String outString = e.getCause().getMessage();
    	String[] tempStrings = outString.split(":", 2);
		return tempStrings[1];
    }
}