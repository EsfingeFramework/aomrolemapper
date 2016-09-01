package org.esfinge.aom.persistence.neo4j;

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
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class Neo4JAOMConfiguration {

	private List<EntityTypeNode> entityTypeNodes;

	private String entityTypeNodeName;
	
	private String database;
	
	public Neo4JAOMConfiguration() {
		try {
			URL configURL = this.getClass().getResource("/Config/Neo4jAOMConfiguration.xml");

			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse (new File(configURL.toURI()));
			XPath xpath = XPathFactory.newInstance().newXPath();
			
			entityTypeNodes = parseConfigurationNodeSet(doc, xpath);
			
			entityTypeNodeName = parseConfigurationNodeItem(doc, xpath, "/Configuration/EntityTypeNodeName");
			
			database = parseConfigurationNodeItem(doc, xpath, "/Configuration/Database");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private List<EntityTypeNode> parseConfigurationNodeSet(Document doc, XPath xpath) throws XPathExpressionException {
		List<EntityTypeNode> entityTypes = new ArrayList<EntityTypeNode>();
		XPathExpression expr = xpath.compile("/Configuration/EntityTypeToNode");
		Object result = expr.evaluate(doc, XPathConstants.NODESET);
		NodeList configs = (NodeList)result;
		for(int i = 0; i < configs.getLength(); i++){
			NamedNodeMap namedNodeMap = configs.item(i).getAttributes();
			String entityTypeRegex = namedNodeMap.getNamedItem("entityType").getNodeValue();
			String node = namedNodeMap.getNamedItem("node").getNodeValue();
			Node packageAttribute = namedNodeMap.getNamedItem("package");
			EntityTypeNode map = null;
			if (packageAttribute != null) {
				String packageRegexp = packageAttribute.getNodeValue();
				map = new EntityTypeNode(entityTypeRegex, packageRegexp, node);
			} else {
				map = new EntityTypeNode(entityTypeRegex, node);
			}
			entityTypes.add(map);
		}
		return entityTypes;
	}

	private String parseConfigurationNodeItem(Document doc, XPath xpath, String configItem) throws XPathExpressionException {
		XPathExpression expr = xpath.compile(configItem);
		Object result = expr.evaluate(doc, XPathConstants.NODE);
		if (result != null) {
			Node nodeName = (Node)result;
			return nodeName.getTextContent();
		} else {
			return "";
		}
	}

	public String getNodeForEntityType(String entityType, String packageName) {
		for (EntityTypeNode map : entityTypeNodes) {
			if (Pattern.matches(map.getEntityTypeRegexp(), entityType))	{
				if (map.getPackageRegexp() != null)	{
					if (Pattern.matches(map.getPackageRegexp(), packageName)) {
						return map.getNode();
					}
				} else {
					return map.getNode();
				}
			}
		}
		
		//TODO what if the type is changed - are we handling the removal and insertion in another collection?
		return null;
	}

	public String getEntityTypeNodeName() {
		return entityTypeNodeName;
	}

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}
	
}
