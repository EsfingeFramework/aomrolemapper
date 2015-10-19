package br.inpe.dga.main;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.impl.GenericEntityType;
import org.esfinge.aom.model.impl.GenericPropertyType;
import org.esfinge.aom.model.rolemapper.core.AdapterEntity;
import org.esfinge.aom.model.rolemapper.core.AdapterEntityType;
import org.esfinge.comparison.CompareException;
import org.esfinge.comparison.ComparisonComponent;
import org.esfinge.comparison.difference.Difference;

import br.inpe.dga.adapter.PersonAOMBeanAdapter;
import br.inpe.dga.beans.Car;
import br.inpe.dga.beans.CarType;
import br.inpe.dga.factory.AdapterFactory;
import br.inpe.dga.factory.AdapterTypesFactory;
import br.inpe.dga.utils.AnnotationMapFileName;
import br.inpe.dga.utils.ObjectPrinter;

public class TestGenerateAnnotationToMetadata {

	public static void main(String[] args) throws EsfingeAOMException{
		IEntityType personType = new GenericEntityType("Person");		
		
		//coloca as propriedades
		IPropertyType namePropertyType = new GenericPropertyType("name", String.class);
		personType.addPropertyType(namePropertyType);		
		IPropertyType agePropertyType = new GenericPropertyType("age", Integer.class);
		personType.addPropertyType(agePropertyType);
		IPropertyType weightPropertyType = new GenericPropertyType("weight", Double.class);
		personType.addPropertyType(weightPropertyType);
		
		//coloca os metadados na entity
		personType.setProperty("persistence", true);
		//coloca os metadados nas properties
		//namePropertyType.setProperty("ignore", true);
		Map<String, Object> parametersSubstring = new HashMap<String, Object>();
		parametersSubstring.put("begin", 1);
		parametersSubstring.put("end", 3);
		namePropertyType.setProperty("substring", parametersSubstring);
		weightPropertyType.setProperty("tolerance", 0.01);
		
		//cria o objeto
		IEntity person1 = personType.createNewEntity();
		person1.setProperty("name", "Guerra");
		person1.setProperty("age", 35);
		person1.setProperty("weight", 110.55);
		
		IEntity person2 = personType.createNewEntity();
		person2.setProperty("name", "Grerraaa");
		person2.setProperty("age", 34);
		person2.setProperty("weight", 110.57);
		
		AdapterFactory af = AdapterFactory.getInstance(AnnotationMapFileName.NAME.getName());		
		Object personBean1 = null;		
		Object personBean2 = null;
		try {
			personBean1 = af.generate(person1);
			personBean2 = af.generate(person2);			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		ClassLoader clAdapter = new PersonAOMBeanAdapter(person1).getClass().getClassLoader();
		System.out.println(clAdapter.getClass());
		
		ClassLoader clDynamicdapter = personBean1.getClass().getClassLoader();
		System.out.println(clDynamicdapter.getParent().getClass());
		
		
		try {
			System.out.println(Class.forName(personBean1.getClass().getName()));
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println("----------------------Generated");		
		ObjectPrinter.printClass(personBean1);
		
		ComparisonComponent c  = new ComparisonComponent();
		
		List<Difference> difs = null;
		try {
			difs = c.compare(personBean1,personBean2);
		} catch (CompareException e) {
			e.printStackTrace();
		}
		
		for(Difference d : difs){
	            System.out.println(d);
	    }
	}
}