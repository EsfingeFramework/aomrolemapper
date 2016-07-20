package org.esfinge.aom.persistence.couchdb;

import java.util.List;

import org.esfinge.aom.api.manager.visitors.IEntityTypeVisitor;
import org.esfinge.aom.api.manager.visitors.IEntityVisitor;
import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.api.modelretriever.IModelRetriever;
import org.esfinge.aom.exceptions.EsfingeAOMException;

public class CouchAOM implements IModelRetriever {

	public CouchAOM()
	{
		
	}

	@Override
	public void save(IEntity entity) throws EsfingeAOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(IEntity entity) throws EsfingeAOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insert(IEntity entity) throws EsfingeAOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeEntity(Object id, IEntityType entityType) throws EsfingeAOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeEntity(IEntity entity) throws EsfingeAOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(IEntityType entityType) throws EsfingeAOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void insert(IEntityType entityType) throws EsfingeAOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void save(IEntityType entityType) throws EsfingeAOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeEntityType(IEntityType entityType) throws EsfingeAOMException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<String> getAllEntityTypeIds() throws EsfingeAOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Object> getAllEntityIDsForType(IEntityType entityType) throws EsfingeAOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IEntity getEntity(Object id, IEntityType entityType, IEntityVisitor entityVisitor)
			throws EsfingeAOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IEntityType getEntityType(String id, IEntityTypeVisitor entityTypeVisitor) throws EsfingeAOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IEntityType getEntityType(String packageName, String name, IEntityTypeVisitor entityTypeVisitor)
			throws EsfingeAOMException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object generateEntityId() throws EsfingeAOMException {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
