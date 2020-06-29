package form;

import validator.ExcelFilePath;

public class ExcelDiffByPathForm extends ExcelDiffForm {
	
	@ExcelFilePath
	private String newfilePath;
	
	@ExcelFilePath
	private String oldfilePath;

	public String getNewfilePath() {
		return newfilePath;
	}
	public void setNewfilePath(String newfilePath) {
		this.newfilePath = newfilePath;
	}
	public String getOldfilePath() {
		return oldfilePath;
	}
	public void setOldfilePath(String oldfilePath) {
		this.oldfilePath = oldfilePath;
	}
}
