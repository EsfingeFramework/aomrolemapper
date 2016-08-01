package org.esfinge.aom.persistence.neo4j;

import java.io.File;
import java.io.FileReader;
import java.net.URL;

import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.lightcouch.CouchDbProperties;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
			URL configURL= this.getClass().getResource("/Config/Neo4jAOMConfiguration.json");
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
