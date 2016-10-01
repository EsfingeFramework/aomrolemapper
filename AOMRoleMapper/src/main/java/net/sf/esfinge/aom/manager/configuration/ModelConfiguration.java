package net.sf.esfinge.aom.manager.configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

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

import net.sf.esfinge.aom.api.manager.visitors.IEntityTypeVisitor;
import net.sf.esfinge.aom.api.model.IEntityType;
import net.sf.esfinge.aom.api.model.IPropertyType;
import net.sf.esfinge.aom.exceptions.EsfingeAOMException;
import net.sf.esfinge.aom.manager.visitors.CreateEntityTypeVisitor;
import net.sf.esfinge.aom.model.factories.PropertyTypeFactory;

public class ModelConfiguration {

	private List<IEntityType> model;
	
	public ModelConfiguration(String configurationPath) 
	{
		try
		{
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse (new File(configurationPath));

			XPathFactory xpathFactory = XPathFactory.newInstance();

			XPath xpath = xpathFactory.newXPath();

			XPathExpression expr = xpath.compile("/Model/Data/EntityType");

			Object result = expr.evaluate(doc, XPathConstants.NODESET);

			NodeList entityTypes = (NodeList)result;
			
			model = loadEntityTypes(entityTypes);

			expr = xpath.compile("/Model/Data/Relationship/EntityType");

			result = expr.evaluate(doc, XPathConstants.NODESET);

			NodeList relationships = (NodeList)result;
			
			addRelationships(model, relationships);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void addRelationships (List<IEntityType> entityTypes, NodeList relationshipNodes) throws EsfingeAOMException
	{
		Map<String, IEntityType> auxiliaryEntityTypeMap = getAuxiliaryMapForEntityTypes(entityTypes);		
		
		for (int i = 0; i < relationshipNodes.getLength(); i++)
		{
			Node entityTypeConfig = relationshipNodes.item(i);
			NamedNodeMap attributes = entityTypeConfig.getAttributes();			
			String entityTypeName = attributes.getNamedItem("name").getNodeValue();
			String packageName = attributes.getNamedItem("package").getNodeValue();
			IEntityType entityType = auxiliaryEntityTypeMap.get(packageName + entityTypeName);
			
			NodeList propertyTypeConfigs = entityTypeConfig.getChildNodes();
			for (int j = 0; j < propertyTypeConfigs.getLength(); j++)
			{
				Node propertyTypeConfig = propertyTypeConfigs.item(j);
				if (propertyTypeConfig.getNodeName().equals("PropertyType"))
				{
					NamedNodeMap propertyTypeAttributes = propertyTypeConfig.getAttributes();
					String propertyTypeName = propertyTypeAttributes.getNamedItem("name").getNodeValue();
					String propertyTypeTypeName = propertyTypeAttributes.getNamedItem("type").getNodeValue();
					String propertyTypeTypePackage = propertyTypeAttributes.getNamedItem("package").getNodeValue();				
					Node propertyTypeClass = propertyTypeAttributes.getNamedItem("adaptedClass");
					IPropertyType propertyType = null;
					IEntityType relatedEntityType = auxiliaryEntityTypeMap.get(propertyTypeTypePackage + propertyTypeTypeName);
					if (propertyTypeClass != null)
					{
						String adaptedClass = propertyTypeClass.getNodeValue();
						propertyType = PropertyTypeFactory.createPropertyType(propertyTypeName, relatedEntityType, adaptedClass);
					}
					else
					{
						propertyType = PropertyTypeFactory.createPropertyType(propertyTypeName, relatedEntityType);
					}
					entityType.addPropertyType(propertyType);
				}
			}			
		}
	}
	
	private Map<String, IEntityType> getAuxiliaryMapForEntityTypes (List<IEntityType> entityTypes) throws EsfingeAOMException
	{
		Map<String, IEntityType> auxiliaryEntityTypeMap = new WeakHashMap<String, IEntityType>();
		
		for (IEntityType entityType : entityTypes)
		{
			auxiliaryEntityTypeMap.put(entityType.getPackageName() + entityType.getName(), entityType);
		}
		return auxiliaryEntityTypeMap;
	}
	
	private List<IEntityType> loadEntityTypes (NodeList entityTypeConfigurations) throws EsfingeAOMException
	{
		List<IEntityType> entityTypes = new ArrayList<IEntityType>();
		IEntityTypeVisitor entityTypeVisitor = new CreateEntityTypeVisitor();
		
		for (int i = 0; i < entityTypeConfigurations.getLength(); i++)
		{
			Node entityTypeConfig = entityTypeConfigurations.item(i);
			NamedNodeMap attributes = entityTypeConfig.getAttributes();			
			String entityTypeName = attributes.getNamedItem("name").getNodeValue();
			String packageName = attributes.getNamedItem("package").getNodeValue();
			Node adaptedClassNode = attributes.getNamedItem("adaptedClass");
			if (adaptedClassNode != null)
			{
				String adaptedClass = adaptedClassNode.getNodeValue();
				entityTypeVisitor.initVisit(packageName, entityTypeName, adaptedClass);
			}
			else
			{
				entityTypeVisitor.initVisit(packageName, entityTypeName);
			}
			
			NodeList propertyTypeConfigs = entityTypeConfig.getChildNodes();
			for (int j = 0; j < propertyTypeConfigs.getLength(); j++)
			{
				Node propertyTypeConfig = propertyTypeConfigs.item(j);
				loadPropertyType(entityTypeVisitor, propertyTypeConfig);
			}
			
			entityTypes.add(entityTypeVisitor.endVisit());
		}
		
		return entityTypes;
	}

	private void loadPropertyType(IEntityTypeVisitor entityTypeVisitor,
			Node propertyTypeConfig) throws EsfingeAOMException {
		
		try
		{
			if (propertyTypeConfig.getNodeName().equals("PropertyType"))
			{
				NamedNodeMap propertyTypeAttributes = propertyTypeConfig.getAttributes();
				String propertyTypeName = propertyTypeAttributes.getNamedItem("name").getNodeValue();
				String propertyTypeTypeName = propertyTypeAttributes.getNamedItem("type").getNodeValue();
				Class<?> propertyTypeType = Class.forName(propertyTypeTypeName);
				Node propertyTypeClass = propertyTypeAttributes.getNamedItem("adaptedClass");
				if (propertyTypeClass != null)
				{
					String adaptedClass = propertyTypeClass.getNodeValue();
					entityTypeVisitor.visitPropertyType(propertyTypeName, propertyTypeType, adaptedClass);
				}
				else
				{
					entityTypeVisitor.visitPropertyType(propertyTypeName, propertyTypeType);
				}
			}
		}
		catch (ClassNotFoundException e)
		{
			throw new EsfingeAOMException(e);
		}
	}

	public List<IEntityType> getModel() {
		return model;
	}
}
