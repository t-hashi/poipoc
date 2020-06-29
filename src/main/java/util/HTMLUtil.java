package util;

import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;

import form.ExcelDiffForm;
import form.DiffMacroItem;

public class HTMLUtil {
	
	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	public static String sanitize(String input) {
		if (null == input || "".equals(input)) {
			return input;
		}
		input = input.replaceAll("&", "&amp;");
		input = input.replaceAll("<", "&lt;");
		input = input.replaceAll(">", "&gt;");
		input = input.replaceAll("\"", "&quot;");
		input = input.replaceAll("'", "&#39;");
		return input;
	}

	public static String toSourceCode(String input) {
		input = sanitize(input);
		if(input != null && !input.equals("")) {
			return input.replace("\n", "<br>");
		}
		return Consts.HTML_NULL;
	}
	
	public static String toCellValue(String input) {
		input = sanitize(input);
		if(input != null && !input.equals("")) {
			return input.replace("\n", "<br>");
		}
		return Consts.HTML_NULL;
	}
	
	public static void convertDiffResultToHTMLFormat(ExcelDiffForm form) {
		long start = System.currentTimeMillis();
		ArrayList<DiffMacroItem> diffMacroItemList = new ArrayList<DiffMacroItem>();
		for(DiffMacroItem diffMacroItem : form.getDiffMacroItemList()){
			String[] result = HTMLUtil.convertDiffResultToHTMLFormat(diffMacroItem.getOldMacroValue(), diffMacroItem.getNewMacroValue());
			diffMacroItem.setOldHTMLMacroValue(result[0]);
			diffMacroItem.setNewHTMLMacroValue(result[1]);
			diffMacroItemList.add(diffMacroItem);
        }
		form.setDiffMacroItemList(diffMacroItemList);
		long end = System.currentTimeMillis();
		log.info(new Object(){}.getClass().getEnclosingMethod().getName() + "() in " + (end - start) + " ms");
	}
	
	public static String[] convertDiffResultToHTMLFormat(String oldString, String newString) {
		String out[] = new String[2];
		if(oldString != null && newString != null && !oldString.equals("") && !newString.equals("") && !oldString.equals(newString)) {
			oldString = sanitize(oldString);
			newString = sanitize(newString);
			DiffRowGenerator generator = DiffRowGenerator.create().showInlineDiffs(true).inlineDiffByWord(true).build();
			//.oldTag(f -> "~") <- this is sample. default <span class="editOldInline">"xxx"</span>
			//.newTag(f -> "**") <- this is sample. <span class="editNewInline">"yyy"</span>
					
			List<DiffRow> rows = generator.generateDiffRows(Arrays.asList(oldString.split("\n")), Arrays.asList(newString.split("\n")));
			StringBuffer oldBuffer = new StringBuffer();
			StringBuffer newBuffer = new StringBuffer();
			for (DiffRow row : rows) {
				oldBuffer.append(row.getOldLine() + "<br>");
				newBuffer.append(row.getNewLine() + "<br>");
			}
			out[0] = oldBuffer.toString();
			out[1] = newBuffer.toString();
		} else {
			out[0] = toSourceCode(oldString);
			out[1] = toSourceCode(newString);
		}
		return out;
	}

}

