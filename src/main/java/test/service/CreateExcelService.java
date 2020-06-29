package test.service;

import java.lang.invoke.MethodHandles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import form.CreateExcelForm;
import util.Consts;
import util.ExcelUtil;
import util.StringUtil;

@Service
public class CreateExcelService {
	
	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	private int fileSize;
	private int rawNumber;
	private int columnNumber;
	private int sheetNumber;
	private int cellStringLength;
	private int numericalRatio;
	private int alphabetRatio;
	private int kanjiRatio;
	private boolean hasImageInsertedSheet;
	private String filePath;
	private String imageFilePath;

	public CreateExcelForm createExcel(CreateExcelForm form) throws Exception {
		try {
			if(StringUtil.isNull(form.getSheetNumber())) {
				this.sheetNumber = Consts.DEFAULT_CREATE_SHEET_NUMBER;
				form.setSheetNumber(String.valueOf(this.sheetNumber));
			} else {
				this.sheetNumber = Integer.parseInt(form.getSheetNumber());
			}
			if(StringUtil.isNull(form.getRawNumber())) {
				this.rawNumber = Consts.DEFAULT_CREATE_RAW_NUMBER;
				form.setRawNumber(String.valueOf(this.rawNumber));
			} else {
				this.rawNumber = Integer.parseInt(form.getRawNumber());
			}
			if(StringUtil.isNull(form.getColumnNumber())) {
				this.columnNumber = Consts.DEFAULT_CREATE_COLUMN_NUMBER;
				form.setColumnNumber(String.valueOf(this.columnNumber));
			} else {
				this.columnNumber = Integer.parseInt(form.getColumnNumber());
			}
			if(StringUtil.isNull(form.getCellStringLength())) {
				this.cellStringLength = Consts.DEFAULT_CREATE_STRING_LENGTH;
				form.setCellStringLength(String.valueOf(this.cellStringLength));
			} else {
				this.cellStringLength = Integer.parseInt(form.getCellStringLength());	
			}
			if(StringUtil.isNull(form.getNumericalRatio())) {
				this.numericalRatio = Consts.DEFAULT_CREATE_NUMERICAL_RATIO;
				form.setNumericalRatio(String.valueOf(this.numericalRatio));
			} else {
				this.numericalRatio = Integer.parseInt(form.getNumericalRatio());	
			}
			if(StringUtil.isNull(form.getAlphabetRatio())) {
				this.alphabetRatio = Consts.DEFAULT_CREATE_ALPHABET_RATIO;
				form.setAlphabetRatio(String.valueOf(this.alphabetRatio));
			} else {
				this.alphabetRatio = Integer.parseInt(form.getAlphabetRatio());	
			}
			if(StringUtil.isNull(form.getKanjiRatio())) {
				this.kanjiRatio = Consts.DEFAULT_CREATE_KANJI_RATIO;
				form.setKanjiRatio(String.valueOf(this.kanjiRatio));
			} else {
				this.kanjiRatio = Integer.parseInt(form.getKanjiRatio());	
			}
			if(StringUtil.isNull(form.getInsertImage())) {
				this.hasImageInsertedSheet = false;
				form.setInsertImage(Consts.CHECKBOX_OFF);
			} else {
				this.hasImageInsertedSheet = true;	
			}
			this.imageFilePath = form.getImageFilePath();
			
			
			ExcelUtil excelUtil = new ExcelUtil();
			String[] result = excelUtil.createExcel(sheetNumber, rawNumber, columnNumber, cellStringLength, numericalRatio, alphabetRatio, kanjiRatio, hasImageInsertedSheet, imageFilePath);
			form.setFilePath(result[0]);
			form.setRawCellDataSize(result[1]);
			form.setOoxmlSize(result[2]);
			form.setCompressRatio(result[3]);
			
		} catch(Exception e) {
			log.error(new Object(){}.getClass().getEnclosingMethod().getName(), e);
			throw e;
		}
		return form;
	}

	
	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public int getSheetNumber() {
		return sheetNumber;
	}

	public void setSheetNumber(int sheetNumber) {
		this.sheetNumber = sheetNumber;
	}

	public int getCellStringLength() {
		return cellStringLength;
	}

	public void setCellStringLength(int stringAverageLength) {
		this.cellStringLength = stringAverageLength;
	}

	public int getNumericalRatio() {
		return numericalRatio;
	}

	public void setNumericalRatio(int numericalRatio) {
		this.numericalRatio = numericalRatio;
	}

	public int getAlphabetRatio() {
		return alphabetRatio;
	}

	public void setAlphabetRatio(int alphabetRatio) {
		this.alphabetRatio = alphabetRatio;
	}

	public int getKanjiRatio() {
		return kanjiRatio;
	}

	public void setKanjiRatio(int kanjiRatio) {
		this.kanjiRatio = kanjiRatio;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public static Logger getLog() {
		return log;
	}

}
