package form;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Pattern;

import util.Consts;

public class CreateExcelForm extends BaseForm {

	@Pattern(regexp="^([1-9][0-9]*)$", message = "{createExcelForm.positiveNumber}")
	private String sheetNumber;
	@Pattern(regexp="^([1-9][0-9]*)$", message = "{createExcelForm.positiveNumber}")
	private String rawNumber;
	@Pattern(regexp="^([1-9][0-9]*)$", message = "{createExcelForm.positiveNumber}")
	private String columnNumber;
	@Pattern(regexp="^([1-9][0-9]*)$", message = "{createExcelForm.positiveNumber}")
	private String cellStringLength;
	@Pattern(regexp="^(0|[1-9][0-9]*)$", message = "{createExcelForm.nonnegativeNumber}")
	private String numericalRatio;
	@Pattern(regexp="^(0|[1-9][0-9]*)$", message = "{createExcelForm.nonnegativeNumber}")
	private String alphabetRatio;
	@Pattern(regexp="^(0|[1-9][0-9]*)$", message = "{createExcelForm.nonnegativeNumber}")
	private String kanjiRatio;
	private String insertImage;
	private String imageFilePath;
	private String filePath;
	private String rawCellDataSize;
	private String ooxmlSize;
	private String compressRatio;
	
	@AssertTrue(message="Total(numerical+alphabet+kanji) must be 100%")
    public boolean isRatioSumValid() {
		try {
			if ((Integer.parseInt(numericalRatio) + Integer.parseInt(alphabetRatio) + Integer.parseInt(kanjiRatio)) == 100) {
				return true;
			}
		} catch(Exception e) {
			return false;
		}
        return false;
    }
	
	@AssertTrue(message="Max size(sheetNumber*rawNumber*columnNumber*cellStringLength) is " + Consts.CREATE_MAX_FILE_SIZE)
    public boolean isFileSizeValid() {
		try {
			if ((Integer.parseInt(sheetNumber) * Integer.parseInt(rawNumber) * Integer.parseInt(columnNumber) * Integer.parseInt(cellStringLength)) <= Consts.CREATE_MAX_FILE_SIZE) {
				return true;
			}
		} catch(Exception e) {
			return false;
		}
        return false;
    }
	
	public String getSheetNumber() {
		return sheetNumber;
	}
	public void setSheetNumber(String sheetNumber) {
		this.sheetNumber = sheetNumber;
	}
	public String getRawNumber() {
		return rawNumber;
	}

	public void setRawNumber(String rawNumber) {
		this.rawNumber = rawNumber;
	}

	public String getColumnNumber() {
		return columnNumber;
	}

	public void setColumnNumber(String columnNumber) {
		this.columnNumber = columnNumber;
	}

	public String getCellStringLength() {
		return cellStringLength;
	}
	public void setCellStringLength(String cellStringLength) {
		this.cellStringLength = cellStringLength;
	}
	public String getNumericalRatio() {
		return numericalRatio;
	}
	public void setNumericalRatio(String numericalRatio) {
		this.numericalRatio = numericalRatio;
	}
	public String getAlphabetRatio() {
		return alphabetRatio;
	}
	public void setAlphabetRatio(String alphabetRatio) {
		this.alphabetRatio = alphabetRatio;
	}
	public String getKanjiRatio() {
		return kanjiRatio;
	}
	public void setKanjiRatio(String kanjiRatio) {
		this.kanjiRatio = kanjiRatio;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getRawCellDataSize() {
		return rawCellDataSize;
	}
	public void setRawCellDataSize(String rawCellDataSize) {
		this.rawCellDataSize = rawCellDataSize;
	}
	public String getOoxmlSize() {
		return ooxmlSize;
	}
	public void setOoxmlSize(String ooxmlSize) {
		this.ooxmlSize = ooxmlSize;
	}
	public String getCompressRatio() {
		return compressRatio;
	}
	public void setCompressRatio(String compressRatio) {
		this.compressRatio = compressRatio;
	}
	public String getInsertImage() {
		return insertImage;
	}

	public void setInsertImage(String insertImage) {
		this.insertImage = insertImage;
	}

	public void debug() {
		System.out.println("sheetNumber:" + this.sheetNumber);
		System.out.println("rawNumber:" + this.rawNumber);
		System.out.println("columnNumber:" + this.columnNumber);
		System.out.println("cellStringLength:" + this.cellStringLength);
		System.out.println("numericalRatio:" + this.numericalRatio);
		System.out.println("alphabetRatio:" + this.alphabetRatio);
		System.out.println("kanjiRatio:" + this.kanjiRatio);
		System.out.println("insertImage:" + this.insertImage);
		System.out.println("filePath:" + this.filePath);
	}

	public String getImageFilePath() {
		return imageFilePath;
	}

	public void setImageFilePath(String imageFilePath) {
		this.imageFilePath = imageFilePath;
	}
	
}
