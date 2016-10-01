package net.sf.esfinge.aom.manager.visitors;

import net.sf.esfinge.aom.api.manager.visitors.IEntityVisitor;
import net.sf.esfinge.aom.api.model.HasProperties;
import net.sf.esfinge.aom.api.model.IEntity;
import net.sf.esfinge.aom.api.model.IEntityType;
import net.sf.esfinge.aom.exceptions.EsfingeAOMException;
import net.sf.esfinge.aom.manager.ModelManager;
import net.sf.esfinge.aom.model.factories.EntityFactory;

public class CreateEntityVisitor implements IEntityVisitor {

	private IEntity entity;
	
	private ModelManager manager = ModelManager.getInstance();
	
	@Override
	public void initVisit(Object id, IEntityType entityType)
			throws EsfingeAOMException {
		entity = EntityFactory.createEntity(entityType, (Class<?>) null);
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
