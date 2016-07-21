package org.esfinge.aom.persistence.couchdb;

import java.util.regex.Pattern;

import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.exceptions.EsfingeAOMException;

public abstract class BasePersistence {

	protected static String ID_FIELD_NAME = "_id";
	protected static String PROPERTY_VALUE = "propertyValue";
	protected static String PROPERTY_IS_RELATIONSHIP = "isRelationship";
	protected static String PROPERTY_TYPE_IS_RELATIONSHIP = "isRelationship";
	protected static String PROPERTY_ENTITY_TYPE = "propertyEntityType";
	protected static String PROPERTY_ENTITY_ID = "esfingePropertyEntityId";
	protected static String ENTITY_PROPERTIES = "esfingeEntityProperties";
	protected static String ENTITY_CLASS = "esfingeEntityClass";
	protected static String ENTITY_ENTITY_TYPE_ID = "esfingeEntityEntityTypeID";
	protected static String ENTITY_TYPE_NAME = "esfingeEntityTypeName";
	protected static String ENTITY_TYPE_PACKAGE = "esfingeEntityTypePackage";
	protected static String ENTITY_TYPE_PROPERTY_TYPES = "esfingeEntityTypePropertyTypes";
	protected static String ENTITY_TYPE_CLASS = "esfingeEntityTypeClass";
	protected static String PROPERTY_TYPE_TYPE = "esfingePropertyTypeType";
	protected static String PROPERTY_TYPE_CLASS = "esfingePropertyTypeClass";

	protected enum PersistenceType {
		Save, Update, Insert
	}
	
	protected String getEntityTypeId(IEntityType entityType) throws EsfingeAOMException {
		return getEntityTypeId(entityType.getPackageName(), entityType.getName());
	}

	protected String getEntityTypeId(String packageName, String name) {
		return packageName + "/" + name;
	}
	
	protected Class<?> getClass(String className) throws ClassNotFoundException
	{
		if (Pattern.matches(".*\\..*", className))
		{
			return Class.forName(className);
		}
		if (className.equals("int"))
		{
			return int.class;
		}
		if (className.equals("long"))
		{
			return long.class;
		}
		if (className.equals("double"))
		{
			return double.class;
		}
		if (className.equals("float"))
		{
			return float.class;
		}
		if (className.equals("boolean"))
		{
			return boolean.class;
		}
		if (className.equals("char"))
		{
			return char.class;
		}
		if (className.equals("byte"))
		{
			return byte.class;
		}
		if (className.equals("short"))
		{
			return short.class;
		}
		return null;
	}
}
