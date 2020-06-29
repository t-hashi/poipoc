package form;

import util.LogUtil;

public class DiffSheetItem {

	public DiffSheetItem(String newFileName, String oldFileName, String sheetName, String comment) {
		this.newFileName = newFileName;
		this.oldFileName = oldFileName;
		this.sheetName = sheetName;
		this.comment = comment;
	}

	private String newFileName;
	private String oldFileName;
	private String sheetName;
	private String comment;
	
	public void debug() {
		LogUtil.info("new file:" + this.newFileName + " old file:" + this.oldFileName + " sheet:" + this.sheetName + this.comment);
	}
	
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public String getNewFileName() {
		return newFileName;
	}
	public void setNewFileName(String newFileName) {
		this.newFileName = newFileName;
	}
	public String getOldFileName() {
		return oldFileName;
	}
	public void setOldFileName(String oldFileName) {
		this.oldFileName = oldFileName;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

	
}
