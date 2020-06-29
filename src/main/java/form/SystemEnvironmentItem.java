package form;

public class SystemEnvironmentItem {
	
	private String category;
	private String variableName;
	private String value;
	
	public SystemEnvironmentItem(String category, String variableName, String value) {
		super();
		this.category = category;
		this.variableName = variableName;
		this.value = value;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getVariableName() {
		return variableName;
	}
	public void setVariableName(String variableName) {
		this.variableName = variableName;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	

}
