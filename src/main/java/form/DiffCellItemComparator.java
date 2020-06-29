package form;

import java.util.Comparator;

public class DiffCellItemComparator implements Comparator<DiffCellItem>{

	@Override
	public int compare(DiffCellItem cell1, DiffCellItem cell2) {
		return cell1.getSheetName().compareTo(cell2.getSheetName());
	}
}