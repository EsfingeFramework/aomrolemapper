package org.esfinge.aom.model.dynamic.utils;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class PropertiesReaderJsonPattern {
	
	JSONObject jsonObject;
	private static PropertiesReaderJsonPattern propertiesReader;
	
	private PropertiesReaderJsonPattern(String propertiesFileName) throws ParseException, IOException{
		JSONParser parser = new JSONParser();
			URL url = getClass().getClassLoader().getResource(propertiesFileName);
			File file = new File(url.getPath());
			jsonObject = (JSONObject) parser.parse(new FileReader(file));	
	}
	
	public static PropertiesReaderJsonPattern getInstance(String propertiesFileName) throws ParseException, IOException{
		if(propertiesReader == null){
			propertiesReader = new PropertiesReaderJsonPattern(propertiesFileName);
		}		
		return propertiesReader;
	}
	
	public boolean isPropertyTarget(String propertyName, String target){
		JSONArray array = (JSONArray) jsonObject.get(propertyName);
		if(array != null){
			String result = searchInArrayNode(array, "target");
			if(result != null && result.equals(target)){
				return true;
			}
		}
		return false;
	}	
	
	public String readProperty(String propertyName){
		JSONArray array = (JSONArray) jsonObject.get(propertyName);
		if(array != null)
			return searchInArrayNode(array,"annotationPath");
		return null;
	}	
	
	public String[] readPropertyParameters(String propertyName){
		JSONArray array = (JSONArray) jsonObject.get(propertyName);		
		if(array != null && array.size() > 1){
			List<String> result = new ArrayList<String>();
			for(int i = 1; i < array.size(); i++){
				String foundedElement = searchInArrayNode(array,"parameter_" + i);
				if(foundedElement != null){
					result.add(foundedElement);
				}				
			}
				
			return result.toArray(new String[result.size()]);
		}
		return null;
	}
	
	private String searchInArrayNode(JSONArray array, String toSearch){
		String result = null;
		for(Object object : array){
			JSONObject  jsonObject= (JSONObject)object;
			result = (String) jsonObject.get(toSearch);
			if(result != null){
				return result;
			}
		}
		return result;
	}
}