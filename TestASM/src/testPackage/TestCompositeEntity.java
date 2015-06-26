package testPackage;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.impl.GenericEntityType;
import org.esfinge.aom.model.impl.GenericPropertyType;
import org.esfinge.aom.model.rolemapper.metadata.annotations.Entity;

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
		
		
		IEntity joao = person.createNewEntity();
		joao.setProperty("name", "João");
		joao.setProperty("age", new Integer(18));
		IEntity home = contact.createNewEntity();
		home.setProperty("phone", "1232312312");
		home.setProperty("type", "work");
		joao.setProperty("mainContact", home);
		
		//dá pau!
		//AdapterFactory g = new AdapterFactory();
		//g.generate(joao);
		
		TemplateAdapter ta = new TemplateAdapter(joao);
		
		Object contactBean = ta.getMainContact();
		Class<?> contactBeanClass = contactBean.getClass();
		
		System.out.println(contactBean.getClass());
		System.out.println(contactBeanClass.getMethod("getPhone").invoke(contactBean));
		
		
		
		System.out.println("OK");

	}

}
