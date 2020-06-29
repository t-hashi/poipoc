package util;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.macros.VBAMacroReader;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.Units;
import org.apache.poi.util.XMLHelper;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import form.DiffCellItem;
import form.DiffCellItemComparator;
import form.ExcelDiffForm;
import form.DiffMacroItem;
import form.DiffSheetItem;
import test.SheetHandler;

public class ExcelUtil {
	
	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	public String[] createExcel(int sheetNumber, int rawNumber, int columnNumber, int stringAverageLength, int numericalRatio,	int alphabetRatio, int kanjiRatio, boolean hasImageInsertedSheet, String imageFilePath) throws Exception {
		long start = System.currentTimeMillis();
		String[] resultArray = new String[4]; // output file path, raw datasize, ooxml size, compress ratio
		FileOutputStream fos = null;
		XSSFWorkbook book = new XSSFWorkbook();
		ArrayList<String> sheetList = (ArrayList<String>) createSheetList(sheetNumber);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		String targetDirectory = new String();
	    try {
	    	String fileName = Consts.CREATE_EXCEL_FILE_PREIX + StringUtil.getCurrentDateString() + Consts.CREATE_EXCEL_FILE_EXTENSION;
	    	targetDirectory = FileUtil.getFileCreateTempDirectory();
	    	String targetPath = targetDirectory + fileName;
	    	resultArray[0] = fileName;
	    	log.info("targetPath:" + targetPath);
    		fos = new FileOutputStream(targetPath);		    
    		
    		int bytes = 0;
	    	for(String sheetName : sheetList) {
	    		XSSFSheet sheet = book.createSheet(sheetName);
	    		for (int i = 0; i < rawNumber; i++) {
	    			XSSFRow xssfrow = sheet.createRow(i);
	    			for (int j = 0; j < columnNumber; j++) {
	    				XSSFCell xssfcell =  xssfrow.createCell(j);
	    				String cellString = getStringFromRandomSelectedMethod(stringAverageLength, numericalRatio, alphabetRatio, kanjiRatio);
	    				xssfcell.setCellValue(cellString);
	    				bytes = bytes + cellString.getBytes(System.getProperty("file.encoding")).length;
	    			}
	    		}
	    	}
	    	log.info("Total cell data(raw data):" + bytes + " bytes");
	    	resultArray[1] = String.valueOf(bytes);
	    	
	    	if(hasImageInsertedSheet) {
	    		// Insert image file to image sheet.
	    		XSSFSheet sheet = book.createSheet(Consts.EXCEL_IMAGE_SHEET_NAME);
	    		InputStream inputstream = new FileInputStream(imageFilePath);
	    		insertImage(book, sheet, inputstream);
	    	}
	    	
	    	book.write(fos);
    		fos.flush();
    		fos.close();
	    	
    	    book.write(baos);
    	    baos.flush();
    	    int ooxmlSize = baos.toByteArray().length;
    	    log.info("Total ooml data(compressed data):" + ooxmlSize + " bytes");
    	    resultArray[2] = String.valueOf(ooxmlSize);
    	    long compressRatio = (100*ooxmlSize)/bytes;
    	    log.info("compress ratio:" + String.valueOf(compressRatio) + "%");
    	    resultArray[3] = String.valueOf(compressRatio);
		} catch (Exception e) {
			throw e;
		} finally {
		    try {
			    if(fos != null) {
			    	fos.close();
			    }
			    if(baos != null) {
			    	baos.close();
			    }
			    if(book != null) {
			    	book.close();
			    }
			} catch (IOException e) {
				throw e;
			}
		    
		}
		
	    long end = System.currentTimeMillis();
	    log.info(new Object(){}.getClass().getEnclosingMethod().getName() + "() in " + (end - start) + " ms");
	    return resultArray;
	}


	private List<String> createSheetList(int sheetNumber) {
		ArrayList<String> sheets = new ArrayList<String>();
		for(int i = 0; i < sheetNumber; i++ ) {
			sheets.add(Consts.SHEET_PREFIX + i);
		}
		return sheets;
	}
	
	
	private String getStringFromRandomSelectedMethod(int stringAverageLength, int numericalRatio, int alphabetRatio, int kanjiRatio) {
		String seedString = "";
		for(int j = 1; j <= numericalRatio; j++) {
			seedString = seedString + "n";
		}
		for(int j = 1; j <= alphabetRatio; j++) {
			seedString = seedString + "a";
		}
		for(int j = 1; j <= kanjiRatio; j++) {
			seedString = seedString + "k";
		}
		Random rand = new Random();
		int index = rand.nextInt(seedString.length()-1);
		if(seedString.substring(index, index+1).equals("n")) {
			return getRandomNumberString(stringAverageLength);
		} else if(seedString.substring(index, index+1).equals("a")) {
			return getRandomAlphabetString(stringAverageLength);
		} else {
			return getRandomKanjiString(stringAverageLength);
		}
	}
	
	
	private String getRandomNumberString(int stringAverageLength) {
		return getRandomString(Consts.NUMBER_SEED_STRING, stringAverageLength);
	}
	
	
	private String getRandomAlphabetString(int stringAverageLength) {
		return getRandomString(Consts.ALPHABET_SEED_STRING, stringAverageLength);
	}
	
	
	private String getRandomKanjiString(int stringAverageLength) {
		return getRandomString(Consts.KANJI_SEED_STRING, stringAverageLength);
	}
	
	
	private String getRandomString(String seedString, int stringAverageLength) {
		Random rand = new Random();
		String output = "";
		for(int i = 0; i < stringAverageLength; i++) {
			int beginIndex = rand.nextInt(seedString.length()-1);
			output = output + seedString.substring(beginIndex, beginIndex+1);
		}
		return output;
	}
	
	/**
	 * insert image file to Excel workbook
	 * @param book
	 * @param sheet
	 * @param is InputStream for image file
	 * @throws Exception
	 */
	public void insertImage(XSSFWorkbook book, XSSFSheet sheet, InputStream is) throws Exception {
		try {
			byte[] bytes = IOUtils.toByteArray(is);
			is.close();
			int pictureIdx = book.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG);
			Drawing<?> patriarch = sheet.createDrawingPatriarch();
			XSSFClientAnchor anchor = book.getCreationHelper().createClientAnchor();
			anchor.setCol1(1);
			anchor.setRow1(1);
			anchor.setCol2(30);
			anchor.setRow2(30);
			anchor.setDx1(Units.EMU_PER_PIXEL * 10);
			anchor.setDy1(Units.EMU_PER_PIXEL * 10);
			anchor.setDx2(Units.EMU_PER_PIXEL * -10);
			anchor.setDy2(Units.EMU_PER_PIXEL * -10);
			anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);
			patriarch.createPicture(anchor, pictureIdx);
		} catch(Exception e) {
			throw e;
		} finally {
			try {
				if(is != null) {
					is.close();
				}
			}catch(Exception e) {
				throw e;
			}
		}
	}
	

	/**
	 * display EXCELL sheet diff and cell diff by SAX
	 * @param newis
	 * @param oldis
	 * @param newFileName
	 * @param oldFileName
	 * @param form
	 * @throws Exception
	 */
	public void excelDiff(InputStream newis, InputStream oldis, String newFileName, String oldFileName, ExcelDiffForm form) throws Exception {
		long start = System.currentTimeMillis();

		XSSFWorkbook newBook = null;
		XSSFWorkbook oldBook = null;
		
		try {	
			newBook = new XSSFWorkbook(newis);
			oldBook = new XSSFWorkbook(oldis);
			
			int newNumberOfSheet = newBook.getNumberOfSheets();
			String[] newSheets = new String[newNumberOfSheet];
			for(int i = 0; i < newNumberOfSheet; i++) {
				newSheets[i] = newBook.getSheetName(i);
			}
			
			int oldNumberOfSheet = oldBook.getNumberOfSheets();
			String[] oldSheets = new String[oldNumberOfSheet];
			for(int i = 0; i < oldNumberOfSheet; i++) {
				oldSheets[i] = oldBook.getSheetName(i);
			}
			
			ArrayList<String> commonSheetList = new ArrayList<String>();
			ArrayList<String> diffSheetList = new ArrayList<String>();
			
			// diff new old 
			for(int i = 0; i < newNumberOfSheet; i++) {
				newSheets[i] = newBook.getSheetName(i);
				boolean diffFlag = true;
				for(int j = 0; j < oldNumberOfSheet; j++) {
					oldSheets[j] = oldBook.getSheetName(j);
					if(newSheets[i].equals(oldSheets[j])) {
						commonSheetList.add(newSheets[i]);
						diffFlag = false;
					}
				}
				if(diffFlag == true) {
					diffSheetList.add(newSheets[i]);
					form.getDiffSheetItemList().add(new DiffSheetItem(null, oldFileName, newSheets[i], Consts.SHEET_NULL_COMMENT));
				}
			}
			
			// diff old new and merge diffList
			for(int i = 0; i < oldNumberOfSheet; i++) {
				oldSheets[i] = oldBook.getSheetName(i);
				boolean diffFlag = true;
				for(int j = 0; j < newNumberOfSheet; j++) {
					newSheets[j] = newBook.getSheetName(j);
					if(oldSheets[i].equals(newSheets[j])) {
						if(!commonSheetList.contains(oldSheets[i])) {
							commonSheetList.add(oldSheets[i]);
						}
						diffFlag = false;
					}
				}
				if(diffFlag == true) {
					if(!diffSheetList.contains(oldSheets[i])) {
						diffSheetList.add(oldSheets[i]);
						form.getDiffSheetItemList().add(new DiffSheetItem(newFileName, null, oldSheets[i], Consts.SHEET_NULL_COMMENT));
					}
				}
			}
									
			Iterator<String> it = commonSheetList.iterator();
			while (it.hasNext()) {
				String sheetName = it.next();
				XSSFSheet newSheet = newBook.getSheet(sheetName);
				XSSFSheet oldSheet = oldBook.getSheet(sheetName);
				int newFirstRowNum = newSheet.getFirstRowNum();
				int newLastRowNum = newSheet.getLastRowNum();
				int oldFirstRowNum = oldSheet.getFirstRowNum();
				int oldLastRowNum = oldSheet.getLastRowNum();
				int firstRowNum = 0;
				int lastRowNum = 0;
				if(newFirstRowNum <= oldFirstRowNum) {
					firstRowNum = newFirstRowNum;
				} else {
					firstRowNum = oldFirstRowNum;
				}
				if(newLastRowNum <= oldLastRowNum) {
					lastRowNum = oldLastRowNum;
				} else {
					lastRowNum = newLastRowNum;
				}
				
				for(int j = firstRowNum; j <= lastRowNum; j++) {
					XSSFRow newrow = newSheet.getRow(j);
					XSSFRow oldrow = oldSheet.getRow(j);
					if(newrow == null && oldrow !=null) {
						for(int k = oldrow.getFirstCellNum(); k <= oldrow.getLastCellNum(); k++) {
							XSSFCell cell = oldrow.getCell(k);
							if(cell != null) {
								form.getDiffCellItemList().add(new DiffCellItem(newFileName, oldFileName, sheetName, cell.getAddress().formatAsString(), null, getStringValue(cell), Consts.CELL_DIFF_COMMENT));
							}
						}
					} else if(newrow != null && oldrow == null) {
						for(int k = newrow.getFirstCellNum(); k <= newrow.getLastCellNum(); k++) {
							XSSFCell cell = newrow.getCell(k);
							if(cell != null) {
								form.getDiffCellItemList().add(new DiffCellItem(newFileName, oldFileName, sheetName, cell.getAddress().formatAsString(), getStringValue(cell), null, Consts.CELL_DIFF_COMMENT));
							} 
						}
					} else if(newrow == null && oldrow == null) {
						// no diff(new and old are both null)
					} else if(newrow != null && oldrow != null) {
						int firstCellNum = 0;
						int lastCellNum = 0;						
						int newFirstCellNum = newrow.getFirstCellNum();
						int newLastCellNum = newrow.getLastCellNum();
						int oldFirstCellNum = oldrow.getFirstCellNum();
						int oldLastCellNum = oldrow.getLastCellNum();
						if(newFirstCellNum <= oldFirstCellNum) {
							firstCellNum = newFirstCellNum;
						} else {
							firstCellNum = oldFirstCellNum;
						}
						if(newLastCellNum <= oldLastCellNum) {
							lastCellNum = oldLastCellNum;
						} else {
							lastCellNum = newLastCellNum;
						}

						for(int k = firstCellNum; k <= lastCellNum; k++) {
							XSSFCell newcell = newrow.getCell(k);
							XSSFCell oldcell = oldrow.getCell(k);
							if(newcell == null && oldcell !=null) {
								form.getDiffCellItemList().add(new DiffCellItem(newFileName, oldFileName, sheetName, oldcell.getAddress().formatAsString(), null, getStringValue(oldcell), Consts.CELL_DIFF_COMMENT));
							} else if(newcell != null && oldcell == null) {
								form.getDiffCellItemList().add(new DiffCellItem(newFileName, oldFileName, sheetName, newcell.getAddress().formatAsString(), getStringValue(newcell), null, Consts.CELL_DIFF_COMMENT));
							} else if(newcell == null && oldcell == null) {
								// no diff(new and old are both null)
							} else if(newcell != null && oldcell != null) {
								if(!getStringValue(newcell).equals(getStringValue(oldcell))) {
									form.getDiffCellItemList().add(new DiffCellItem(newFileName, oldFileName, sheetName, newcell.getAddress().formatAsString(), getStringValue(newcell), getStringValue(oldcell), Consts.CELL_DIFF_COMMENT));
								}
							}
						}
					}
					
				}
			}
			long end = System.currentTimeMillis();
			log.info(new Object(){}.getClass().getEnclosingMethod().getName() + "() in " + (end - start) + " ms");
		} catch (Exception e) {
			throw e;
		} finally {
			if(newis != null) {
				newis.close();
			}
			if(oldis != null) {
				oldis.close();
			}
			if(newBook != null) {
				newBook.close();
			}
			if(oldBook != null) {
				oldBook.close();
			}
		}
	}
	
	private String getStringValue(XSSFCell cell) {
		return cell.toString();
	}

	
	/**
	 * display EXCEL Macro Source differences.
	 * @param newis
	 * @param oldis
	 * @param newFileName
	 * @param oldFileName
	 * @param form
	 * @throws Exception
	 */
	public void excelMacroDiff(InputStream newis, InputStream oldis, String newFileName, String oldFileName, ExcelDiffForm form) throws Exception {
		long start = System.currentTimeMillis();
		VBAMacroReader newReader = null;
		VBAMacroReader oldReader = null;
		Map<String,String> newMacro = null;
		Map<String,String> oldMacro = null;
		
		try {
			newReader = getVBAMacroReader(newis);
			oldReader = getVBAMacroReader(oldis);
			if(newReader != null) {
				newMacro = newReader.readMacros();	
			}
			if(oldReader != null) {
				oldMacro = oldReader.readMacros();
			}
			ArrayList<DiffMacroItem> diffMacroItemList = form.getDiffMacroItemList();
				
			if (newMacro != null && oldMacro != null) {
				for (String newKey : newMacro.keySet()) {
					String newValue = newMacro.get(newKey);
					if(oldMacro.containsKey(newKey)) {
						String oldValue = oldMacro.get(newKey);
						if(!newValue.equals(oldValue)) {
							DiffMacroItem item = new DiffMacroItem(newFileName, oldFileName, newValue, oldValue, Consts.MACRO_DIFF_COMMENT);
							diffMacroItemList.add(item);
						} else {
							// no diff(same key, same value)
						}
					} else { // key diff
						DiffMacroItem item = new DiffMacroItem(newFileName, oldFileName, newValue, null, Consts.MACRO_DIFF_COMMENT);
						diffMacroItemList.add(item);
					}
				}
				for (String oldKey : oldMacro.keySet()) {
					String oldValue = oldMacro.get(oldKey);
					if(!newMacro.containsKey(oldKey)) {
						DiffMacroItem item = new DiffMacroItem(newFileName, oldFileName, null, oldValue, Consts.MACRO_DIFF_COMMENT);
						diffMacroItemList.add(item);
					}
				}
			} else if(newMacro != null && oldMacro == null) {
				DiffMacroItem item = new DiffMacroItem(newFileName, oldFileName, Consts.MACRO_ABBREVIATION_COMMENT, Consts.NO_MACRO_COMMENT, Consts.MACRO_DIFF_COMMENT);
				diffMacroItemList.add(item);
			} else if(newMacro == null && oldMacro != null) {
				DiffMacroItem item = new DiffMacroItem(newFileName, oldFileName, Consts.NO_MACRO_COMMENT, Consts.MACRO_ABBREVIATION_COMMENT, Consts.MACRO_DIFF_COMMENT);
				diffMacroItemList.add(item);
			} else {
				DiffMacroItem item = new DiffMacroItem(newFileName, oldFileName, Consts.NO_MACRO_COMMENT, Consts.NO_MACRO_COMMENT, Consts.MACRO_DIFF_COMMENT);
				diffMacroItemList.add(item);				
			}
			
			form.setDiffMacroItemList(diffMacroItemList);
			
			long end = System.currentTimeMillis();
			log.info(new Object(){}.getClass().getEnclosingMethod().getName() + "() in " + (end - start) + " ms");
		} catch (Exception e) {
			throw e;
		} finally {
			try {
				if(newis != null) {
					newis.close();
				}
				if(oldis != null) {
					oldis.close();
				}
				if(newReader != null) {
					newReader.close();
				}
				if(oldReader != null) {
					oldReader.close();
				}
			} catch (Exception e) {
				throw e;
			}
		}
		
	}
	
	
	/**
	 * get VBAMacroReader
	 * @param is
	 * @return
	 */
	public VBAMacroReader getVBAMacroReader(InputStream is) {
		VBAMacroReader reader;
		try {
			reader = new VBAMacroReader(is);
			return reader;
		} catch(Exception e) {
			if (e.getMessage().equals(Consts.NO_VBA_MESSAGE)) {
				return null;
            }
		}
		return null;	
	}


	/**
	 * file not empty and XSSF readable check
	 * @param multipartFile
	 * @return
	 * @throws IOException 
	 */
	public boolean isExcel(MultipartFile multipartFile) throws IOException {
		long start = System.currentTimeMillis();
		boolean result = false;
	    try {
	    	if(multipartFile != null && !multipartFile.getOriginalFilename().isEmpty()) {
	    		result = isXSSFBookReadable(multipartFile.getInputStream());
	    	} else {
	    		result = false;
	    	}    
		} catch (Exception e) {
			result = false;
		} finally {
		    try {
		    	multipartFile.getInputStream().close();
			} catch (IOException e) {
				log.error(new Object(){}.getClass().getEnclosingMethod().getName(), e);
				throw e;
			}
		}
	    long end = System.currentTimeMillis();
		log.info(new Object(){}.getClass().getEnclosingMethod().getName() + "() in " + (end - start) + " ms");
		return result;
	}
	
	
	/**
	 * file not empty and XSSF readable check
	 * @param filePath
	 * @return
	 * @throws IOException 
	 */
	public boolean isExcel(String filePath) throws IOException {
		long start = System.currentTimeMillis();
		FileInputStream is = null;
		boolean result = false;
	    try {
	    	if(!StringUtil.isNull(filePath)) {
	    		is= new FileInputStream(filePath);
	    		result = isXSSFBookReadable(is);
	    	} else {
	    		result = false;
	    	}    
		} catch (Exception e) {
			result = false;
		} finally {
		    try {
		    	if(is != null) {
		    		is.close();
		    	}
			} catch (IOException e) {
				log.error(new Object(){}.getClass().getEnclosingMethod().getName(), e);
				throw e;
			}
		}
	    long end = System.currentTimeMillis();
		log.info(new Object(){}.getClass().getEnclosingMethod().getName() + "() in " + (end - start) + " ms");
		return result;
	}
	
	
	/**
	 * Excel sheet diff and cell diff by SAX
	 * @param newis
	 * @param oldis
	 * @param newFileName
	 * @param oldFileName
	 * @param form
	 * @throws Exception
	 */
	public void excelDiffBySAX(InputStream newis, InputStream oldis, String newFileName, String oldFileName, ExcelDiffForm form) throws Exception {
		try {
			long start = System.currentTimeMillis();

			ArrayList<String> commonSheetList = new ArrayList<String>();
			ArrayList<String> diffSheetList = new ArrayList<String>();
			ConcurrentHashMap<String,String> newSheetMap = new ConcurrentHashMap<String,String>(); // key=rIdIndex(OOXML sheet index), value=sheetName
			ConcurrentHashMap<String,String> oldSheetMap = new ConcurrentHashMap<String,String>(); // key=rIdIndex(OOXML sheet index), value=sheetName

			XSSFReader newReader = new XSSFReader(OPCPackage.open(newis));
			XSSFReader oldReader = new XSSFReader(OPCPackage.open(oldis));

			// Get new file's Sheet map by SAX
			Iterator<InputStream> newSheets = newReader.getSheetsData();
			if (newSheets instanceof XSSFReader.SheetIterator) {
				XSSFReader.SheetIterator sheetiterator = (XSSFReader.SheetIterator)newSheets;
				int rIdIndex = 1;
				while (sheetiterator.hasNext()) {
					InputStream is = sheetiterator.next();
					newSheetMap.put(Consts.OOXML_SHEET_INDEX_PREIX + rIdIndex, sheetiterator.getSheetName());
					rIdIndex++;
					diffSheetList.add(sheetiterator.getSheetName()); // get all sheet
					commonSheetList.add(sheetiterator.getSheetName()); // get all sheet
					is.close();
				}
			}

			// Get old file's Sheet map by SAX and get common(diff) sheet list
			Iterator<InputStream> oldSheets = oldReader.getSheetsData();
			if (oldSheets instanceof XSSFReader.SheetIterator) {
				XSSFReader.SheetIterator sheetiterator = (XSSFReader.SheetIterator)oldSheets;
				int rIdIndex = 1;
				while (sheetiterator.hasNext()) {
					InputStream is = sheetiterator.next();
					oldSheetMap.put(Consts.OOXML_SHEET_INDEX_PREIX + rIdIndex, sheetiterator.getSheetName());
					rIdIndex++;

					// get common(diff) sheet list.
					if(diffSheetList.contains(sheetiterator.getSheetName())) {
						diffSheetList.remove(sheetiterator.getSheetName());
					} else {
						diffSheetList.add(sheetiterator.getSheetName());
					}
					if(!commonSheetList.contains(sheetiterator.getSheetName())) {
						commonSheetList.remove(sheetiterator.getSheetName());
					}
					is.close();
				}
			}

			// get deleted sheet
			for(String diffSheet: diffSheetList) {
				commonSheetList.remove(diffSheet);
				for(Map.Entry<String, String> entry : newSheetMap.entrySet()) {
					if(diffSheet.equals(entry.getValue())) {
						// to display deleted sheet name. old file don't have(new file have)
						form.getDiffSheetItemList().add(new DiffSheetItem(null, oldFileName, diffSheet, Consts.SHEET_NULL_COMMENT));
					}
				}
				for(Map.Entry<String, String> entry : oldSheetMap.entrySet()) {
					if(diffSheet.equals(entry.getValue())) {
						// to display deleted sheet name. new file don't have(old file have)
						form.getDiffSheetItemList().add(new DiffSheetItem(newFileName, null, diffSheet, Consts.SHEET_NULL_COMMENT));
					}
				}
			}
			
			long end2 = System.currentTimeMillis();
			log.info(new Object(){}.getClass().getEnclosingMethod().getName() + "() sheet diff in " + (end2 - start) + " ms");


			// Get cell map by SAX parse :new file
			HashMap<String,String> newCellMap = new HashMap<String,String>(15*100*100); // SAX parse OOXML. result map(key=sheetname:cellname value=cellvalue)
			for(Map.Entry<String, String> entry : newSheetMap.entrySet()) {			
				for(String commonSheet: commonSheetList) {
					if(commonSheet.equals(entry.getValue())) {
						String sheetName = entry.getValue();
						XMLReader newParser = fetchSheetParser(newReader.getSharedStringsTable(), newCellMap, sheetName);
						newParser.parse(new InputSource(newReader.getSheet(entry.getKey())));
						newReader.getSheet(entry.getKey()).close();
					} 
				}
			}
			

			// Get cell map by SAX parse :old file
			HashMap<String,String> oldCellMap = new HashMap<String,String>(15*100*100); // SAX parse OOXML. result map(key=sheetname:cellname value=cellvalue)
			for(Map.Entry<String, String> entry : oldSheetMap.entrySet()) {			
				for(String commonSheet: commonSheetList) {
					if(commonSheet.equals(entry.getValue())) {
						String sheetName = entry.getValue();					
						XMLReader oldParser = fetchSheetParser(oldReader.getSharedStringsTable(), oldCellMap, sheetName);
						oldParser.parse(new InputSource(oldReader.getSheet(entry.getKey())));
						oldReader.getSheet(entry.getKey()).close();
					} 
				}
			}
			
			long end3 = System.currentTimeMillis();
			log.info(new Object(){}.getClass().getEnclosingMethod().getName() + "() sax parse in " + (end3 - start) + " ms");


			// diff newcell oldcell.(old search by new key.)
			ArrayList<DiffCellItem> diffCellItemList = new ArrayList<DiffCellItem>();
			for(Map.Entry<String, String> newEntry : newCellMap.entrySet()) {
				String newKey = newEntry.getKey();
				String newValue = newEntry.getValue();
				String[] tempStrings = newKey.split(Consts.SHEET_CELL_SPLIT_STRING); // key=sheetname:cellname
				String sheetName = tempStrings[0];
				String cellName = tempStrings[1];
				String oldValue = oldCellMap.get(newKey);

				if(!oldCellMap.containsKey(newKey)) { // key difference
					diffCellItemList.add(new DiffCellItem(newFileName, oldFileName, sheetName, cellName, newValue, oldValue, Consts.CELL_DIFF_COMMENT));
				} else {
					if(!oldValue.equals(newValue)) { // value difference
						diffCellItemList.add(new DiffCellItem(newFileName, oldFileName, sheetName, cellName, newValue, oldValue, Consts.CELL_DIFF_COMMENT));
					} else {

					}
				}
			}

			// diff newcell oldcell.(new search by old key.)
			for(Map.Entry<String, String> oldEntry : oldCellMap.entrySet()) {
				String oldKey = oldEntry.getKey();
				String oldValue = oldEntry.getValue();
				String[] tempStrings = oldKey.split(Consts.SHEET_CELL_SPLIT_STRING); // key=sheetname:cellname
				String sheetName = tempStrings[0];
				String cellName = tempStrings[1];
				String newValue = newCellMap.get(oldKey);
				if(!newCellMap.containsKey(oldKey)) {  // key difference
					diffCellItemList.add(new DiffCellItem(newFileName, oldFileName, sheetName, cellName, newValue, oldValue, Consts.CELL_DIFF_COMMENT));
				} 
			}

			Collections.sort(diffCellItemList, new DiffCellItemComparator());
			form.setDiffCellItemList(diffCellItemList);
			long end = System.currentTimeMillis();
			log.info(new Object(){}.getClass().getEnclosingMethod().getName() + "() in " + (end - start) + " ms");
		} catch(Exception e) {
			log.error(new Object(){}.getClass().getEnclosingMethod().getName(), e);	
			throw e;
		} finally {
			try {
				if(newis != null) {
					newis.close();
				}
				if(oldis != null) {
					oldis.close();
				}
			} catch(Exception e) {
				log.error(new Object(){}.getClass().getEnclosingMethod().getName(), e);	
				throw e;
			}
		}

	}

	
	/**
	 * get SAX parser
	 * @param sst
	 * @param cellMap
	 * @param sheetName
	 * @return
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public XMLReader fetchSheetParser(SharedStringsTable sst, HashMap<String,String> cellMap, String sheetName) throws SAXException, ParserConfigurationException {
        XMLReader parser = XMLHelper.newXMLReader();
        ContentHandler handler = new SheetHandler(sst, cellMap, sheetName);
        parser.setContentHandler(handler);
        return parser;
    }
	

	/**
	 * XLSX file check(=XSSFReader is not null?) by SAX
	 * @param is
	 * @return
	 * @throws Exception
	 */
    public boolean isXSSFBookReadable(InputStream is) throws Exception {
		boolean result = false;
        try {
        	XSSFReader reader = new XSSFReader(OPCPackage.open(is));
        	if(reader != null) {
        		result = true;
        	}
        } catch(Exception e) {
        	result = false;
		}
        return result;
    }
	

}
