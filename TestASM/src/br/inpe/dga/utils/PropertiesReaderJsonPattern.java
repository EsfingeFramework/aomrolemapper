package br.inpe.dga.utils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PropertiesReaderJsonPattern {
	
	JSONObject jsonObject;
	private static PropertiesReaderJsonPattern propertiesReader;
	
	private PropertiesReaderJsonPattern(String propertiesFileName){
		JSONParser parser = new JSONParser();
		try {
			URL url = PropertiesReaderJsonPattern.class.getClassLoader().getResource(propertiesFileName);
			System.out.println(url);
			File file = new File(url.getPath());
			jsonObject = (JSONObject) parser.parse(new FileReader(file));
		} catch (ParseException | IOException e1) {
			e1.printStackTrace();
		} 
	}
	
	public static PropertiesReaderJsonPattern getInstance(String propertiesFileName){
		if(propertiesReader == null){
			propertiesReader = new PropertiesReaderJsonPattern(propertiesFileName);
		}		
		return propertiesReader;
	}
	
	public String readProperty(String propertyName){
		JSONArray array = (JSONArray) jsonObject.get(propertyName);
		if(array != null)
			return (String)((JSONObject)array.get(0)).get("annotationPath");
		return null;
	}	
	
	public String[] readPropertyParameters(String propertyName){
		JSONArray array = (JSONArray) jsonObject.get(propertyName);		
		if(array != null && array.size() > 1){
			String[] result = new String[array.size()-1];
			for(int i = 1; i < array.size(); i++)
				result[i-1] = (String)((JSONObject)array.get(i)).get("parameter_" + i);
			return result;
		}
		return null;
	}
}