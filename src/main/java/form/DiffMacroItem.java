package form;

import java.lang.invoke.MethodHandles;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DiffMacroItem {
	
	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public DiffMacroItem(String newFileName, String oldFileName, String newMacroValue, String oldMacroValue, String comment) {
		this.newFileName = newFileName;
		this.oldFileName = oldFileName;
		this.newMacroValue = newMacroValue;
		this.oldMacroValue = oldMacroValue;
		this.comment = comment;
	}
	
	private String newFileName;
	private String oldFileName;
	private String newMacroValue;
	private String oldMacroValue;
	private String newHTMLMacroValue;
	private String oldHTMLMacroValue;
	private String comment;
	
	public String getNewHTMLMacroValue() {
		return this.newHTMLMacroValue;
	}
	public String getOldHTMLMacroValue() {
		return this.oldHTMLMacroValue;
	}
	public void setNewHTMLMacroValue(String newHTMLMacroValue) {
		this.newHTMLMacroValue = newHTMLMacroValue;
	}
	public void setOldHTMLMacroValue(String oldHTMLMacroValue) {
		this.oldHTMLMacroValue = oldHTMLMacroValue;
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
	public String getNewMacroValue() {
		return newMacroValue;
	}
	public void setNewMacroValue(String newMacroValue) {
		this.newMacroValue = newMacroValue;
	}
	public String getOldMacroValue() {
		return oldMacroValue;
	}
	public void setOldMacroValue(String oldMacroValue) {
		this.oldMacroValue = oldMacroValue;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public void debug() {
		log.info(new Object(){}.getClass().getEnclosingMethod().getName() + " new file:" + this.newFileName + " old file:" + this.oldFileName + " newMacroValue:" + this.newMacroValue + " oldMacroValue:" + this.oldMacroValue + " comment:" + this.comment);
	}
	
}
