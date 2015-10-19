package br.inpe.dga.main;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.swing.JFileChooser;

import org.esfinge.aom.api.model.HasProperties;
import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.model.impl.GenericEntityType;
import org.esfinge.aom.model.impl.GenericPropertyType;

import br.inpe.dga.adapter.TemplateAdapter;
import br.inpe.dga.factory.AdapterFactory;
import br.inpe.dga.old.adapter.TestClassBeanAdapter;
import br.inpe.dga.utils.AnnotationMapFileName;
import br.inpe.dga.utils.ObjectPrinter;

public class TestGenerateBean {
	public static void main(String[] args) throws Exception {

		IEntityType entityType = new GenericEntityType("Person");
		entityType.addPropertyType(new GenericPropertyType("number",
				Integer.class));
		entityType.addPropertyType(new GenericPropertyType("height",
				Double.class));
		entityType
				.addPropertyType(new GenericPropertyType("name", String.class));
		
		IEntityType contact = new GenericEntityType("Contact");
		contact.addPropertyType(new GenericPropertyType("phone",
				String.class));
		contact.addPropertyType(new GenericPropertyType("type",
				String.class));
		
		entityType.addPropertyType(new GenericPropertyType("mainContact",contact));
		
		HasProperties entityComplex = contact.createNewEntity();
		entityComplex.setProperty("phone", "1232312312");
		entityComplex.setProperty("type", "work");

		IEntity entity = entityType.createNewEntity();
		entity.setProperty("number", 27);
		entity.setProperty("height", 1.8);
		entity.setProperty("name", "João");	
		entity.setProperty("mainContact", entityComplex);
		
		AdapterFactory af = AdapterFactory.getInstance(AnnotationMapFileName.NAME.getName());
		
		Object obj = af.generate(entity, true);

		System.out.println("----------------------Generated");
		
		ObjectPrinter.printClass(obj);

		System.out.println("----------------------HandMade");
		Object templateAdapter = new TemplateAdapter(entity);
		ObjectPrinter.printClass2(templateAdapter);
	}
}