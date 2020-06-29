package form;

import org.springframework.web.multipart.MultipartFile;
import validator.ExcelFile;
import validator.FileRequired;

public class ExcelDiffByUploadForm extends ExcelDiffForm {
	
	@FileRequired
	@ExcelFile
	private MultipartFile newfile;
	
	@FileRequired
	@ExcelFile
	private MultipartFile oldfile;
	
	public String getNewFileName() {
		return getNewfile().getOriginalFilename();
	}
	public String getOldFileName() {
		return getOldfile().getOriginalFilename();
	}

	public MultipartFile getNewfile() {
		return newfile;
	}
	public void setNewfile(MultipartFile newfile) {
		this.newfile = newfile;
	}
	public MultipartFile getOldfile() {
		return oldfile;
	}
	public void setOldfile(MultipartFile oldfile) {
		this.oldfile = oldfile;
	}
}
