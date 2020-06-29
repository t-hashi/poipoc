package form;

import java.util.ArrayList;

public class SystemEnvironmentForm {

	private ArrayList<SystemEnvironmentItem> systemEnvironmentItemList = new ArrayList<SystemEnvironmentItem>();

	public ArrayList<SystemEnvironmentItem> getSystemEnvironmentItemList() {
		return systemEnvironmentItemList;
	}

	public void setSystemEnvironmentItemList(ArrayList<SystemEnvironmentItem> systemEnvironmentItemList) {
		this.systemEnvironmentItemList = systemEnvironmentItemList;
	}
	
	public void debug() {
		for(SystemEnvironmentItem item : systemEnvironmentItemList) {
			System.out.println("category:[" + item.getCategory() + "] variable name:[" + item.getVariableName() + "] value:[" + item.getValue() + "]");
		}
	}
}
