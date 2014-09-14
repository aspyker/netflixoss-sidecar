package com.ibm.ibmcsf.simplesidecar.dynamicconf;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.netflix.config.DynamicPropertyFactory;

public class PropertyUpdater implements Runnable {
	String fileToReadFrom;
	String fileToWriteTo;
	Properties properties;
	private static final Logger logger = LoggerFactory.getLogger(DynamicPropertiesManager.class);
	
	public PropertyUpdater(String fileToReadFrom, String fileToWriteTo) {
		this.fileToReadFrom = fileToReadFrom;
		this.fileToWriteTo = fileToWriteTo;
		
		properties = new Properties();
		try {
			File f = new File(fileToReadFrom);
			FileInputStream is = new FileInputStream(f);
			properties.load(is);
			
			for (Object key : properties.keySet()) {
				String keyname = (String)key;
				String value = DynamicPropertyFactory.getInstance().getStringProperty(keyname, "").get();
				// clear out any properties that were in the incoming file
				properties.setProperty(keyname, "");
			}
		}
		catch (Exception e) {
			logger.error(e.toString());
		}
	}
	
	@Override
	public void run() {
		try {
			logger.debug("about to update properties");
			for (Object key : properties.keySet()) {
				String keyname = (String)key;
				String value = DynamicPropertyFactory.getInstance().getStringProperty(keyname, "").get();
				logger.debug("about to set property " + keyname + " to " + value);
				properties.setProperty(keyname, value);
			}
			File f = new File(fileToWriteTo);
			FileOutputStream fos = new FileOutputStream(f);
			properties.store(fos, "last written by PropertyUpdater " + new Date());
		}
		catch (Exception e) {
			logger.error(e.toString());
		}
		
	}
}
