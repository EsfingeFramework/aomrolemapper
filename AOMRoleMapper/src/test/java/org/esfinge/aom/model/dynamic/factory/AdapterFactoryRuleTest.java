package org.esfinge.aom.model.dynamic.factory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import org.esfinge.aom.model.impl.PeriodoConsumo;
import org.esfinge.aom.model.rolemapper.metadata.annotations.RuleAttribute;
import org.esfinge.aom.model.rolemapper.metadata.annotations.RuleMethod;
import org.junit.Before;
import org.junit.Test;

public class AdapterFactoryRuleTest {
	private AdapterFactory af;
	private String fileName;

	@Before
	public void setUp() throws Exception {
		fileName = "JsonMapTest.json";
		af = AdapterFactory.getInstance(fileName);
	}

	@Test
	public void createRuleWithMetadata() throws EsfingeAOMException, AdapterFactoryFileReaderException,
			AdapterFactoryClassConstructionException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {

		IEntityType tipoProduto = new GenericEntityType("ProdutoNovo1");

		// criando property types
		IPropertyType dataNascPropertyType = new GenericPropertyType("dataFabricacao", Date.class);
		IPropertyType nomePropertyType = new GenericPropertyType("nome", String.class);

		// adicionando property types no tipo de entidade
		tipoProduto.addPropertyType(dataNascPropertyType);
		tipoProduto.addPropertyType(nomePropertyType);
		tipoProduto.addOperation("anosFabricacao", new CalculaAnos("dataFabricacao"));

		// Essas anotações devem ser adicionadas na rule
		tipoProduto.setProperty("ruleClass", true);
		nomePropertyType.setProperty("ruleObject", true);

		GregorianCalendar dataFabr = new GregorianCalendar();
		dataFabr.set(2010, 11, 23);
		Map<String, Object> parametersSubstring = new HashMap<String, Object>();
		parametersSubstring.put("dataFabricacao", "2016");
		parametersSubstring.put("perecivel", true);
		nomePropertyType.setProperty("ruleAttribute", parametersSubstring);

		IEntity produto = tipoProduto.createNewEntity();
		produto.setProperty("nome", "Notebook DELL");
		produto.setProperty("dataFabricacao", dataFabr.getTime());

		Object personBeanAdapter = af.generate(produto);

		boolean entityAnnotationPresence = personBeanAdapter.getClass()
				.isAnnotationPresent(org.esfinge.aom.model.rolemapper.metadata.annotations.RuleClass.class);
		assertTrue(entityAnnotationPresence);

		boolean entityAnnotationAttribute = personBeanAdapter.getClass().getMethod("getNome")
				.isAnnotationPresent(org.esfinge.aom.model.rolemapper.metadata.annotations.RuleAttribute.class);
		assertTrue(entityAnnotationAttribute);

		boolean columnAnnotationPresence = personBeanAdapter.getClass().getMethod("getNome")
				.isAnnotationPresent(RuleMethod.class);
		assertTrue(columnAnnotationPresence);

		RuleAttribute ruleAttribute = personBeanAdapter.getClass().getMethod("getNome")
				.getAnnotation(RuleAttribute.class);
		assertTrue(ruleAttribute != null);

		String columnName = (String) ruleAttribute.getClass().getMethod("dataFabricacao").invoke(ruleAttribute);
		assertEquals(columnName, "2016");

	}

	@Test
	public void createRuleWithMetadata2() throws EsfingeAOMException, AdapterFactoryFileReaderException,
			AdapterFactoryClassConstructionException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {

		IEntityType tipoProduto = new GenericEntityType("ProdutoNovo2");

		// criando property types
		IPropertyType dataNascPropertyType = new GenericPropertyType("dataFabricacao", Date.class);
		IPropertyType nomePropertyType = new GenericPropertyType("anosFabricacao", String.class);

		// adicionando property types no tipo de entidade
		tipoProduto.addPropertyType(dataNascPropertyType);
		tipoProduto.addPropertyType(nomePropertyType);
		tipoProduto.addOperation("anosFabricacao", new CalculaAnos("dataFabricacao"));

		// Essas anotações devem ser adicionadas na rule
		tipoProduto.setProperty("ruleClass", true);
		nomePropertyType.setProperty("ruleObject", true);

		GregorianCalendar dataFabr = new GregorianCalendar();
		dataFabr.set(2010, 11, 23);
		Map<String, Object> parametersSubstring = new HashMap<String, Object>();
		parametersSubstring.put("dataFabricacao", "2016");
		parametersSubstring.put("perecivel", true);
		nomePropertyType.setProperty("ruleAttribute", parametersSubstring);

		IEntity produto = tipoProduto.createNewEntity();
		produto.setProperty("nome", "Notebook DELL");
		produto.setProperty("dataFabricacao", dataFabr.getTime());

		Object personBeanAdapter = af.generate(produto);

		boolean entityAnnotationPresence = personBeanAdapter.getClass()
				.isAnnotationPresent(org.esfinge.aom.model.rolemapper.metadata.annotations.RuleClass.class);
		assertTrue(entityAnnotationPresence);

		boolean entityAnnotationAttribute = personBeanAdapter.getClass().getMethod("getAnosFabricacao")
				.isAnnotationPresent(org.esfinge.aom.model.rolemapper.metadata.annotations.RuleAttribute.class);
		assertTrue(entityAnnotationAttribute);

		boolean columnAnnotationPresence = personBeanAdapter.getClass().getMethod("getAnosFabricacao")
				.isAnnotationPresent(RuleMethod.class);
		assertTrue(columnAnnotationPresence);

		RuleAttribute ruleAttribute = personBeanAdapter.getClass().getMethod("getAnosFabricacao")
				.getAnnotation(RuleAttribute.class);
		assertTrue(ruleAttribute != null);

		String columnName = (String) ruleAttribute.getClass().getMethod("dataFabricacao").invoke(ruleAttribute);
		assertEquals(columnName, "2016");

		Method m = personBeanAdapter.getClass().getMethod("getAnosFabricacao");
		assertNotNull(m);
		assertTrue(m.isAnnotationPresent(RuleMethod.class));
		assertEquals("2016", m.getAnnotation(RuleAttribute.class).dataFabricacao());
		assertEquals(true, m.getAnnotation(RuleAttribute.class).perecivel());

	}

	@Test
	public void createAdapterToEntity() throws EsfingeAOMException, AdapterFactoryFileReaderException,
			AdapterFactoryClassConstructionException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {

		try {

			IEntityType tipoProduto = new GenericEntityType("Produto77");

			GenericPropertyType dataNascPropertyType = new GenericPropertyType("dataFabricacao", Date.class);
			dataNascPropertyType.setProperty("notempty", true);

			GenericPropertyType nomePropertyType = new GenericPropertyType("nome", String.class);
			nomePropertyType.setProperty("notempty", true);

			GenericPropertyType perecivel = new GenericPropertyType("perecivel", Boolean.class);
			perecivel.setProperty("notempty", true);

			GenericPropertyType validade = new GenericPropertyType("validade", Integer.class);
			validade.setProperty("range.min", 1);
			validade.setProperty("range.min", 730);

			// adicionando property types no tipo de entidade
			tipoProduto.addPropertyType(dataNascPropertyType);
			tipoProduto.addPropertyType(nomePropertyType);
			tipoProduto.addPropertyType(perecivel);
			tipoProduto.addPropertyType(validade);
			tipoProduto.addOperation("periodoConsumo", new PeriodoConsumo("dataFabricacao"));

			IEntity produto = tipoProduto.createNewEntity();
			produto.setProperty("nome", "Yogurt X");
			GregorianCalendar dataFabr = new GregorianCalendar();
			dataFabr.set(2016, 11, 23);
			produto.setProperty("dataFabricacao", dataFabr.getTime());
			produto.setProperty("perecivel", Boolean.TRUE);
			produto.setProperty("validade", 90);

			Object personAdapter = af.generate(produto);
			Method declaredMethod = personAdapter.getClass().getDeclaredMethod("executeOperation", String.class,
					Object[].class);
			Object resultOperation = declaredMethod.invoke(personAdapter, "periodoConsumo", null);

			String days = (String) resultOperation;
			Integer value = (Integer) produto.getProperty("validade").getValue();
			System.out.println(" periodo consumo " + days + " validade " + value);
			assertTrue("@@@ Regra executeOperation executada com sucesso ", days != null);
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	@Test
	public void testDataFabricacao() {
		try {
			IEntityType tipoProduto = new GenericEntityType("Produto");

			// criando property types
			GenericPropertyType dataNascPropertyType = new GenericPropertyType("dataFabricacao", Date.class);
			dataNascPropertyType.setProperty("notempty", true);

			GenericPropertyType nomePropertyType = new GenericPropertyType("nome", String.class);
			nomePropertyType.setProperty("notempty", true);

			GenericPropertyType raio = new GenericPropertyType("raio", Double.class);

			// adicionando property types no tipo de entidade
			tipoProduto.addPropertyType(dataNascPropertyType);
			tipoProduto.addPropertyType(nomePropertyType);
			tipoProduto.addPropertyType(raio);

			tipoProduto.addOperation("anosFabricacao", new CalculaAnos("dataFabricacao"));

			IEntity produto = tipoProduto.createNewEntity();
			produto.setProperty("nome", "Notebook DELL");
			GregorianCalendar dataFabr = new GregorianCalendar();
			dataFabr.set(2010, 11, 23);
			produto.setProperty("dataFabricacao", dataFabr.getTime());
			produto.setProperty("raio", 15);

			Object personAdapter = af.generate(produto);
			Method declaredMethod = personAdapter.getClass().getDeclaredMethod("executeOperation", String.class,
					Object[].class);
			Object resultOperation = declaredMethod.invoke(personAdapter, "anosFabricacao", null);

			String result = (String) resultOperation;
			System.out.println(" rule object anosFabricacao retornou " + result);
			assertTrue("produto deve ser preenchido corretamente", result != null);

			Object value = produto.getProperty("raio").getValue();
			Map<String, Object> mapa = new HashMap<>();
			mapa.put("pi", Math.PI);
			mapa.put("raio", produto.getProperty("raio").getValue());
			String expr = "${pi*(raio*raio)}";

			Object executeEL = produto.executeEL(expr, DynamicELTest.class, mapa);
			System.out.println(executeEL);

		} catch (Exception e) {
			assertTrue(false);
		}
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
		home.setProperty("phone", "551197735588");
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
	public void createAdapterSensorProperty() throws EsfingeAOMException, AdapterFactoryFileReaderException,
			AdapterFactoryClassConstructionException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {

		IEntityType sensorType = new GenericEntityType("Sensor");

		sensorType.addPropertyType(new GenericPropertyType("name", String.class));
		sensorType.addPropertyType(new GenericPropertyType("type", String.class));

		IEntity person = sensorType.createNewEntity();
		person.setProperty("name", "radiometerX");
		person.setProperty("type", "radiometer");

		Object personAdapter = af.generate(person);

		String phone = (String) personAdapter.getClass().getMethod("getName").invoke(personAdapter);
		String type = (String) personAdapter.getClass().getMethod("getType").invoke(personAdapter);

		assertEquals(phone, person.getProperty("name").getValue());
		assertEquals(type, person.getProperty("type").getValue());
	}

}