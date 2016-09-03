package net.sf.esfinge.aom.model.dynamic.factory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.parser.ParseException;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import net.sf.esfinge.aom.api.model.IEntity;
import net.sf.esfinge.aom.api.model.IEntityType;
import net.sf.esfinge.aom.api.model.IProperty;
import net.sf.esfinge.aom.exceptions.EsfingeAOMException;
import net.sf.esfinge.aom.model.dynamic.exceptions.AdapterFactoryClassConstructionException;
import net.sf.esfinge.aom.model.dynamic.exceptions.AdapterFactoryFileReaderException;
import net.sf.esfinge.aom.model.dynamic.utils.ClassConstructor;
import net.sf.esfinge.aom.model.dynamic.utils.DynamicClassLoader;
import net.sf.esfinge.aom.model.dynamic.utils.PropertiesReaderJsonPattern;

public class AdapterFactory {
	private static HashMap<String, Class> storedClasses = new HashMap<String, Class>();
	private static AdapterFactory adapterFactory;
	private final String metadataFileName;
	private final String suffixAdapterClassName = "AOMBeanAdapter";

	public static AdapterFactory getInstance() {
		return adapterFactory;
	}

	public static AdapterFactory getInstance(String annotationMapFileName) {
		if (adapterFactory == null) {
			adapterFactory = new AdapterFactory(annotationMapFileName);
		}
		return adapterFactory;
	}

	private AdapterFactory(String annotationMapFileName) {
		storedClasses.clear();
		metadataFileName = annotationMapFileName;
	}

	public Object generate(IEntity entity) throws EsfingeAOMException,
			AdapterFactoryFileReaderException,
			AdapterFactoryClassConstructionException {
		try {
			if (!existInStoredClasses(entity)) {
				ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
				PropertiesReaderJsonPattern pr = PropertiesReaderJsonPattern
						.getInstance(metadataFileName);
				IEntityType entityType = entity.getEntityType();
				String name = entityType.getName() + suffixAdapterClassName;
				addStoredClass(entity, null);

				ClassConstructor.createClassAndConstructor(name, cw);
				Map<String, Object> annotationClassParameters = new HashMap<String, Object>();
				annotateClass(cw, pr, entityType);

				ClassConstructor.createPrivateAttribute(null, cw);

				for (IProperty p : entity.getProperties()) {
					MethodVisitor mv = null;
					if (!pr.isPropertyTarget(p.getName(), "class")) {
						if (IEntityType.class.isAssignableFrom(p
								.getPropertyType().getType().getClass())) {
							String propertyType = ((IEntity) p.getValue()).getEntityType().getName() + suffixAdapterClassName;
							if(!storedClasses.containsKey(name)){
								//Gera um adapter aqui apenas para criar a classe do adapter da composi��o no ClassPath para o get funcionar
								this.getInstance(metadataFileName).generate((IEntity)p.getValue());								
								mv = ClassConstructor.createComplexPropertyGetter(name, cw, p.getName(), propertyType);
								ClassConstructor.createComplexPropertySetter(name,cw, p.getName(), propertyType);
							}
						} else if (!p.getPropertyType().getType().toString()
								.substring(6).equals("java.lang.Object")) {
							String propertyType = p.getPropertyType().getType()
									.toString().substring(6);
							mv = ClassConstructor
									.createGetterWithSpecificProperty(name, cw,
											p.getName(), propertyType);
							ClassConstructor.createSetterWithSpecificProperty(
									name, cw, p.getName(), propertyType);
						} else {
							mv = ClassConstructor.createGetter(name, cw,
									p.getName());
							ClassConstructor
									.createSetter(name, cw, p.getName());
						}
						
						if(mv !=null) annotateMethod(pr, entityType, p, mv);
					}
				}

				DynamicClassLoader cl = DynamicClassLoader.getInstance(Thread
						.currentThread().getContextClassLoader());
				Class<?> clazz = cl.defineClass(name, cw.toByteArray());
				addStoredClass(entity, clazz);

				return clazz.getConstructor(IEntity.class).newInstance(entity);
			}

			return existInStoredClassesWithoutClazz(entity) ? null : getInstanceFromStoredClasses(entity);
		} catch (IllegalAccessException | IllegalArgumentException
				| InstantiationException | NoSuchMethodException
				| InvocationTargetException ex) {
			throw new AdapterFactoryClassConstructionException(ex.getCause());
		} catch (ParseException | IOException ex) {
			throw new AdapterFactoryFileReaderException(ex.getCause());
		}
	}

	// Verifica propriedades da propertyType, busca propiedades no arquivo, e anota o get
	private void annotateField(PropertiesReaderJsonPattern pr,
			IEntityType entityType, IProperty p, MethodVisitor fv)
			throws EsfingeAOMException {
		Map<String, Object> annotationParameters = new HashMap<String, Object>();
		for (IProperty metadataPropertyType : entityType.getPropertyType(
				p.getName()).getProperties()) {
			String annotationClassPath = pr.readProperty(metadataPropertyType
					.getName());
			if (annotationClassPath != null
					&& annotationClassPath.length() != 0) {
				boolean isTarget = pr.isPropertyTarget(
						metadataPropertyType.getName(), "field");
				String[] metadataParameters = null;

				if (isTarget) {
					metadataParameters = pr
							.readPropertyParameters(metadataPropertyType
									.getName());
				}

				if (metadataParameters != null) {
					for (String metadataParameter : metadataParameters) {
						Map<String, Object> parameters = null;
						try {
							parameters = (Map<String, Object>) metadataPropertyType
									.getValue();
						} catch (ClassCastException e) {
							annotationParameters.put(metadataParameter,
									metadataPropertyType.getValue());
							break;
						}
						annotationParameters.put(metadataParameter,
								parameters.get(metadataParameter));
					}
					ClassConstructor.createAnnotationField(annotationClassPath,
							annotationParameters, fv);
				}
			}
		}
	}

	// Verifica propriedades da propertyType, busca propiedades no arquivo, e anota o get
	private void annotateMethod(PropertiesReaderJsonPattern pr,
			IEntityType entityType, IProperty p, MethodVisitor mv)
			throws EsfingeAOMException {
		Map<String, Object> annotationParameters = new HashMap<String, Object>();
		for (IProperty metadataPropertyType : entityType.getPropertyType(p.getName()).getProperties()) {
			String annotationClassPath = pr.readProperty(metadataPropertyType.getName());
			if (annotationClassPath != null	&& annotationClassPath.length() != 0) {
				String[] metadataParameters = pr.readPropertyParameters(metadataPropertyType.getName());
				if (metadataParameters != null) {
					for (String metadataParameter : metadataParameters) {
						Map<String, Object> parameters = null;
						try {
							parameters = (Map<String, Object>) metadataPropertyType.getValue();
						} catch (ClassCastException e) {
							annotationParameters.put(metadataParameter,	metadataPropertyType.getValue());
							break;
						}
						if(parameters.get(metadataParameter) != null)
							annotationParameters.put(metadataParameter,	parameters.get(metadataParameter));
						
					}
					ClassConstructor.createAnnotationMethod(annotationClassPath, annotationParameters, mv);
				}
			}
		}
	}

	// Verifica propriedades do entityType, busca propriedades no arquivo e anota a classe
	private void annotateClass(ClassWriter cw, PropertiesReaderJsonPattern pr,
			IEntityType entityType) throws EsfingeAOMException {
		Map<String, Object> annotationClassParameters;
		for (IProperty metadataEntity : entityType.getProperties()) {
			String annotationClassPath = pr.readProperty(metadataEntity
					.getName());
			annotationClassParameters = new HashMap<String, Object>();
			if (annotationClassPath != null
					&& annotationClassPath.length() != 0) {
				String[] metadataParameters = pr
						.readPropertyParameters(metadataEntity.getName());
				if (metadataParameters != null) {
					for (String metadataParameter : metadataParameters) {
						Map<String, Object> parameters = null;
						try {
							parameters = (Map<String, Object>) metadataEntity
									.getValue();
						} catch (ClassCastException e) {
							annotationClassParameters.put(metadataParameter,
									metadataEntity.getValue());
							break;
						}
						annotationClassParameters.put(metadataParameter,
								parameters.get(metadataParameter));
					}
				}
				ClassConstructor.createAnnotationClass(annotationClassPath,
						annotationClassParameters, cw);
			}
		}
	}

	private Class addStoredClass(IEntity entity, Class clazz)
			throws EsfingeAOMException {
		return storedClasses.put(entity.getEntityType().getName(), clazz);
	}

	private Object getInstanceFromStoredClasses(IEntity entity)
			throws InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException,
			EsfingeAOMException {
		Class clazz = getStoredClass(entity);
		return clazz.getConstructor(IEntity.class).newInstance(entity);
	}

	private boolean existInStoredClasses(IEntity entity)
			throws EsfingeAOMException {
		return storedClasses.containsKey(entity.getEntityType().getName());
	}
	
	private boolean existInStoredClassesWithoutClazz(IEntity entity)
			throws EsfingeAOMException {
		return storedClasses.get(entity.getEntityType().getName()) == null;
	}

	private Class getStoredClass(IEntity entity) throws EsfingeAOMException {
		Class clazz;
		clazz = storedClasses.get(entity.getEntityType().getName());
		return clazz;
	}

	public Object generate(IEntity entity, boolean forceClassGeneration)
			throws Exception {
		Object obj = null;

		if (forceClassGeneration) {
			storedClasses.remove(entity.getEntityType().getName());
		}
		return generate(entity);
	}
}