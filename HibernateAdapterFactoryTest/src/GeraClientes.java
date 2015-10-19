import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.OneToOne;
import javax.persistence.Persistence;

import org.esfinge.aom.api.model.HasProperties;
import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.impl.GenericEntityType;
import org.esfinge.aom.model.impl.GenericProperty;
import org.esfinge.aom.model.impl.GenericPropertyType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.Cascade;
import org.hibernate.cfg.Configuration;
import org.esfinge.aom.model.dynamic.factory.AdapterFactory;

import br.inpe.jpateste.utils.ObjectPrinter;

public class GeraClientes {
	public static void main(String[] args) throws EsfingeAOMException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ClassNotFoundException{
		Object person = createAOMEntityToPersist();
		
		//troca o ClassLoader padrão pelo ClassLoader do adapter. 
		//Assim quando o Hibernate for buscar as classe usando Class.forName ele irá encontrar a class dinâmica.
		//Isso ocorre pois foi necessário usa o método defineClass do ClassLoader, e para iso é preciso extender essa classe,
		//criando uma nova.
		Thread.currentThread().setContextClassLoader(person.getClass().getClassLoader());
		
		//Passando o endereço com o nome do banco para a opção hsqldb, o db será criado nele
		DAO dao = DAO.getInstance("hsqldb", "C:/data/clientes", "aom_user", "aom_user");
		dao.addAnnotedClassToConnection(person.getClass());
		dao.addAnnotedClassToConnection(person.getClass().getMethod("getMainContact").invoke(person).getClass()); 
		Session session = dao.getSession(); 
	    session.getTransaction().begin();
		session.clear();
		session.save(person);
		session.getTransaction().commit();
		session.close();
	}
	
	public static Object createAOMEntityToPersist() throws EsfingeAOMException{
		IEntityType contact = createContactEntity();
		IEntity home = contact.createNewEntity();	
		home.setProperty("id", 6L);
		home.setProperty("phone", "1232312312");
		home.setProperty("type", "work");
		
		//cria o objeto
		IEntity person = createPersonEntity(contact).createNewEntity();
		person.setProperty("id", 26L);
		person.setProperty("name", "Guerra");
		person.setProperty("weight", 110.55);
		person.setProperty("mainContact", home);

		AdapterFactory af = AdapterFactory.getInstance("AnnotationMapping.json");		
		Object personBean = null;		
		Object homeBean = null;
		try {
			//tenho que criar as dependencias da entity composta antes para poder jogar no class path
			//jogar isso dentro do AdapterFactory, no if que verifica se é uma propriedade composta
			homeBean = af.generate((IEntity)person.getProperty("mainContact").getValue());
			ObjectPrinter.printClass(homeBean);
			personBean = af.generate(person);
			System.out.println("-----------------------------");
			//ObjectPrinter.printClass(personBean);
			//ObjectPrinter.printClass(homeBean);
			return personBean;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static IEntityType createPersonEntity(IEntityType contact) throws EsfingeAOMException {
		IEntityType personType = new GenericEntityType("Person");		
		
		//coloca as propriedades
		IPropertyType idPropertyType = new GenericPropertyType("id", Long.class);
		personType.addPropertyType(idPropertyType);
		IPropertyType namePropertyType = new GenericPropertyType("name", String.class);
		personType.addPropertyType(namePropertyType);		
		IPropertyType weightPropertyType = new GenericPropertyType("weight", Double.class);
		personType.addPropertyType(weightPropertyType);
		IPropertyType contactPropertyType = new GenericPropertyType("mainContact", contact);
		personType.addPropertyType(contactPropertyType);
			
		//parametros das annotations de person
		Map<String, Object> parametersPerson = new HashMap<String, Object>();
		parametersPerson.put("name", "tb_pessoas");
		//annotations da classe person
		personType.setProperty("table", parametersPerson);
		personType.setProperty("entity", true);
		
		//annotations da propriedade id
		idPropertyType.setProperty("id", true);
		
		//parametros das annotations de name
		Map<String, Object> nameParamters = new HashMap<String, Object>();
		nameParamters.put("name", "nome_pessoa");
		nameParamters.put("nullable", false);
		//annotations da propriedade de name
		namePropertyType.setProperty("column", nameParamters);
		
		//parametros das annotations de weight
		Map<String, Object> weightParamters = new HashMap<String, Object>();
		weightParamters.put("name", "peso");
		weightParamters.put("nullable", false);
		//annotations da propriedade de weight
		weightPropertyType.setProperty("column", weightParamters);
		
		//parametros das annotations de contact
		Map<String, Object> contactParamters = new HashMap<String, Object>();
		//contactParamters.put("cascade", CascadeType.ALL.ordinal());
		//annotations da propriedade de weight
		contactPropertyType.setProperty("oneToOne", contactParamters);
		contactPropertyType.setProperty("primaryKeyJoinColumn", true);
		
		return personType;
	}

	public static IEntityType createContactEntity() throws EsfingeAOMException {
		IEntityType contactType = new GenericEntityType("Contact");
		
		IPropertyType idPropertyType = new GenericPropertyType("id", Long.class);
		contactType.addPropertyType(idPropertyType);
		IPropertyType phonePropertyType = new GenericPropertyType("phone", String.class);
		contactType.addPropertyType(phonePropertyType);
		IPropertyType typePropertyType = new GenericPropertyType("type",	String.class);
		contactType.addPropertyType(typePropertyType);
		
		//parametros das annotations de contact
		Map<String, Object> parametersPerson = new HashMap<String, Object>();
		parametersPerson.put("name", "tb_contato");
		//annotations da classe contact
		contactType.setProperty("table", parametersPerson);
		contactType.setProperty("entity", true);
		
		//annotations do método do campo id
		idPropertyType.setProperty("id", true);

		//parametros das annotations de phone
		Map<String, Object> phoneParamters = new HashMap<String, Object>();
		phoneParamters.put("name", "telefone");
		phoneParamters.put("nullable", false);
		//annotations da propriedade phone
		phonePropertyType.setProperty("column", phoneParamters);
		
		//parametros das annotations de type
		Map<String, Object> typeParamters = new HashMap<String, Object>();
		typeParamters.put("name", "tipo");
		typeParamters.put("nullable", false);
		//annotations da propriedade type
		typePropertyType.setProperty("column", typeParamters);		
		
		return contactType;		
	}	
}