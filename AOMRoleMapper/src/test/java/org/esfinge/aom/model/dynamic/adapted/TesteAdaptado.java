package org.esfinge.aom.model.dynamic.adapted;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.esfinge.aom.model.dynamic.factory.AdapterFactory;
import org.esfinge.aom.model.rolemapper.core.AdapterEntity;
import org.esfinge.aom.model.rolemapper.core.AdapterEntityType;
import org.junit.Test;

import junit.framework.Assert;

public class TesteAdaptado {

	@Test
	public void testAdaptarSensor() {
		try {
			SensorType sensorType = new SensorType();
			//sensorType.addOperacao("regraSensor", new OperacaoRegraSensor());
			sensorType.addOperation("regraSensor", new OperacaoRegraSensor());

			SensorPropertyType prop = new SensorPropertyType();
			prop.setName("unidade");
			prop.setPropertyType(String.class);
			sensorType.addPropertyTypes(prop);
			
			SensorPropertyType prop2 = new SensorPropertyType();
			prop2.setName("propriedade");
			prop2.setPropertyType(String.class);
			sensorType.addPropertyTypes(prop2);

			Sensor sensor = new Sensor();
			sensor.setSensorType(sensorType);

			AdapterEntityType adaptedEntityType = AdapterEntityType.getAdapter(sensorType);
			AdapterEntity entity = AdapterEntity.getAdapter(adaptedEntityType, sensor);
			
			// vai executar a operacao adicionada no modelo dependente de
			// dominio
			Object resultDepend = entity.executeOperation("regraSensor", sensor, null);
			Assert.assertEquals(10, resultDepend);

			entity.setProperty("unidade", "K");
			entity.setProperty("propriedade", "grau");
			
			// gerar o adaptador e invocar o metodo regraSensor 
			AdapterFactory af = AdapterFactory.getInstance("JsonMapTest.json");
			
			Object adapted = af.generateAdapted(entity, "sensorAdaptado");
			
			Method declaredMethod = adapted.getClass().getDeclaredMethod("regraSensor", String.class, Object[].class);
	
			System.out.println(declaredMethod);
			
			Object resultOperation = declaredMethod.invoke(adapted,  "regraSensor", new Object[2]);
			System.out.println("adaptada retornou: " + resultOperation);
			Assert.assertEquals(10, resultDepend);

			// vai executar a operacao fixa no modelo independente de dominio
			Object resultIndep = entity.executeOperation("converterUnidade", "propriedade", "unidade");
			Assert.assertEquals(1, resultIndep);
		} catch (InvocationTargetException e) {
			e.getTargetException().printStackTrace();
			Assert.assertTrue(false);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}catch (Exception e) {
			e.printStackTrace();
			Assert.assertTrue(false);
		}
	}
}
