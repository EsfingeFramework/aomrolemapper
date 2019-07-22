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
import org.esfinge.aom.model.impl.CalculaVolume;
import org.esfinge.aom.model.impl.ExpLangRuleObject;
import org.esfinge.aom.model.impl.GenericEntityType;
import org.esfinge.aom.model.impl.GenericPropertyType;
import org.esfinge.aom.model.impl.MenorQHoje;
import org.esfinge.aom.model.impl.PeriodoConsumo;
import org.esfinge.aom.model.rolemapper.core.AdapterEntity;
import org.esfinge.aom.model.rolemapper.core.AdapterEntityType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.RuleAttribute;
import org.esfinge.aom.model.rolemapper.metadata.annotations.RuleMethod;
import org.esfinge.aom.rolemapper.core.testclasses.entitytest.Account;
import org.esfinge.aom.rolemapper.core.testclasses.entitytest.AccountPropertyType;
import org.esfinge.aom.rolemapper.core.testclasses.entitytest.AccountType;
import org.esfinge.aom.rolemapper.core.testclasses.entitytest.Sensor;
import org.esfinge.aom.rolemapper.core.testclasses.entitytest.SensorPropertyType;
import org.esfinge.aom.rolemapper.core.testclasses.entitytest.SensorType;
import org.esfinge.aom.utils.ObjectPrinter;
import org.junit.Before;
import org.junit.Test;

import junit.framework.Assert;

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
			Method declaredMethod = personAdapter.getClass().getDeclaredMethod("periodoConsumo", String.class,
					Object[].class);
			Object resultOperation = declaredMethod.invoke(personAdapter, "periodoConsumo", null);

			Object resultado = produto.executeOperation("periodoConsumo");
			System.out.println(resultado);

			String days = (String) resultOperation;
			Integer value = (Integer) produto.getProperty("validade").getValue();
			System.out.println(" periodo consumo " + days + " validade " + value);
			assertTrue("@@@ Regra executeOperation executada com sucesso ", days != null);
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	public void createRule() throws EsfingeAOMException, AdapterFactoryFileReaderException,
			AdapterFactoryClassConstructionException, IllegalAccessException, IllegalArgumentException,
			InvocationTargetException, NoSuchMethodException, SecurityException {

		try {

			IEntityType tipoProduto = new GenericEntityType("Produto99");

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

			PeriodoConsumo consumo = new PeriodoConsumo("dataFabricacao");
			consumo.setProperty("nomeendpoint", "teste");
			tipoProduto.addOperation("periodoConsumo", new PeriodoConsumo("dataFabricacao"));

			IEntity produto = tipoProduto.createNewEntity();
			produto.setProperty("nome", "Yogurt X");
			GregorianCalendar dataFabr = new GregorianCalendar();
			dataFabr.set(2016, 11, 23);
			produto.setProperty("dataFabricacao", dataFabr.getTime());
			produto.setProperty("perecivel", Boolean.TRUE);
			produto.setProperty("validade", 90);

			Object resultado = produto.executeOperation("periodoConsumo");

			String days = (String) resultado;
			Integer value = (Integer) produto.getProperty("validade").getValue();
			System.out.println(" periodo consumo " + days + " validade " + value);
			assertTrue("@@@ Regra periodoConsumo executada com sucesso ", days != null);
		} catch (Exception e) {
			assertTrue(false);
		}
	}

	@Test(expected = EsfingeAOMException.class)
	public void testChangePropertyValue() throws EsfingeAOMException {

		//try {
			IEntityType tipoProduto = new GenericEntityType("ProdutoGelado");

			GenericPropertyType dataNascPropertyType = new GenericPropertyType("dataFabricacao", Date.class);

			dataNascPropertyType.setProperty("notempty", true);

			GenericPropertyType nomePropertyType = new GenericPropertyType("nome", String.class);
			nomePropertyType.setProperty("notempty", true);

			// adicionando property types no tipo de entidade
			tipoProduto.addPropertyType(dataNascPropertyType);
			tipoProduto.addPropertyType(nomePropertyType);

			// adiciona a operacao de comportamento
			tipoProduto.addOperation("menorqhoje", new MenorQHoje("dataFabricacao"));

			IEntity produto = tipoProduto.createNewEntity();
			produto.setProperty("nome", "Yogurt X");
			GregorianCalendar dataFabr = new GregorianCalendar();
			dataFabr.set(2017, 11, 23);
			produto.setProperty("dataFabricacao", dataFabr.getTime());

			// executa o comportamento
			Object resultado = produto.executeOperation("menorqhoje");
			Long days = (Long) resultado;
			System.out.println(days);
			assertTrue("@@@ Data de Fabricação menor do que hoje ", days > 0);

			// monitora a propriedade dataFabricacao
			produto.addPropertyMonitored("dataFabricacao", "menorqhoje");

			dataFabr.set(2019, 11, 03);
			// muda o valor da propriedade monitorada
			produto.setProperty("dataFabricacao", dataFabr.getTime());
			Object resultado2 = produto.getResultOperation("menorqhoje");

			days = (Long) resultado2;
			assertTrue("@@@ Data de Fabricação menor do que hoje ", days > 0);
		//} catch (EsfingeAOMException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		//}
	}

	@Test
	public void testDataFabricacao() {
		try {
			IEntityType tipoProduto = new GenericEntityType("Produto99");

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

			CalculaAnos consumo = new CalculaAnos("dataFabricacao");
			consumo.setEntityType(tipoProduto);
			consumo.setProperty("nomeendpoint", "teste");
			tipoProduto.addOperation("anosFabricacao", consumo);

			// add segunda regra
			tipoProduto.addOperation("periodoConsumo", new PeriodoConsumo("dataFabricacao"));

			IEntity produto = tipoProduto.createNewEntity();
			produto.setProperty("nome", "Notebook DELL");
			GregorianCalendar dataFabr = new GregorianCalendar();
			dataFabr.set(2010, 11, 23);
			produto.setProperty("dataFabricacao", dataFabr.getTime());
			produto.setProperty("raio", 15);

			Object personAdapter = af.generate(produto);
			Method declaredMethod = personAdapter.getClass().getDeclaredMethod("anosFabricacao", String.class,
					Object[].class);
			Object resultOperation = declaredMethod.invoke(personAdapter, "anosFabricacao", null);

			String result = (String) resultOperation;
			System.out.println(" rule object anosFabricacao retornou " + result);
			assertTrue("produto deve ser preenchido corretamente", result != null);

			// executa segunda regra
			declaredMethod = personAdapter.getClass().getDeclaredMethod("periodoConsumo", String.class, Object[].class);
			Object resultOperation2 = declaredMethod.invoke(personAdapter, "periodoConsumo", null);
			String result2 = (String) resultOperation2;
			System.out.println(" rule object periodoConsumo retornou " + result2);
			assertTrue("produto deve ser consumido", result2 != null);

			// teste de EL
			// Object value = produto.getProperty("raio").getValue();
			// Map<String, Object> mapa = new HashMap<>();
			// mapa.put("pi", Math.PI);
			// mapa.put("raio", produto.getProperty("raio").getValue());
			// String expr = "${pi*(raio*raio)}";
			//
			// Object executeEL = produto.executeEL(expr, DynamicELTest.class,
			// mapa);
			// System.out.println("EL: " + executeEL);

			String expr = "${pi*(raio*raio)}";
			String nameRule = "ELarea";

			Map<String, Object> others = new HashMap<>();
			others.put("pi", Math.PI);

			ExpLangRuleObject expLangRuleObject = new ExpLangRuleObject(expr);
			expLangRuleObject.setMapValues(others);

			tipoProduto.addOperation(nameRule, expLangRuleObject);
			Object executeOperation = produto.executeOperation(nameRule);
			System.out.println("EL retornou " + executeOperation);

		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	public void testELMediana() {
		try {
			IEntityType tipoProduto = new GenericEntityType("Produto");

			// criando property types

			GenericPropertyType nomePropertyType = new GenericPropertyType("nome", String.class);
			nomePropertyType.setProperty("notempty", true);

			GenericPropertyType msMin = new GenericPropertyType("medidaMinima", Double.class);
			GenericPropertyType msMax = new GenericPropertyType("medidaMaxima", Double.class);

			// adicionando property types no tipo de entidade
			tipoProduto.addPropertyType(nomePropertyType);
			tipoProduto.addPropertyType(msMin);
			tipoProduto.addPropertyType(msMax);

			IEntity produto = tipoProduto.createNewEntity();
			produto.setProperty("nome", "Sensor");
			produto.setProperty("medidaMinima", 15);
			produto.setProperty("medidaMaxima", 10);

			String expr = "${ (medidaMinima+medidaMaxima) / 2 }";
			String nameRule = "mediana";
			tipoProduto.addOperation(nameRule, new ExpLangRuleObject(expr));
			Object executeOperation = produto.executeOperation(nameRule);
			System.out.println("EL retornou " + executeOperation);

		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	public void testELMedianaAdapter() {
		try {
			IEntityType tipoProduto = new GenericEntityType("Produto");

			// criando property types

			GenericPropertyType nomePropertyType = new GenericPropertyType("nome", String.class);
			nomePropertyType.setProperty("notempty", true);

			GenericPropertyType msMin = new GenericPropertyType("medidaMinima", Double.class);
			GenericPropertyType msMax = new GenericPropertyType("medidaMaxima", Double.class);

			// adicionando property types no tipo de entidade
			tipoProduto.addPropertyType(nomePropertyType);
			tipoProduto.addPropertyType(msMin);
			tipoProduto.addPropertyType(msMax);

			String nameRule = "mediana";
			String expr = "${ (medidaMinima+medidaMaxima) / 2 }";
			ExpLangRuleObject expLangRuleObject = new ExpLangRuleObject(expr);

			Map<String, Object> propriedades = new HashMap<>();
			propriedades.put("nomeendpoint", "teste");
			propriedades.put("restcontroller", true);
			expLangRuleObject.setOperationProperties(propriedades);

			tipoProduto.addOperation(nameRule, expLangRuleObject);

			IEntity produto = tipoProduto.createNewEntity();
			produto.setProperty("nome", "Sensor");
			produto.setProperty("medidaMinima", 15);
			produto.setProperty("medidaMaxima", 10);

			Object executeOperation = produto.executeOperation(nameRule);
			System.out.println("EL retornou " + executeOperation);

			Object personAdapter = af.generate(produto);
			Method declaredMethod = personAdapter.getClass().getDeclaredMethod(nameRule, String.class, Object[].class);
			ObjectPrinter.printClass(personAdapter);
			Object resultOperation = declaredMethod.invoke(personAdapter, nameRule, null);
			System.out.println("EL adaptada retornou: " + resultOperation);

		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	public void testVolume() {
		try {
			IEntityType tipoProduto = new GenericEntityType("Cilindro");

			// criando property types
			GenericPropertyType raio = new GenericPropertyType("raio", Double.class);

			// adicionando property types no tipo de entidade
			tipoProduto.addPropertyType(raio);

			tipoProduto.addOperation("volume", new CalculaVolume("raio"));

			IEntity produto = tipoProduto.createNewEntity();
			produto.setProperty("raio", 10d);

			Object personAdapter = af.generate(produto);
			Method declaredMethod = personAdapter.getClass().getDeclaredMethod("volume", String.class, Object[].class);
			Object[] params = new Object[1];
			params[0] = 5d;
			Object resultOperation = declaredMethod.invoke(personAdapter, "volume", params);

			String result = (String) resultOperation;
			System.out.println(" rule object anosFabricacao retornou " + result);
			assertTrue("produto deve ser preenchido corretamente", result != null);
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

	@Test
	public void testeAdapterSensorType1() throws Exception {
		AccountPropertyType accountPropertyType = new AccountPropertyType();
		accountPropertyType.setName("Balance");
		accountPropertyType.setPropertyType(double.class);
		AccountType accountType = new AccountType();
		Account account = new Account();
		accountType.addPropertyTypes(accountPropertyType);
		account.setAccountType(accountType);

		double value = 100000.25;
		AdapterEntityType entityType = AdapterEntityType.getAdapter(accountType);
		AdapterEntity entity = AdapterEntity.getAdapter(entityType, account);
		entity.setProperty("Balance", value);

		Assert.assertEquals(value, entity.getProperty("Balance").getValue());

	}

	@Test
	public void testeAdapterSensorType() throws Exception {
		SensorType sensorType = new SensorType();
		SensorPropertyType sensorPropertyType = new SensorPropertyType();
		sensorPropertyType.setName("varredura");
		sensorPropertyType.setPropertyType(double.class);
		sensorType.addPropertyTypes(sensorPropertyType);

		Sensor sensor = new Sensor();
		sensor.setSensorType(sensorType);

		double valorVarredura = 100.0;
		AdapterEntityType adaptedEntityType = AdapterEntityType.getAdapter(sensorType);
		AdapterEntity entity = AdapterEntity.getAdapter(adaptedEntityType, sensor);

		entity.setProperty("varredura", valorVarredura);
		Assert.assertEquals(valorVarredura, entity.getProperty("varredura").getValue());
	}

}