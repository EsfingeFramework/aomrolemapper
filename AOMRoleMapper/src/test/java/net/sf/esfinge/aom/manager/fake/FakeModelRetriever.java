package net.sf.esfinge.aom.manager.fake;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.esfinge.aom.api.manager.visitors.IEntityTypeVisitor;
import net.sf.esfinge.aom.api.manager.visitors.IEntityVisitor;
import net.sf.esfinge.aom.api.model.IEntity;
import net.sf.esfinge.aom.api.model.IEntityType;
import net.sf.esfinge.aom.api.model.IProperty;
import net.sf.esfinge.aom.api.model.IPropertyType;
import net.sf.esfinge.aom.api.modelretriever.IModelRetriever;
import net.sf.esfinge.aom.exceptions.EsfingeAOMException;
import net.sf.esfinge.aom.model.impl.GenericEntityType;
import net.sf.esfinge.aom.model.impl.GenericPropertyType;

public class FakeModelRetriever implements IModelRetriever {

	private Map<String, IEntityType> entityTypeMap = new HashMap<String, IEntityType>();
	
	private Map<Object, IEntity> entityMap = new HashMap<Object, IEntity>();
	
	private boolean enteredInGetEntity;
	
	private boolean enteredInGetEntityType;
	
	private boolean savedEntity;
	
	private boolean updatedEntity;
	
	private boolean insertedEntity;
	
	private boolean savedEntityType;
	
	private boolean updatedEntityType;
	
	private boolean insertedEntityType;
	
	private boolean returnCopy;
	
	private int entityId = 1000;
	
	@Override
	public void save(IEntity entity) throws EsfingeAOMException {
		savedEntity = true;
		entityMap.put(entity.getProperty("id").getValue(), entity);
	}

	@Override
	public void update(IEntity entity) throws EsfingeAOMException {
		updatedEntity = true;
		entityMap.put(entity.getProperty("id").getValue(), entity);
	}

	@Override
	public void insert(IEntity entity) throws EsfingeAOMException {
		insertedEntity = true;
		entityMap.put(entity.getProperty("id").getValue(), entity);
	}

	@Override
	public void removeEntity(Object id, IEntityType entityType) {
		entityMap.remove(id);
	}

	@Override
	public void removeEntity(IEntity entity) throws EsfingeAOMException {
		entityMap.remove(entity.getProperty("id").getValue());
	}

	@Override
	public void update(IEntityType entityType) throws EsfingeAOMException {
		updatedEntityType = true;
		entityTypeMap.put(getEntityTypeId(entityType), entityType);
	}

	@Override
	public void insert(IEntityType entityType) throws EsfingeAOMException {
		insertedEntityType = true;
		entityTypeMap.put(getEntityTypeId(entityType), entityType);
	}

	@Override
	public void save(IEntityType entityType) throws EsfingeAOMException {
		savedEntityType = true;
		entityTypeMap.put(getEntityTypeId(entityType), entityType);
	}

	@Override
	public void removeEntityType(IEntityType entityType) throws EsfingeAOMException {
		entityTypeMap.remove(getEntityTypeId(entityType));
	}

	@Override
	public List<Object> getAllEntityIDsForType(IEntityType entityType) throws EsfingeAOMException {
		List<Object> entityIds = new ArrayList<Object>();
		
		for (Object id : entityMap.keySet())
		{
			IEntity entity = entityMap.get(id);
			if (getEntityTypeId(entity.getEntityType()).equals(getEntityTypeId(entityType)))
			{
				entityIds.add(id);
			}
		}
		return entityIds;
	}

	@Override
	public IEntity getEntity(Object id, IEntityType entityType, IEntityVisitor entityTypeVisitor) throws EsfingeAOMException {
		enteredInGetEntity = true;
		if (returnCopy)
		{
			// A copy is returned because real retrievers create instances whenever an object is loaded from the database
			return copy(entityMap.get(id));
		}
		return entityMap.get(id);
	}

	@Override
	public IEntityType getEntityType(String packageName, String name, IEntityTypeVisitor entityTypeVisitor) throws EsfingeAOMException {
		enteredInGetEntityType = true;
		if (returnCopy)
		{
			// A copy is returned because real retrievers create instances whenever an object is loaded from the database
			return copy(entityTypeMap.get(getEntityTypeId(packageName, name)));
		}
		return entityTypeMap.get(getEntityTypeId(packageName, name));
	}

	@Override
	public Object generateEntityId() {
		return entityId++;
	}

	@Override
	public IEntityType getEntityType(String id, IEntityTypeVisitor entityTypeVisitor) throws EsfingeAOMException {
		enteredInGetEntityType = true;
		if (returnCopy)
		{
			// A copy is returned because real retrievers create instances whenever an object is loaded from the database
			return copy(entityTypeMap.get(id));
		}
		return entityTypeMap.get(id);
	}

	@Override
	public List<String> getAllEntityTypeIds()
			throws EsfingeAOMException {
		List<String> entityTypeIds = new ArrayList<String>();
		for (String id : entityTypeMap.keySet())
		{
			entityTypeIds.add(id);
		}
		return entityTypeIds;
	}
	
	public void resetAttributes ()
	{
		entityTypeMap = new HashMap<String, IEntityType>();
		entityMap = new HashMap<Object, IEntity>();
		entityId = 1000;
		savedEntity = false;
		savedEntityType = false;
		updatedEntity = false;
		updatedEntityType = false;
		insertedEntity = false;
		insertedEntityType = false;
		enteredInGetEntity = false;
		enteredInGetEntityType = false;
		returnCopy = false;
	}

	public Map<Object, IEntity> getEntityMap() {
		return entityMap;
	}

	public Map<String, IEntityType> getEntityTypeMap() {
		return entityTypeMap;
	}
	
	private String getEntityTypeId (IEntityType entityType) throws EsfingeAOMException
	{
		return getEntityTypeId(entityType.getPackageName(), entityType.getName());
	}
	
	private String getEntityTypeId (String entityTypePackage, String entityTypeName) throws EsfingeAOMException
	{
		return entityTypePackage + "/" + entityTypeName;
	}
	
	public int getEntityId() {
		return entityId;
	}

	public void setEntityId(int entityId) {
		this.entityId = entityId;
	}

	public boolean isEnteredInGetEntity() {
		return enteredInGetEntity;
	}

	public void resetEnteredInGetEntity() {
		this.enteredInGetEntity = false;
	}

	public boolean isEnteredInGetEntityType() {
		return enteredInGetEntityType;
	}

	public void resetEnteredInGetEntityType() {
		this.enteredInGetEntityType = false;
	}

	public boolean isSavedEntity() {
		return savedEntity;
	}

	public void setSavedEntity(boolean savedEntity) {
		this.savedEntity = savedEntity;
	}

	public boolean isUpdatedEntity() {
		return updatedEntity;
	}

	public void setUpdatedEntity(boolean updatedEntity) {
		this.updatedEntity = updatedEntity;
	}

	public boolean isInsertedEntity() {
		return insertedEntity;
	}

	public void setInsertedEntity(boolean insertedEntity) {
		this.insertedEntity = insertedEntity;
	}

	public boolean isSavedEntityType() {
		return savedEntityType;
	}

	public void setSavedEntityType(boolean savedEntityType) {
		this.savedEntityType = savedEntityType;
	}

	public boolean isUpdatedEntityType() {
		return updatedEntityType;
	}

	public void setUpdatedEntityType(boolean updatedEntityType) {
		this.updatedEntityType = updatedEntityType;
	}

	public boolean isInsertedEntityType() {
		return insertedEntityType;
	}

	public void setInsertedEntityType(boolean insertedEntityType) {
		this.insertedEntityType = insertedEntityType;
	}
	
	private IEntity copy (IEntity entity) throws EsfingeAOMException
	{
		IEntity copyEntity = entity.getEntityType().createNewEntity();
		for (IProperty property : entity.getProperties())
		{
			copyEntity.setProperty(property.getPropertyType().getName(), property.getValue());
		}
		return copyEntity;
	}
	
	private IEntityType copy (IEntityType entityType) throws EsfingeAOMException
	{
		IEntityType copyEntityType = new GenericEntityType(entityType.getPackageName(), entityType.getName());
		for (IPropertyType propertyType : entityType.getPropertyTypes())
		{
			IPropertyType copyPropertyType = new GenericPropertyType(propertyType.getName(), propertyType.getType());
			copyEntityType.addPropertyType(copyPropertyType);
		}
		return copyEntityType;
	}

	public boolean isReturnCopy() {
		return returnCopy;
	}

	public void setReturnCopy(boolean returnCopy) {
		this.returnCopy = returnCopy;
	}
}
