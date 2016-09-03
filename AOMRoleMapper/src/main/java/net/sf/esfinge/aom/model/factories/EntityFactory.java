package net.sf.esfinge.aom.model.factories;

import net.sf.esfinge.aom.api.model.HasProperties;
import net.sf.esfinge.aom.api.model.IEntity;
import net.sf.esfinge.aom.api.model.IEntityType;
import net.sf.esfinge.aom.exceptions.EsfingeAOMException;
import net.sf.esfinge.aom.model.rolemapper.core.AdapterEntity;

public class EntityFactory {

	public static HasProperties createEntity(IEntityType entityType, Object dsObject) throws EsfingeAOMException
	{
		return AdapterEntity.getAdapter(entityType, dsObject);
	}
		
	public static IEntity createEntity(IEntityType entityType, String associatedClass) throws EsfingeAOMException
	{
		try {
			return createEntity(entityType, Class.forName(associatedClass));
		} catch (ClassNotFoundException e) {
			throw new EsfingeAOMException("Class "+associatedClass+" does not exist",e);
		}
	}
	
	public static IEntity createEntity(IEntityType entityType, Class<?> associatedClass) throws EsfingeAOMException
	{
		if (associatedClass != null)
		{
			IEntity entity = new AdapterEntity(entityType, associatedClass);
			return entity;
		}
		
		return entityType.createNewEntity();
	}
}
