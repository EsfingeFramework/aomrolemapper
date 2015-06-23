package tests;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.model.impl.GenericEntityType;
import org.esfinge.aom.model.impl.GenericPropertyType;
import org.junit.Test;

import testPackage.GenerateClassesUsingAdapterImprovedV3;

public class TestGeneratingAClass {
	
	IEntityType entityType;
	IEntity entity;
	
	@Test
	public void testGeneratingAClass() throws Exception {

		entityType = new GenericEntityType("Car");

		entityType.addPropertyType(new GenericPropertyType("plateNumber",
				String.class));
		entityType.addPropertyType(new GenericPropertyType(
				"yearOfManufacturing", Integer.class));
		entityType.addPropertyType(new GenericPropertyType("color",
				String.class));

		entityType.addPropertyType(new GenericPropertyType("internalSpace",
				Double.class));
		entityType.addPropertyType(new GenericPropertyType("inStock",
				Boolean.class));

		// IEntity entity = entityType.createNewEntity();
		entity = entityType.createNewEntity();

		entity.setProperty("plateNumber", "DZZ-3421");
		entity.setProperty("yearOfManufacturing", 1997);
		entity.setProperty("color", "yellow");

		entity.setProperty("internalSpace", 3.8);
		entity.setProperty("inStock", true);

		GenerateClassesUsingAdapterImprovedV3 gc = new GenerateClassesUsingAdapterImprovedV3();
		Object obj = gc.generate(entity, false);

		assertEquals(5, entity.getProperties().size());

		Method m = obj.getClass().getMethod("getPlateNumber");
		assertEquals("DZZ-3421", m.invoke(obj));
		assertEquals(String.class, m.getReturnType());

		m = obj.getClass().getMethod("getYearOfManufacturing");
		assertEquals(1997, m.invoke(obj));
		assertEquals(Integer.class, m.getReturnType());

		m = obj.getClass().getMethod("getColor");
		assertEquals("yellow", m.invoke(obj));
		assertEquals(String.class, m.getReturnType());

		m = obj.getClass().getMethod("getInternalSpace");
		assertEquals(3.8, m.invoke(obj));
		assertEquals(Double.class, m.getReturnType());

		m = obj.getClass().getMethod("getInStock");
		assertEquals(true, m.invoke(obj));
		assertEquals(Boolean.class, m.getReturnType());

		System.out.println("------------------------------------------");
		for (Method methods : obj.getClass().getMethods()) {
			if (m.getName().startsWith("get")) {

			}

			System.out.println(methods);
		}
		for (Field field : obj.getClass().getDeclaredFields()) {
			System.out.println(field);
		}
		System.out.println("------------------------------------------");
	}
}
