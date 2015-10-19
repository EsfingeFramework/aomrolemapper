package br.inpe.dga.utils;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesReaderPropertiesPattern {

	private String propertiesFileName;
	private Properties properties;
	InputStream input;
	
	public PropertiesReaderPropertiesPattern(String propertiesFileName){
		this.propertiesFileName = propertiesFileName;
		this.properties = new Properties();
		try {
			URL url = PropertiesReaderPropertiesPattern.class.getClassLoader().getResource(propertiesFileName);
			this.input = url.openStream();
			this.properties.load(input);
		} catch (IOException io) {
			throw new ExceptionInInitializerError("Properties file cannot be open!");
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					throw new ExceptionInInitializerError("Error closing input!");
				}
			}
		}
	}
	
	public Map<String, String> readProperties(){	
		return (Map<String, String>) properties.entrySet();
	}
	
	public Map<String, String> readProperty(String propertyName){
		String propertyValue = readPropertyValue(propertyName);
		if(propertyValue != null){
			Map<String, String> map = new HashMap<>();
			map.put(propertyName, propertyValue);
			return map;
		}
		return null;
	}	
	
	public String readPropertyValue(String propertyName){
		return properties.getProperty(propertyName);
	}
}