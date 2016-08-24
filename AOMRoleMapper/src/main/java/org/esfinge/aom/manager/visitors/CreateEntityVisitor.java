package org.esfinge.aom.manager.visitors;

import org.esfinge.aom.api.manager.visitors.IEntityVisitor;
import org.esfinge.aom.api.model.HasProperties;
import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.manager.ModelManager;
import org.esfinge.aom.model.factories.EntityFactory;

public class CreateEntityVisitor implements IEntityVisitor {

	private IEntity entity;
	
	private ModelManager manager = ModelManager.getInstance();
	
	@Override
	public void initVisit(Object id, IEntityType entityType)
			throws EsfingeAOMException {
		initVisit(id, entityType, null);
	}

	@Override
	public void initVisit(Object id, IEntityType entityType, String associatedClass)
			throws EsfingeAOMException {
		entity = EntityFactory.createEntity(entityType, associatedClass);
	}
	
	@Override
	public void visitProperty(String propertyName, Object propertyValue)
			throws EsfingeAOMException {
		entity.setProperty(propertyName, propertyValue);		
	}

	@Override
	public IEntity endVisit() throws EsfingeAOMException {
		return entity;
	}

	@Override
	public void visitRelationship(String propertyName, String entityTypeId, String entityId) throws EsfingeAOMException {
		IEntityType entityType = manager.getEntityType(entityTypeId);
		HasProperties relatedEntity = manager.getEntity(entityType, entityId);
		entity.setProperty(propertyName, relatedEntity);
	}

}
