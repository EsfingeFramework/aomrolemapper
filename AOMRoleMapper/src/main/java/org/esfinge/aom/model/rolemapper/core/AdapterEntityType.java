package org.esfinge.aom.model.rolemapper.core;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import org.apache.commons.lang3.ClassUtils;
import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.api.model.RuleObject;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.impl.GenericPropertyType;
import org.esfinge.aom.model.impl.MethodRuleAdapter;
import org.esfinge.aom.model.rolemapper.metadata.annotations.RuleMap;
import org.esfinge.aom.model.rolemapper.metadata.annotations.RuleMethod;
import org.esfinge.aom.model.rolemapper.metadata.descriptors.EntityTypeDescriptor;
import org.esfinge.aom.model.rolemapper.metadata.descriptors.FieldDescriptor;
import org.esfinge.aom.model.rolemapper.metadata.descriptors.FixedPropertyDescriptor;
import org.esfinge.aom.model.rolemapper.metadata.repository.EntityTypeMetadataRepository;
import org.esfinge.aom.model.rolemapper.metadata.repository.FixedPropertyMetadataRepository;

public class AdapterEntityType implements IEntityType {

	private Object dsObject;

	private String packageName;

	private EntityTypeDescriptor entityTypeDescriptor;

	private Map<String, AdapterFixedProperty> fixedMetadataPerName = new WeakHashMap<String, AdapterFixedProperty>();

	private static Map<Object, AdapterEntityType> objectMap = new WeakHashMap<Object, AdapterEntityType>();

	private Map<String, IPropertyType> fixedPropertyTypes = new HashMap<String, IPropertyType>();

	private Map<String, RuleObject> operations = new LinkedHashMap<>();
	private Map<String, Object> operationProperties = new LinkedHashMap<>();

	private List<MethodRuleAdapter> fixedRules = new ArrayList<>();

	public AdapterEntityType(String entityTypeClass) throws EsfingeAOMException, ClassNotFoundException {
		this(entityTypeClass, null);
	}

	public AdapterEntityType(Class<?> clazz) throws EsfingeAOMException {
		this(clazz, null);
	}

	private AdapterEntityType(String entityTypeClass, Object dsEntityType)
			throws EsfingeAOMException, ClassNotFoundException {
		this(Class.forName(entityTypeClass), dsEntityType);
	}

	private AdapterEntityType(Class<?> clazz, Object dsEntityType) throws EsfingeAOMException {
		try {

			packageName = clazz.getName();
			EntityTypeDescriptor entityTypeDescriptor = (EntityTypeDescriptor) EntityTypeMetadataRepository
					.getInstance().getMetadata(clazz);
			this.entityTypeDescriptor = entityTypeDescriptor;

			Object dsObj = dsEntityType;

			if (dsObj == null) {
				dsObj = clazz.newInstance();
			}
			dsObject = dsObj;
			objectMap.put(dsObj, this);

			FixedPropertyDescriptor fixedPropertyDescriptor = FixedPropertyMetadataRepository.getInstance()
					.getDescriptor();
			if (fixedPropertyDescriptor != null) {
				List<Field> fixedFields = fixedPropertyDescriptor.getFixedProperties(clazz);
				if (fixedFields != null) {
					for (Field f : fixedFields) {
						AdapterFixedPropertyType fixedPropertyType = new AdapterFixedPropertyType(f);
						fixedPropertyTypes.put(fixedPropertyType.getName(), fixedPropertyType);
					}
				}
			}

			List<FieldDescriptor> fixedMetadataDescriptor = entityTypeDescriptor.getFixedMetadataDescriptor();
			for (FieldDescriptor fixedMetadata : fixedMetadataDescriptor) {
				Class proptype = fixedMetadata.getFieldClass();
				IPropertyType propertyType = new GenericPropertyType(fixedMetadata.getFieldName(), proptype);
				AdapterFixedProperty property = new AdapterFixedProperty(dsObj, propertyType);
				fixedMetadataPerName.put(fixedMetadata.getFieldName(), property);
			}
			addFixedRules();
			addOperations();
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}
	}

	private void addOperations() {
		try {
			Field[] declaredFields = dsObject.getClass().getDeclaredFields();
			for (int i = 0; i < declaredFields.length; i++) {
				Field field = declaredFields[i];
				if (field.isAnnotationPresent(RuleMap.class)) {
					String name = field.getName();
					//Class<?> type = field.getType();
					// Method[] declaredMethods = type.getDeclaredMethods();

					name = name.substring(0, 1).toUpperCase() + name.substring(1);

					Method method1 = dsObject.getClass().getMethod("get" + name);
					Object result = method1.invoke(dsObject);
					if (result != null) {
						@SuppressWarnings("unchecked")
						Map<String, Object> results = (Map<String, Object>) result;
						Set<String> keySet = results.keySet();

						for (String key : keySet) {
							Object object = results.get(key);
							Method[] declaredMethods = object.getClass().getDeclaredMethods();
							for (Method method : declaredMethods) {
								System.out.println("found RuleMethod " + method.getName());
								MethodRuleAdapter methodRuleAdapter = new MethodRuleAdapter(method, object);
								addOperation(key, methodRuleAdapter);
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static AdapterEntityType getAdapter(Object dsEntityType) throws EsfingeAOMException {
		if (dsEntityType != null) {
			if (objectMap.containsKey(dsEntityType))
				return objectMap.get(dsEntityType);
			return new AdapterEntityType(dsEntityType.getClass(), dsEntityType);
		}
		return null;
	}

	@Override
	public List<IPropertyType> getPropertyTypes() throws EsfingeAOMException {
		List<IPropertyType> propertyTypes = new ArrayList<IPropertyType>();
		try {
			if (entityTypeDescriptor.getPropertyTypesDescriptor() != null) {
				Method getPropertyTypesMethod = entityTypeDescriptor.getPropertyTypesDescriptor().getGetFieldMethod();
				Collection<?> dsPropertyTypes = (Collection<?>) getPropertyTypesMethod.invoke(dsObject);
				for (Object dsPropertyType : dsPropertyTypes) {
					propertyTypes.add(AdapterPropertyType.getAdapter(dsPropertyType));
				}
			}

			// Adding the fixed property types
			for (IPropertyType propertyType : fixedPropertyTypes.values())
				propertyTypes.add(propertyType);

			return propertyTypes;
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}
	}

	public List<MethodRuleAdapter> getFixedRules() {
		return fixedRules;
	}

	public void addFixedRules() {
		Method[] declaredMethods = dsObject.getClass().getDeclaredMethods();
		System.out.println("métodos " + dsObject.getClass().getDeclaredMethods().length);
		for (Method method : declaredMethods) {
			if (method.isAnnotationPresent(RuleMethod.class)) {
				System.out.println("found RuleMethod " + method.getName());
				MethodRuleAdapter methodRuleAdapter = new MethodRuleAdapter(method, dsObject);
				addOperation("fixedRule", methodRuleAdapter);
			}
		}
	}

	@Override
	public void removePropertyType(IPropertyType propertyType) throws EsfingeAOMException {
		try {
			if (fixedPropertyTypes.containsValue(propertyType)) {
				fixedPropertyTypes.remove(propertyType);
				return;
			}

			Method removePropertyTypeMethod = entityTypeDescriptor.getPropertyTypesDescriptor()
					.getRemoveElementMethod();

			if (removePropertyTypeMethod != null) {
				removePropertyTypeMethod.invoke(dsObject, propertyType.getAssociatedObject());
			} else {
				Method getPropertyTypesMethod = entityTypeDescriptor.getPropertyTypesDescriptor().getGetFieldMethod();
				Collection dsPropertyTypes = (Collection<?>) getPropertyTypesMethod.invoke(dsObject);
				dsPropertyTypes.remove(propertyType.getAssociatedObject());
			}
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}
	}

	@Override
	public void addPropertyType(IPropertyType propertyType) throws EsfingeAOMException {

		try {
			if (!(propertyType instanceof AdapterPropertyType)) {
				if (entityTypeDescriptor.getPropertyTypesDescriptor() != null) {
					Class<?> propertyTypeClass = entityTypeDescriptor.getPropertyTypesDescriptor().getInnerFieldClass();
					AdapterPropertyType apt = AdapterPropertyType.getAdapter(propertyTypeClass.newInstance());
					apt.setName(propertyType.getName());
					apt.setType(propertyType.getType());
					propertyType = apt;
				}
			}

			if (entityTypeDescriptor.getPropertyTypesDescriptor() != null) {
				Method addPropertyTypeMethod = entityTypeDescriptor.getPropertyTypesDescriptor().getAddElementMethod();

				if (addPropertyTypeMethod != null) {
					addPropertyTypeMethod.invoke(dsObject, propertyType.getAssociatedObject());
				} else {
					Method getPropertyTypesMethod = entityTypeDescriptor.getPropertyTypesDescriptor()
							.getGetFieldMethod();
					Collection dsPropertyTypes = (Collection<?>) getPropertyTypesMethod.invoke(dsObject);
					dsPropertyTypes.add(propertyType.getAssociatedObject());
				}
			}

		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}
	}

	@Override
	public IEntity createNewEntity() throws EsfingeAOMException {

		AdapterEntity entity = null;

		Method createEntityMethod = entityTypeDescriptor.getCreateEntityMethod();

		if (createEntityMethod != null) {
			try {
				Object dsEntity = createEntityMethod.invoke(dsObject);
				entity = AdapterEntity.getAdapter(this, dsEntity);
				for (IPropertyType propertyType : getPropertyTypes()) {
					Object propertyTypeType = propertyType.getType();
					if (!propertyType.isRelationshipProperty()) {
						Class<?> propertyTypeClazz = (Class<?>) propertyTypeType;
						if (ClassUtils.isAssignable(propertyTypeClazz, Boolean.class)) {
							entity.setProperty(propertyType.getName(), false);
						} else if (ClassUtils.isAssignable(propertyTypeClazz, Number.class)) {
							entity.setProperty(propertyType.getName(), 0);
						} else if (ClassUtils.isAssignable(propertyTypeClazz, Character.class)) {
							entity.setProperty(propertyType.getName(), (char) 0);
						} else {
							entity.setProperty(propertyType.getName(), null);
						}
					} else {
						entity.setProperty(propertyType.getName(), null);
					}
				}
			} catch (Exception e) {
				throw new EsfingeAOMException("Could not create new entity", e);
			}
		} else {
			throw new EsfingeAOMException("Could not create new entity because no creation method was found");
		}

		return entity;
	}

	@Override
	public String getName() throws EsfingeAOMException {
		try {
			Method getNameMethod = entityTypeDescriptor.getNameDescriptor().getGetFieldMethod();
			Object dsName = getNameMethod.invoke(dsObject);
			return (String) dsName;
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}
	}

	@Override
	public void setName(String name) throws EsfingeAOMException {
		FieldDescriptor descriptor = entityTypeDescriptor.getNameDescriptor();
		if (descriptor == null) {
			throw new EsfingeAOMException("Metadata \"Name\" not found in entity type");
		}
		try {
			Method setNameMethod = descriptor.getSetFieldMethod();
			setNameMethod.invoke(dsObject, name);
		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}
	}

	@Override
	public Object getAssociatedObject() {
		return dsObject;
	}

	@Override
	public void removePropertyType(String propertyName) throws EsfingeAOMException {

		IPropertyType propertyType = getPropertyType(propertyName);
		if (propertyType != null) {
			removePropertyType(propertyType);
		}
	}

	@Override
	public IPropertyType getPropertyType(String propertyName) throws EsfingeAOMException {

		for (IPropertyType type : getPropertyTypes()) {
			if (type.getName().equals(propertyName)) {
				return type;
			}
		}

		return null;
	}

	@Override
	public String getPackageName() {
		return packageName;
	}

	@Override
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@Override
	public List<IProperty> getProperties() throws EsfingeAOMException {
		List<IProperty> result = new ArrayList<IProperty>();
		try {
			// Metadatas
			if (entityTypeDescriptor.getMetadataDescriptor() != null) {
				Method getMetadadaMethod = entityTypeDescriptor.getMetadataDescriptor().getGetFieldMethod();
				// TODO We consider that the ds class initializes the collection
				// objects properly
				Collection<?> dsProperties = (Collection<?>) getMetadadaMethod.invoke(dsObject);

				for (Object property : dsProperties) {
					IProperty adapterProperty = AdapterProperty.getAdapter(property);
					result.add(adapterProperty);
				}
			}

			// Fixed metadatas
			if (entityTypeDescriptor.getFixedMetadataDescriptor() != null) {
				for (IProperty metadata : fixedMetadataPerName.values()) {
					result.add(metadata);
				}
			}

			// Map properties
			if (entityTypeDescriptor.getMapMetadataDescriptor() != null) {
				Method getMetadataMapMethod = entityTypeDescriptor.getMapMetadataDescriptor().getGetFieldMethod();
				Map dsMapMetadata = (Map<String, Object>) getMetadataMapMethod.invoke(dsObject);
				Iterator iterator = dsMapMetadata.entrySet().iterator();
				while (iterator.hasNext()) {
					Map.Entry pair = (Map.Entry) iterator.next();
					IProperty adapterPropertyMap = AdapterPropertyMap.getAdapter(pair.getKey(),
							dsMapMetadata.get(pair.getKey()));
					result.add(adapterPropertyMap);
				}
			}

		} catch (Exception e) {
			throw new EsfingeAOMException(e);
		}
		return result;
	}

	@Override
	public void setProperty(String propertyName, Object propertyValue) throws EsfingeAOMException {
		if (fixedMetadataPerName.containsKey(propertyName)) {
			fixedMetadataPerName.remove(propertyName);
		}

		IPropertyType propertyType = getPropertyType(propertyName);

		if (propertyType == null) {
			IPropertyType ipropertyType = new GenericPropertyType(propertyName, propertyValue.getClass());
			addPropertyType(ipropertyType);
			propertyType = getPropertyType(propertyName);
			AdapterFixedProperty property = new AdapterFixedProperty(dsObject, propertyType);
			fixedMetadataPerName.put(propertyName, property);
		} // else{
			// AdapterFixedProperty property = new
			// AdapterFixedProperty(dsObject,
			// propertyType);
			// fixedMetadataPerName.put(propertyName, property);
			// }
	}

	@Override
	public void removeProperty(String propertyName) throws EsfingeAOMException {
		fixedMetadataPerName.remove(propertyName);
	}

	@Override
	public IProperty getProperty(String metadataName) throws EsfingeAOMException {
		for (IProperty metadata : getProperties()) {
			if (metadata.getName().equals(metadataName))
				return metadata;
		}
		return null;
	}

	@Override
	public void addOperation(String name, RuleObject rule) {
		operations.put(name, rule);
	}

	@Override
	public RuleObject getOperation(String name) {
		return operations.get(name);
	}

	@Override
	public Map<String, Object> getOperationProperties() {
		return operationProperties;
	}

	@Override
	public Map<String, RuleObject> getAllOperation() {
		return operations;
	}

	@Override
	public Collection<RuleObject> getAllRules() {
		return operations.values();
	}

	@Override
	public void setOperationProperties(Map<String, Object> operationProperties) {
		this.operationProperties = operationProperties;
	}

}