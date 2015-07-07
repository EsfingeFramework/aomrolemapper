package tests;

import static org.junit.Assert.assertEquals;

import org.esfinge.aom.api.model.HasProperties;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.impl.GenericEntityType;
import org.esfinge.aom.model.impl.GenericPropertyType;
import org.junit.Test;


public class TestAddingPropertiesToAnEntitty {
	@Test	
	public void testAddingPropertiesToAnEntitty() throws EsfingeAOMException {
		IEntityType entityType;
		HasProperties entity;

		entityType = new GenericEntityType("Car");

		entityType.addPropertyType(new GenericPropertyType("plateNumber",
				String.class));
		entityType.addPropertyType(new GenericPropertyType(
				"yearOfManufacturing", Integer.class));
		entityType.addPropertyType(new GenericPropertyType("color",
				String.class));

		// IEntity entity = entityType.createNewEntity();
		entity = entityType.createNewEntity();

		entity.setProperty("plateNumber", "DZZ-3421");
		entity.setProperty("yearOfManufacturing", 1997);
		entity.setProperty("color", "yellow");

		assertEquals(3, entity.getProperties().size());
		assertEquals("DZZ-3421", entity.getProperty("plateNumber").getValue());
		assertEquals(1997, entity.getProperty("yearOfManufacturing").getValue());
		assertEquals("yellow", entity.getProperty("color").getValue());

		assertEquals(String.class, entity.getProperty("plateNumber")
				.getPropertyType().getType());
		assertEquals(Integer.class, entity.getProperty("yearOfManufacturing")
				.getPropertyType().getType());
		assertEquals(String.class, entity.getProperty("color")
				.getPropertyType().getType());

		// fail("Not yet implemented");

	}

}
