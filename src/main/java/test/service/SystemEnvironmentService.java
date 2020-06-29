package test.service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import org.springframework.stereotype.Service;

import form.SystemEnvironmentForm;
import form.SystemEnvironmentItem;

@Service
public class SystemEnvironmentService {

	public void getSystemEnvironment(SystemEnvironmentForm form) {
		
		ArrayList<SystemEnvironmentItem> systemEnvironmentItemList = new ArrayList<SystemEnvironmentItem>();
		
		// Java System Properties
		Properties properties = System.getProperties();
		for(Object key : properties.keySet()) {
			systemEnvironmentItemList.add(new SystemEnvironmentItem("Java", (String)key, properties.getProperty((String)key)));
		}
				
		// OS System properties
		Map<String, String> env = System.getenv();
		for(Map.Entry<String, String> entry : env.entrySet()) {
			systemEnvironmentItemList.add(new SystemEnvironmentItem("OS", entry.getKey(), entry.getValue()));
		}
      
		form.setSystemEnvironmentItemList(systemEnvironmentItemList);
	}
}
