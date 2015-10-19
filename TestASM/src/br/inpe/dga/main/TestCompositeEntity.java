package br.inpe.dga.main;

import java.lang.reflect.Method;

import org.esfinge.aom.api.model.HasProperties;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.impl.GenericEntityType;
import org.esfinge.aom.model.impl.GenericPropertyType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.Entity;

import br.inpe.dga.adapter.TemplateAdapter;

public class TestCompositeEntity {

	public static void main(String[] args) throws Exception {
		IEntityType person = new GenericEntityType("Person");
		IEntityType contact = new GenericEntityType("Contact");

		person.addPropertyType(new GenericPropertyType("name",
				String.class));
		person.addPropertyType(new GenericPropertyType("age",
				Integer.class));
		
		contact.addPropertyType(new GenericPropertyType("phone",
				String.class));
		contact.addPropertyType(new GenericPropertyType("type",
				String.class));
		
		person.addPropertyType(new GenericPropertyType("mainContact",
				contact));
		
		
		HasProperties joao = person.createNewEntity();
		joao.setProperty("name", "João");
		joao.setProperty("age", new Integer(18));
		HasProperties home = contact.createNewEntity();
		home.setProperty("phone", "1232312312");
		home.setProperty("type", "work");
		joao.setProperty("mainContact", home);
			
		TemplateAdapter ta = new TemplateAdapter(joao);
		
		Object contactBean = ta.getMainContact();
		
		System.out.println(contactBean.getClass().getMethod("getPhone").invoke(contactBean));

		HasProperties home2 = contact.createNewEntity();
		home2.setProperty("phone", "4444444");
		home2.setProperty("type", "home");
		ta.setMainContact(home2);
		contactBean = ta.getMainContact();
		
		System.out.println(contactBean.getClass().getMethod("getPhone").invoke(contactBean));
		System.out.println(ta.getClass().getMethod("getMainContact").invoke(ta));
		
		System.out.println("OK");
	}

}
