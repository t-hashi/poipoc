package validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import util.ExcelUtil;

public class ExcelFileValidator implements ConstraintValidator<ExcelFile, MultipartFile> {
    @Override
    public void initialize(ExcelFile constraint) {}

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
    	try {
    		ExcelUtil excelUtil = new ExcelUtil();
    		if(excelUtil.isExcel(multipartFile)) {
    			return true;
    		}
    	} catch(Exception e) {
    		return false;
    	}
        return false;
    }
}