package form;

import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HTMLUtil;

public class DiffCellItem {
	
	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public DiffCellItem(String newFileName, String oldFileName, String sheetName, String cellName, String newCellValue, String oldCellValue, String comment) {
		this.newFileName = newFileName;
		this.oldFileName = oldFileName;
		this.sheetName = sheetName;
		this.cellName = cellName;
		this.newCellValue = newCellValue;
		this.oldCellValue = oldCellValue;
		this.comment = comment;
	}
	
	private String newFileName;
	private String oldFileName;
	private String sheetName;
	private String cellName;
	private String newCellValue;
	private String oldCellValue;
	private String comment;
	
	public void debug() {
		log.info(new Object(){}.getClass().getEnclosingMethod().getName() + " new file:" + newFileName + " old file:" + oldFileName + " sheet:" + sheetName + " cell:" + cellName + " " + newCellValue + " " + oldCellValue + this.comment);
	}
	
	public String getNewHTMLCellValue() {
		return HTMLUtil.toCellValue(newCellValue);
	}
	public String getOldHTMLCellValue() {
		return HTMLUtil.toCellValue(oldCellValue);
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
	public String getSheetName() {
		return sheetName;
	}
	public void setSheetName(String sheetName) {
		this.sheetName = sheetName;
	}
	public String getCellName() {
		return cellName;
	}
	public void setCellName(String cellName) {
		this.cellName = cellName;
	}
	public String getNewCellValue() {
		return newCellValue;
	}
	public void setNewCellValue(String newCellValue) {
		this.newCellValue = newCellValue;
	}
	public String getOldCellValue() {
		return oldCellValue;
	}
	public void setOldCellValue(String oldCellValue) {
		this.oldCellValue = oldCellValue;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}

	
}
