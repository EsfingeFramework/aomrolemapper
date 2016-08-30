package org.esfinge.aom.manager.visitors;

import org.esfinge.aom.api.manager.visitors.IEntityTypeVisitor;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.manager.ModelManager;
import org.esfinge.aom.model.factories.EntityTypeFactory;
import org.esfinge.aom.model.factories.PropertyTypeFactory;

public class CreateEntityTypeVisitor implements IEntityTypeVisitor {

	private IEntityType entityType;
	
	private ModelManager manager = ModelManager.getInstance();
	
	@Override
	public void initVisit(String packageName, String name) throws EsfingeAOMException {
		initVisit(packageName, name, null);
	}

	@Override
	public void initVisit(String packageName, String name, String associatedClass) throws EsfingeAOMException {		
		try {
			entityType =  EntityTypeFactory.createEntityType(packageName, name, associatedClass);
		} catch (ClassNotFoundException e) {
			throw new EsfingeAOMException("Associated class not found",e);
		}
	}
	
	@Override
	public void visitPropertyType(String propertyName, Object type) throws EsfingeAOMException {
		visitPropertyType(propertyName, type, null);
	}

	@Override
	public void visitPropertyType(String propertyName, Object type, String associatedClass) throws EsfingeAOMException {
		IPropertyType propertyType = entityType.getPropertyType(propertyName);
		if (propertyType == null)
		{
			if (associatedClass == null)
			{
				propertyType = PropertyTypeFactory.createPropertyType(propertyName, type);
			}
			else
			{
				propertyType = PropertyTypeFactory.createPropertyType(propertyName, type, associatedClass);
			}
			entityType.addPropertyType(propertyType);
		}
		else
		{
			propertyType.setType(type);
		}
	}
	
	@Override
	public void visitRelationship(String propertyName, String entityTypeId, String associatedClass) throws EsfingeAOMException {
		IEntityType relatedEntityType = null;
		
		if (manager.equivalentEntityTypes(entityType, entityTypeId))
		{
			relatedEntityType = entityType;
		}
		else
		{
			relatedEntityType = manager.getEntityType(entityTypeId);
		}
		visitPropertyType(propertyName, relatedEntityType, associatedClass);		
	}
	
	@Override
	public void visitRelationship(String propertyName, String entityTypeId) throws EsfingeAOMException {
		visitRelationship(propertyName, entityTypeId, null);
	}


	@Override
	public IEntityType endVisit() {
		return entityType;
	}

}
