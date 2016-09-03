package org.esfinge.aom.simpletypesquare;

import net.sf.esfinge.aom.api.model.HasProperties;
import net.sf.esfinge.aom.api.model.IEntity;
import net.sf.esfinge.aom.api.model.IEntityType;
import net.sf.esfinge.aom.api.model.IProperty;
import net.sf.esfinge.aom.api.model.IPropertyType;
import net.sf.esfinge.aom.exceptions.EsfingeAOMException;
import net.sf.esfinge.aom.model.factories.EntityTypeFactory;
import net.sf.esfinge.aom.model.factories.PropertyTypeFactory;
import net.sf.esfinge.aom.model.rolemapper.core.AdapterEntity;

public class PrincipalTypeSquare {
	
	public static void main(String[] args) throws EsfingeAOMException{
		ProductType pt = new ProductType("Book");
		InformationType author = new InformationType("author", String.class);
		pt.getList().add(author);
		InformationType year = new InformationType("year", Integer.class);
		pt.getList().add(year);
		
		IEntityType type = EntityTypeFactory.createEntityType(pt);
		type.addPropertyType(PropertyTypeFactory.createPropertyType("title", String.class));
		
		for(IPropertyType propType: type.getPropertyTypes()){
			System.out.println(propType.getName() +" - "+ propType.getType());
		}
		
		System.out.println("--------------------");
		
		for(InformationType it : pt.getList()){
			System.out.println(it.getName());
		}
		for(IPropertyType propType: type.getPropertyTypes()){
			System.out.println(propType.getName() +" - "+ propType.getType());
		}
		
		System.out.println("-------------------- Cria entidade e adapta");
		
		
		Product p = new Product();
		p.setType(pt);
		p.setProductName("Dog Mendonca e Pizzaboy");
		p.getInformations().add(new Information(author,"Filipe Melo"));
		p.getInformations().add(new Information(year,2003));
		
		HasProperties entity = AdapterEntity.getAdapter(null, p);
		
		for(IProperty ip : entity.getProperties()){
			System.out.println(ip.getPropertyType().getName()+" = "+ ip.getValue());
		}

		System.out.println("-------------------- Cria adapter e recupera entidade");
		
		IEntity entity2 = new AdapterEntity(type, Product.class);
		entity2.setProperty("author", "Filipe Melo");
		entity2.setProperty("year", 2003);
		entity2.setProperty("productName", "Dog Mendonca e Pizzaboy");
		
		Product p2 = (Product) entity2.getAssociatedObject();
		System.out.println("productName = "+p2.getProductName());
		for(Information i : p2.getInformations()){
			System.out.println(i.getType().getName()+" = "+i.getInfo());
		}
		
	}

}
