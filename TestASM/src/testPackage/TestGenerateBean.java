package testPackage;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.model.impl.GenericEntity;
import org.esfinge.aom.model.impl.GenericEntityType;
import org.esfinge.aom.model.impl.GenericPropertyType;
import org.esfinge.aom.model.rolemapper.core.AdapterEntity;

public class TestGenerateBean {

	public static void main(String[] args) throws Exception {

		IEntityType entityType = new GenericEntityType("Person");
		entityType.addPropertyType(new GenericPropertyType("number",
				Integer.class));
		entityType.addPropertyType(new GenericPropertyType("height",
				Double.class));
		entityType
				.addPropertyType(new GenericPropertyType("name", String.class));

		IEntity entity = entityType.createNewEntity();
		entity.setProperty("number", 27);
		entity.setProperty("height", 1.8);
		entity.setProperty("name", "Ze");

		// System.out.println(entity.getProperty("number").getValue());
		// System.out.println(entity.getProperty("number").getPropertyType()
		// .getType());

		// Generate Class versions...
		// GenerateClasses2 gc = new GenerateClasses2();
		// GenerateClassesUsingAdapter gc = new GenerateClassesUsingAdapter();
		GenerateClassesUsingAdapterImproved gc = new GenerateClassesUsingAdapterImproved();

		Object obj = gc.generate(entity);

		System.out.println("----------------------Generated");
		printClass(obj);
		// System.out.println("Class Name => " + obj.getClass().getName());

		System.out.println("----------------------HandMade");
		Object handMadeAdapter = new TestClassBeanAdapter(entity);
		printClass2(handMadeAdapter);

	}

	private static void printClass(Object obj) {

		for (Method m : obj.getClass().getMethods()) {
			if (m.getName().startsWith("get")) {
				System.out.print(m + "   => ");
				try {
					System.out.println(m.invoke(obj));
				} catch (IllegalAccessException e) {

					e.printStackTrace();
				} catch (IllegalArgumentException e) {

					e.printStackTrace();
				} catch (InvocationTargetException e) {

					e.printStackTrace();
				}
			} else {
				System.out.println(m);
			}
		}

		for (Field field : obj.getClass().getDeclaredFields()) {
			System.out.println(field);
		}

	}

	private static void printClass2(Object obj) {
		for (Method m : obj.getClass().getMethods()) {
			System.out.println(m);
		}
		for (Field field : obj.getClass().getDeclaredFields()) {
			System.out.println(field);
		}
	}
}
