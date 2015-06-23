package testPackage;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JFileChooser;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.model.impl.GenericEntityType;
import org.esfinge.aom.model.impl.GenericPropertyType;

public class CopyOfTestGenerateBean {

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
		entity.setProperty("name", "João");
		
		BeanFactory gc = new BeanFactory();
		
		Object obj = gc.generate(entity, true);

		System.out.println("----------------------Generated");
		printClass(obj);

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
