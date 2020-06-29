package test.controller;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.URLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FileDownloadController {

	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	private static final String EXTERNAL_FILE_PATH = System.getProperty("java.io.tmpdir");

	@RequestMapping("/download/{fileName:.+}")
	public void download(HttpServletRequest request, HttpServletResponse response, @PathVariable("fileName") String fileName) throws IOException {
		try {
			File file = new File(EXTERNAL_FILE_PATH + fileName);
			if (file.exists()) {

				String mimeType = URLConnection.guessContentTypeFromName(file.getName());
				if (mimeType == null) {
					//unknown mimetype so set the mimetype to application/octet-stream
					mimeType = "application/octet-stream";
				}

				response.setContentType(mimeType);
				response.setHeader("Content-Disposition", String.format("attachment; filename=\"" + file.getName() + "\""));
				response.setContentLength((int) file.length());
				InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
				FileCopyUtils.copy(inputStream, response.getOutputStream());
			}
		} catch(Exception e) {
			log.error(this.getClass().getName(), e);
		} 
	}
}