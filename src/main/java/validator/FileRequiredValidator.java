package validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FileRequiredValidator implements ConstraintValidator<FileRequired, MultipartFile> {
    @Override
    public void initialize(FileRequired constraint) {}

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext context) {
    	if(multipartFile != null && !multipartFile.getOriginalFilename().isEmpty()) {
    		return true;
    	}
        return false;
    }
}