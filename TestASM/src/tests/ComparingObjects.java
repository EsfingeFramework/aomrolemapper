package tests;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.model.impl.GenericEntityType;
import org.esfinge.aom.model.impl.GenericPropertyType;
import org.esfinge.comparison.ComparisonComponent;
import org.esfinge.comparison.difference.Difference;
import org.junit.Test;

import testPackage.GenerateClassesUsingAdapterImprovedV2;
import testPackage.AdapterFactory;
import testPackage.TestClass;

public class ComparingObjects {
	
	IEntityType entityTypeA = null;
	IEntityType entityTypeB = null;
		
	@Test
	public void comparingObjects() throws Exception {
		
		
		entityTypeA = new GenericEntityType("Car");

		entityTypeA.addPropertyType(new GenericPropertyType("plateNumber",
				String.class));
		entityTypeA.addPropertyType(new GenericPropertyType(
				"yearOfManufacturing", Integer.class));
		entityTypeA.addPropertyType(new GenericPropertyType("color",
				String.class));

		IEntity entityA = entityTypeA.createNewEntity();
		entityA.setProperty("plateNumber", "DGZ-1714");
		entityA.setProperty("yearOfManufacturing", 1980);
		entityA.setProperty("color", "yellow");

		AdapterFactory gc = new AdapterFactory();
		Object objA = gc.generate(entityA);
		
		
		entityTypeB = new GenericEntityType("Car");

		entityTypeB.addPropertyType(new GenericPropertyType("plateNumber",
				String.class));
		entityTypeB.addPropertyType(new GenericPropertyType(
				"yearOfManufacturing", Integer.class));
		entityTypeB.addPropertyType(new GenericPropertyType("color",
				String.class));

		IEntity entityB = entityTypeB.createNewEntity();
		entityB.setProperty("plateNumber", "DZZ-3421");
		entityB.setProperty("yearOfManufacturing", 1979);
		entityB.setProperty("color", "yellow");

		Object objB = gc.generate(entityB);
				
		System.out
				.println("**********************************************objA");
		printClass(objA);
		System.out.println("********************************************objB");
		printClass(objB);
		System.out
				.println("**************************************************");

		// ------------
		ComparisonComponent c = new ComparisonComponent();

		List<Difference> difs = c.compare(objA, objB);
		
		System.out.println("Differences:");
		
		for (Difference d : difs) {
			System.out.println(d.toString());
		}

	}

	private static void printClass(Object obj) {
		System.out.println("Type Name => " + obj.getClass().getTypeName());

		for (Method m : obj.getClass().getMethods()) {
			System.out.println(m);
		}
		for (Field field : obj.getClass().getDeclaredFields()) {
			System.out.println(field);
		}
	}

}
