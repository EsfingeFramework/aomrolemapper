package org.esfinge.aom.persistence.mongodb;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class MongoAOMConfiguration {

	private List<EntityTypeCollectionMap> entityTypeCollectionMaps = new ArrayList<EntityTypeCollectionMap>();

	private String entityTypeCollectionName;
	
	private String host;
	
	private String database;
	
	public MongoAOMConfiguration() 
	{
		try
		{
			URL configURL = this.getClass().getResource("/Config/MongoAOMConfiguration.xml");

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse (new File(configURL.toURI()));

			XPathFactory xpathFactory = XPathFactory.newInstance();

			XPath xpath = xpathFactory.newXPath();

			XPathExpression expr = xpath.compile("/Configuration/EntityTypeToCollectionMap");

			Object result = expr.evaluate(doc, XPathConstants.NODESET);

			NodeList configs = (NodeList)result;

			for(int i = 0; i < configs.getLength(); i++){
				NamedNodeMap namedNodeMap = configs.item(i).getAttributes();
				String entityTypeRegex = namedNodeMap.getNamedItem("entityType").getNodeValue();
				String collection = namedNodeMap.getNamedItem("collection").getNodeValue();
				Node packageAttribute = namedNodeMap.getNamedItem("package");
				EntityTypeCollectionMap map = null;
				if (packageAttribute != null)
				{
					String packageRegexp = packageAttribute.getNodeValue();
					map = new EntityTypeCollectionMap(entityTypeRegex, packageRegexp, collection);
				}
				else
				{
					map = new EntityTypeCollectionMap(entityTypeRegex, collection);
				}
				entityTypeCollectionMaps.add(map);
			}
			
			expr = xpath.compile("/Configuration/EntityTypeCollectionName");

			result = expr.evaluate(doc, XPathConstants.NODE);

			Node collectionName = (Node)result;
			
			entityTypeCollectionName = collectionName.getTextContent();
			
			expr = xpath.compile("/Configuration/Host");

			result = expr.evaluate(doc, XPathConstants.NODE);

			if (result != null)
			{
				Node hostNode = (Node)result;
				host = hostNode.getTextContent();
			}
			
			expr = xpath.compile("/Configuration/Database");

			result = expr.evaluate(doc, XPathConstants.NODE);

			if (result != null)
			{
				Node databaseNode = (Node)result;
				database = databaseNode.getTextContent();
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public String getCollectionForEntityType (String entityType, String packageName)
	{
		for (EntityTypeCollectionMap map : entityTypeCollectionMaps)
		{
			if (Pattern.matches(map.getEntityTypeRegexp(), entityType))
			{
				if (map.getPackageRegexp() != null)
				{
					if (Pattern.matches(map.getPackageRegexp(), packageName))
					{
						return map.getCollection();
					}
				}
				else
				{
					return map.getCollection();
				}
			}
		}
		
		//TODO what if the type is changed - are we handling the removal and insertion in another collection?
		return null;
	}

	public String getEntityTypeCollectionName() {
		return entityTypeCollectionName;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}
	
}
