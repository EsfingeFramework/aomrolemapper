package net.sf.esfinge.aom.persistence.couchdb;

import java.io.File;
import java.io.FileReader;
import java.net.URL;

import org.lightcouch.CouchDbProperties;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.sf.esfinge.aom.exceptions.EsfingeAOMException;

public class PersistenceConfig {
	
	private String dbPrefix;
	private boolean createDbIfNotExist;
	private String host;
	private int port;
	private String path;
	private String protocol;
	private String username;
	private String password;
	
	public PersistenceConfig() throws EsfingeAOMException {
		try {
			URL configURL= this.getClass().getResource("/Config/CouchAOMConfiguration.json");
			File configFile = new File(configURL.toURI());
			JsonObject jsonConfig = convertFileToJSON(configFile);
			parseJsonConfig(jsonConfig);
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}
	}
	
	public CouchDbProperties getEntityPersistenceConfig() {
		return getCommonConfig("entity");
	}
	
	public CouchDbProperties getEntityTypePersistenceConfig() {
		return getCommonConfig("entity_type");
	}
	
	private CouchDbProperties getCommonConfig(String dbName) {
		CouchDbProperties config = new CouchDbProperties();
		config.setHost(host)
			.setPort(port)
			.setProtocol(protocol)
			.setDbName(dbPrefix + "-" + dbName)
			.setCreateDbIfNotExist(createDbIfNotExist);
		if (!path.isEmpty())
			config.setPath(path);
		if (!username.isEmpty()) {
			config.setUsername(username)
				.setPassword(password);
		}
		return config;				
	}
	
	private void parseJsonConfig(JsonObject jsonConfig) {
		dbPrefix = jsonConfig.get("dbPrefix").getAsString();
		createDbIfNotExist = jsonConfig.get("createDbIfNotExist").getAsBoolean();
		host = jsonConfig.get("host").getAsString();
		port = jsonConfig.get("port").getAsInt();
		path = jsonConfig.get("path").getAsString();
		protocol = jsonConfig.get("protocol").getAsString();
		username = jsonConfig.get("username").getAsString();
		password = jsonConfig.get("password").getAsString();
	}
	
	private JsonObject convertFileToJSON (File file) throws EsfingeAOMException {

        JsonObject jsonObject = new JsonObject();
        
        try {
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new FileReader(file));
            jsonObject = jsonElement.getAsJsonObject();
        } catch (Exception e){
        	throw new EsfingeAOMException(e);
        }
        
        return jsonObject;
    }
}
