package org.esfinge.aom.model.dynamic.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.esfinge.aom.api.model.HasProperties;
import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.dynamic.exceptions.AdapterFactoryClassConstructionException;
import org.esfinge.aom.model.dynamic.exceptions.AdapterFactoryFileReaderException;
import org.esfinge.aom.model.impl.CalculaAnos;
import org.esfinge.aom.model.impl.GenericEntityType;
import org.esfinge.aom.model.impl.GenericPropertyType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.RuleMethod;
import org.junit.Before;
import org.junit.Test;

public class AdapterFactoryTest {
	private AdapterFactory af;
	private String fileName;

	@Before
	public void setUp() throws Exception {
		fileName = "JsonMapTest.json";
		af = AdapterFactory.getInstance(fileName);
	}

	public void createAdapterToEntity() throws EsfingeAOMException, AdapterFactoryFileReaderException,
			AdapterFactoryClassConstructionException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		IEntityType personType = new GenericEntityType("Person");
		personType.addPropertyType(new GenericPropertyType("name", String.class));
		personType.addPropertyType(new GenericPropertyType("age", Integer.class));

		IEntity person = personType.createNewEntity();
		person.setProperty("name", "Alberto Sales");
		person.setProperty("age", 25);

		Object personAdapter = af.generate(person);

		String nameFromAdapter = (String) personAdapter.getClass().getMethod("getName").invoke(personAdapter);
		assertEquals(nameFromAdapter, person.getProperty("name").getValue());

		int ageFromAdapter = (int) personAdapter.getClass().getMethod("getAge").invoke(personAdapter);
		assertEquals(ageFromAdapter, person.getProperty("age").getValue());
	}

	@Test
	public void createAdapterWithComplexProperty() throws EsfingeAOMException, AdapterFactoryFileReaderException,
			AdapterFactoryClassConstructionException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {
		IEntityType personType = new GenericEntityType("Person");
		IEntityType contactType = new GenericEntityType("Contact");

		personType.addPropertyType(new GenericPropertyType("mainContact", contactType));
		contactType.addPropertyType(new GenericPropertyType("phone", String.class));
		contactType.addPropertyType(new GenericPropertyType("type", String.class));

		HasProperties home = contactType.createNewEntity();
		home.setProperty("phone", "1232312312");
		home.setProperty("type", "home");

		IEntity person = personType.createNewEntity();
		person.setProperty("mainContact", home);

		Object personAdapter = af.generate(person);

		Object mainContactAdapter = personAdapter.getClass().getMethod("getMainContact").invoke(personAdapter);

		String phone = (String) mainContactAdapter.getClass().getMethod("getPhone").invoke(mainContactAdapter);
		String type = (String) mainContactAdapter.getClass().getMethod("getType").invoke(mainContactAdapter);

		assertEquals(phone, home.getProperty("phone").getValue());
		assertEquals(type, home.getProperty("type").getValue());
	}

	@Test
	public void createAdapterWithMetadata() throws EsfingeAOMException, AdapterFactoryFileReaderException,
			AdapterFactoryClassConstructionException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {

		IEntityType clientType = new GenericEntityType("Client");

		// coloca as propriedades
		IPropertyType namePropertyType = new GenericPropertyType("name", String.class);
		clientType.addPropertyType(namePropertyType);
		IPropertyType agePropertyType = new GenericPropertyType("age", Integer.class);
		clientType.addPropertyType(agePropertyType);

		// coloca os metadados na entity
		clientType.setProperty("entity", true);

		// coloca os metadados nas properties
		namePropertyType.setProperty("oneToOne", true);

		Map<String, Object> parametersSubstring = new HashMap<String, Object>();
		parametersSubstring.put("name", "nomeCliente");
		parametersSubstring.put("nullable", true);
		namePropertyType.setProperty("column", parametersSubstring);

		// criar a entidade e preenche seus atributos
		IEntity client = clientType.createNewEntity();
		client.setProperty("name", "Guerra");
		client.setProperty("age", 35);
		client.setProperty("weight", 110.55);

		Object personBeanAdapter = af.generate(client);

		boolean entityAnnotationPresence = personBeanAdapter.getClass()
				.isAnnotationPresent(javax.persistence.Entity.class);
		assertTrue(entityAnnotationPresence);

		boolean columnAnnotationPresence = personBeanAdapter.getClass().getMethod("getName")
				.isAnnotationPresent(javax.persistence.Column.class);
		assertTrue(columnAnnotationPresence);

		javax.persistence.Column columnAnnotation = personBeanAdapter.getClass().getMethod("getName")
				.getAnnotation(javax.persistence.Column.class);
		boolean oneToOneColumnAnnotation = (columnAnnotation != null);
		assertTrue(oneToOneColumnAnnotation);

		String columnName = (String) columnAnnotation.getClass().getMethod("name").invoke(columnAnnotation);
		assertEquals(columnName, "nomeCliente");
	}

	@Test
	public void createRuleWithMetadata() throws EsfingeAOMException, AdapterFactoryFileReaderException,
			AdapterFactoryClassConstructionException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {

		IEntityType tipoProduto = new GenericEntityType("Produtos");

		// criando property types
		IPropertyType dataNascPropertyType = new GenericPropertyType("dataFabricacao", Date.class);

		IPropertyType nomePropertyType = new GenericPropertyType("nome", String.class);
		
		tipoProduto.setProperty("ruleClass", true);
		
		nomePropertyType.setProperty("ruleAttribute", true);
				
		GregorianCalendar dataFabr = new GregorianCalendar();
		dataFabr.set(2010, 11, 23);
		Map<String, Object> parametersSubstring = new HashMap<String, Object>();
		parametersSubstring.put("dataFabricacao", "2016");
		parametersSubstring.put("perecivel", Boolean.TRUE);
		dataNascPropertyType.setProperty("ruleObject", parametersSubstring);

		// adicionando property types no tipo de entidade
		tipoProduto.addPropertyType(dataNascPropertyType);
		tipoProduto.addPropertyType(nomePropertyType);
		tipoProduto.addOperation("anosFabricacao", new CalculaAnos("dataFabricacao"));
		tipoProduto.setProperty("ruleObject", true);
	
		IEntity produto = tipoProduto.createNewEntity();
		produto.setProperty("nome", "Notebook DELL");		
		produto.setProperty("dataFabricacao", dataFabr.getTime());

		Object personBeanAdapter = af.generate(produto);
		
		boolean entityAnnotationPresence = personBeanAdapter.getClass()
				.isAnnotationPresent(org.esfinge.aom.model.rolemapper.metadata.annotations.RuleClass.class);
		assertTrue(entityAnnotationPresence);
		
//		boolean entityAnnotationAttribute = personBeanAdapter.getClass()
//				.isAnnotationPresent(org.esfinge.aom.model.rolemapper.metadata.annotations.RuleAttribute.class);
//		assertTrue(entityAnnotationAttribute);
		
		boolean columnAnnotationPresence = personBeanAdapter.getClass().getMethod("getDataFabricacao")
				.isAnnotationPresent(RuleMethod.class);
		assertTrue(columnAnnotationPresence);
	
		RuleMethod ruleMethodAnnot = personBeanAdapter.getClass().getMethod("getDataFabricacao")
				.getAnnotation(RuleMethod.class);
		assertTrue(ruleMethodAnnot != null);	
		
		
	}

}