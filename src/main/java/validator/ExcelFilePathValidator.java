package validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import util.ExcelUtil;
import util.StringUtil;

public class ExcelFilePathValidator implements ConstraintValidator<ExcelFilePath, String> {
    @Override
    public void initialize(ExcelFilePath constraint) {}

    @Override
    public boolean isValid(String filePath, ConstraintValidatorContext context) {
    	try {
    		if(StringUtil.isNull(filePath)) {
    			return false;
    		}
    		ExcelUtil excelUtil = new ExcelUtil();
    		if(excelUtil.isExcel(filePath)) {
    			return true;
    		}
    	} catch(Exception e) {
    		return false;
    	}
        return false;
    }
}