package net.sf.esfinge.aom.persistence.couchdb;

import java.util.List;
import java.util.UUID;

import net.sf.esfinge.aom.api.manager.visitors.IEntityTypeVisitor;
import net.sf.esfinge.aom.api.manager.visitors.IEntityVisitor;
import net.sf.esfinge.aom.api.model.IEntity;
import net.sf.esfinge.aom.api.model.IEntityType;
import net.sf.esfinge.aom.api.modelretriever.IModelRetriever;
import net.sf.esfinge.aom.exceptions.EsfingeAOMException;

public class CouchAOM extends BasePersistence implements IModelRetriever {

	private EntityPersistence entityPersistence;
	private EntityTypePersistence entityTypePersistence;
	

	public CouchAOM() throws EsfingeAOMException {
		PersistenceConfig config = new PersistenceConfig();
		entityPersistence = new EntityPersistence(config);
		entityTypePersistence = new EntityTypePersistence(config);
	}

	@Override
	public void save(IEntity entity) throws EsfingeAOMException {
		entityPersistence.persist(entity, PersistenceType.Save);
	}

	@Override
	public void update(IEntity entity) throws EsfingeAOMException {
		entityPersistence.persist(entity, PersistenceType.Update);
	}

	@Override
	public void insert(IEntity entity) throws EsfingeAOMException {
		entityPersistence.persist(entity, PersistenceType.Insert);
	}

	@Override
	public void removeEntity(Object id, IEntityType entityType) throws EsfingeAOMException {
		entityPersistence.removeById(id.toString());
	}

	@Override
	public void removeEntity(IEntity entity) throws EsfingeAOMException {
		removeEntity(entity.getProperty("id").getValue(), entity.getEntityType());
	}

	@Override
	public void update(IEntityType entityType) throws EsfingeAOMException {
		entityTypePersistence.persist(entityType, PersistenceType.Update);
	}

	@Override
	public void insert(IEntityType entityType) throws EsfingeAOMException {
		entityTypePersistence.persist(entityType, PersistenceType.Insert);
	}

	@Override
	public void save(IEntityType entityType) throws EsfingeAOMException {
		entityTypePersistence.persist(entityType, PersistenceType.Save);
	}

	@Override
	public void removeEntityType(IEntityType entityType) throws EsfingeAOMException {
		String id = getEntityTypeId(entityType);
		entityTypePersistence.removeById(id);
		entityPersistence.removeByEntityType(entityType);
	}

	@Override
	public List<String> getAllEntityTypeIds() throws EsfingeAOMException {
		return entityTypePersistence.listIds();
	}

	@Override
	public List<Object> getAllEntityIDsForType(IEntityType entityType) throws EsfingeAOMException {
		return entityPersistence.listIdsByEntityType(entityType);
	}

	@Override
	public IEntity getEntity(Object id, IEntityType entityType, IEntityVisitor entityVisitor)
			throws EsfingeAOMException {
		
		return entityPersistence.get(id, entityType, entityVisitor);
	}

	@Override
	public IEntityType getEntityType(String id, IEntityTypeVisitor entityTypeVisitor) throws EsfingeAOMException {
		return entityTypePersistence.get(id, entityTypeVisitor);
	}

	@Override
	public IEntityType getEntityType(String packageName, String name, IEntityTypeVisitor entityTypeVisitor)
			throws EsfingeAOMException {
		String id = getEntityTypeId(packageName, name);
		return getEntityType(id, entityTypeVisitor);
	}

	@Override
	public Object generateEntityId() throws EsfingeAOMException {
		return UUID.randomUUID().toString();
	}

}
