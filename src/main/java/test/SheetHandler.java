package test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import util.Consts;

/**
 * See org.xml.sax.helpers.DefaultHandler javadocs
 */
public class SheetHandler extends DefaultHandler {
	private final SharedStringsTable sst;
    private String lastContents;
    private boolean nextIsString;
    private boolean inlineStr;
    private String sheetName;
    private String lineData;
    private HashMap<String,String> cellMap;
     	
    private final LruCache<Integer,String> lruCache = new LruCache<>(50);

    private static class LruCache<A,B> extends LinkedHashMap<A, B> {
        private final int maxEntries;

        public LruCache(final int maxEntries) {
            super(maxEntries + 1, 1.0f, true);
            this.maxEntries = maxEntries;
        }

        @Override
        protected boolean removeEldestEntry(final Map.Entry<A, B> eldest) {
            return super.size() > maxEntries;
        }
    }

    public SheetHandler(SharedStringsTable sst, HashMap<String,String> cellMap, String sheetName) {
        this.sst = sst;
        this.sheetName = sheetName;
       	this.cellMap = cellMap;
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        // c => cell
        if(name.equals("c")) {
            //System.out.print(this.sheetName + Consts.SHEET_CELL_SPLIT_STRING + attributes.getValue("r") + ":");
            lineData = this.sheetName + Consts.SHEET_CELL_SPLIT_STRING + attributes.getValue("r");
            String cellType = attributes.getValue("t");
            nextIsString = cellType != null && cellType.equals("s");
            inlineStr = cellType != null && cellType.equals("inlineStr");
        }
        lastContents = "";
    }

    @Override
    public void endElement(String uri, String localName, String name) throws SAXException {
        if(nextIsString) {
            Integer idx = Integer.valueOf(lastContents);
            lastContents = lruCache.get(idx);
            if (lastContents == null && !lruCache.containsKey(idx)) {
                //lastContents = new XSSFRichTextString(sst.getEntryAt(idx)).toString();
            	lastContents = sst.getItemAt(idx).toString();
                lruCache.put(idx, lastContents);
            }
            nextIsString = false;
        }

        // v => contents of a cell
        // Output after we've seen the string contents
        if(name.equals("v") || (inlineStr && name.equals("c"))) {
            //System.out.println(lastContents);
            cellMap.put(lineData, lastContents);
        }
        lineData = null;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        lastContents += new String(ch, start, length);
    }
    
    public String getSheetName() {
    	return this.sheetName;
    }
    
    public void setSheetName(String sheetName) {
    	this.sheetName = sheetName;
    }
}