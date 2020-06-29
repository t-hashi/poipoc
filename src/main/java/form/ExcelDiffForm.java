package form;

import java.util.ArrayList;
import java.util.Iterator;

public class ExcelDiffForm extends BaseForm {

	private String comment;
	private ArrayList<DiffSheetItem> diffSheetItemList = new ArrayList<DiffSheetItem>();
	private ArrayList<DiffCellItem> diffCellItemList = new ArrayList<DiffCellItem>();
	private ArrayList<DiffMacroItem> diffMacroItemList = new ArrayList<DiffMacroItem>();
	private boolean hasDiffSheet;
	private boolean hasDiffCell;
	private boolean hasDiffMacro;
	
	public boolean getHasDiffSheet() {
		if ((getDiffSheetItemList() != null) && (getDiffSheetItemList().size() != 0)) {
			this.hasDiffSheet = true;
		} else {
			this.hasDiffSheet = false;
		}
		return this.hasDiffSheet;
	}
	public void setHasDiffSheet(boolean diffSheet) {
		this.hasDiffSheet = diffSheet;
	}
	
	public boolean getHasDiffCell() {
		if ((getDiffCellItemList() != null) && (getDiffCellItemList().size() != 0)) {
			this.hasDiffCell = true;
		} else {
			this.hasDiffCell = false;
		}
		return this.hasDiffCell;
	}
	public void setHasDiffCell(boolean hasDiffCell) {
		this.hasDiffCell = hasDiffCell;
	}
	
	public boolean getHasDiffMacro() {
		if ((getDiffMacroItemList() != null) && (getDiffMacroItemList().size() != 0)) {
			this.hasDiffMacro = true;
		} else {
			this.hasDiffMacro = false;
		}
		return this.hasDiffMacro;
	}
	public void setHasDiffMacro(boolean hasDiffMacro) {
		this.hasDiffMacro = hasDiffMacro;
	}
	
	public void debug() {
		Iterator<DiffSheetItem> it = diffSheetItemList.iterator();
		while (it.hasNext()) {
			it.next().debug();
		}
		Iterator<DiffCellItem> it2 = diffCellItemList.iterator();
		while (it2.hasNext()) {
			it2.next().debug();
		}
		Iterator<DiffMacroItem> it3 = diffMacroItemList.iterator();
		while (it3.hasNext()) {
			it3.next().debug();
		}
	}
	
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public ArrayList<DiffSheetItem> getDiffSheetItemList() {
		return diffSheetItemList;
	}
	public void setDiffSheetItemList(ArrayList<DiffSheetItem> diffSheetItemList) {
		this.diffSheetItemList = diffSheetItemList;
	}
	public ArrayList<DiffCellItem> getDiffCellItemList() {
		return diffCellItemList;
	}
	public void setDiffCellItemList(ArrayList<DiffCellItem> diffCellItemList) {
		this.diffCellItemList = diffCellItemList;
	}
	public ArrayList<DiffMacroItem> getDiffMacroItemList() {
		return diffMacroItemList;
	}
	public void setDiffMacroItemList(ArrayList<DiffMacroItem> diffMacroItemList) {
		this.diffMacroItemList = diffMacroItemList;
	}

}
