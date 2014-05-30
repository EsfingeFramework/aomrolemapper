package org.esfinge.aom.performance;

import java.util.ArrayList;
import java.util.List;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.manager.ModelManager;

public class Scenario1 {

	public static void main(String[] args) throws EsfingeAOMException {
		
		int numberOfEntityTypes = 10;
		int numberOfPropertyTypes = 10;
		int numberOfEntities = 10 * numberOfEntityTypes;

		ModelManager manager = ModelManager.getInstance();
		Helper helper = new Helper(manager);

		for (int i = 0; i < numberOfEntityTypes; i++) {
			IEntityType entityType = helper.createEntityType("banking", "entity" + i, 
					"org.esfinge.aom.example.bankingsystem.accounts.AccountType");
			
			List<IPropertyType> propertyTypes = new ArrayList<IPropertyType>();
			for (int j = 0; j < numberOfPropertyTypes; j++) {
				IPropertyType propertyType = helper.createPropertyType(entityType, "property" + j, java.lang.Integer.class, 
						"org.esfinge.aom.example.bankingsystem.accounts.AccountPropertyType");
				propertyTypes.add(propertyType);
			}

			for (int j = 0; j < numberOfEntities; j++) {
				IEntity entity = helper.createEntity(entityType);
				
				for (IPropertyType propertyType : propertyTypes) {
					helper.setProperty(entity, propertyType, j);
				}
			}
			
			long start = System.currentTimeMillis();
			helper.selectStar(entityType);
			long end = System.currentTimeMillis();
			
			System.out.println(end-start);
		}		
	}
}