import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FetchType;
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
		Object client = createAOMEntityToPersist();
		
		//troca o ClassLoader padrão pelo ClassLoader do adapter. 
		//Assim quando o Hibernate for buscar as classe usando Class.forName ele irá encontrar a class dinâmica.
		//Isso ocorre pois foi necessário usa o método defineClass do ClassLoader, e para iso é preciso extender essa classe,
		//criando uma nova.
		Thread.currentThread().setContextClassLoader(client.getClass().getClassLoader());
				
		//Passando o endereço com o nome do banco para a opção hsqldb, o db será criado nele
		DAO dao = DAO.getInstance("hsqldb", "C:/data/clientes8", "aom_user", "aom_user");
		dao.addAnnotedClassToConnection(client.getClass());
		dao.addAnnotedClassToConnection(client.getClass().getMethod("getMainContact").invoke(client).getClass()); 
		Session session = dao.getSession(); 
	    session.getTransaction().begin();
		session.clear();
		session.merge(client);
		session.getTransaction().commit();
		session.close();
	}
	
	public static Object createAOMEntityToPersist() throws EsfingeAOMException{
		IEntityType contact = createContactEntity();
		
		//cria o objeto
		IEntity client = createClientEntity(contact).createNewEntity();
		client.setProperty("id", 55L);
		client.setProperty("name", "Guerra");
		client.setProperty("weight", 110.55);
		
		//Necessário configurar o PropertyType cliente de contact depois de adiona-lo a entity cliente		
		IPropertyType clientPropertyType = new GenericPropertyType("client", client.getEntityType());
		//annotations da propriedade type		
		Map<String, Object> clientParamtersOneToOne = new HashMap<String, Object>();
		clientPropertyType.setProperty("oneToOne", clientParamtersOneToOne);
		Map<String, Object> contactParamters = new HashMap<String, Object>();
		contactParamters.put("name", "fk_contato");
		clientPropertyType.setProperty("joinColumn", contactParamters);
		contact.addPropertyType(clientPropertyType);
		
		IEntity home = contact.createNewEntity();	
		home.setProperty("id", 100L);
		home.setProperty("phone", "1232312312");
		home.setProperty("type", "work");
		home.setProperty("client", client);
		
		client.setProperty("mainContact", home);
		
		AdapterFactory af = AdapterFactory.getInstance("AnnotationMapping.json");		
		Object clientBean = null;		
		Object homeBean = null;
		try {
			//tenho que criar as dependencias da entity composta antes para poder jogar no class path
			//jogar isso dentro do AdapterFactory, no if que verifica se é uma propriedade composta
			homeBean = af.generate((IEntity)client.getProperty("mainContact").getValue());
			ObjectPrinter.printClass(homeBean);
			clientBean = af.generate(client);
			System.out.println("-----------------------------");
			ObjectPrinter.printClass(clientBean);
			ObjectPrinter.printClass(homeBean);
			return clientBean;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static IEntityType createClientEntity(IEntityType contact) throws EsfingeAOMException {
		IEntityType clientType = new GenericEntityType("Client");		
		
		//coloca as propriedades
		IPropertyType idPropertyType = new GenericPropertyType("id", Long.class);
		clientType.addPropertyType(idPropertyType);
		IPropertyType namePropertyType = new GenericPropertyType("name", String.class);
		clientType.addPropertyType(namePropertyType);		
		IPropertyType weightPropertyType = new GenericPropertyType("weight", Double.class);
		clientType.addPropertyType(weightPropertyType);
		IPropertyType contactPropertyType = new GenericPropertyType("mainContact", contact);
		clientType.addPropertyType(contactPropertyType);
			
		//parametros das annotations de person
		Map<String, Object> parametersPerson = new HashMap<String, Object>();
		parametersPerson.put("name", "tb_clientes");
		//annotations da classe person
		clientType.setProperty("table", parametersPerson);
		clientType.setProperty("entity", true);
		
		//annotations da propriedade id
		idPropertyType.setProperty("id", true);
		
		//parametros das annotations de name
		Map<String, Object> nameParamters = new HashMap<String, Object>();
		nameParamters.put("name", "nome_cliente");
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
		
		//annotations da propriedade contact
		Map<String, Object> clientParamters = new HashMap<String, Object>();
		clientParamters.put("cascade", CascadeType.ALL);
		clientParamters.put("fetch", FetchType.EAGER);
		clientParamters.put("mappedBy", "client");
		contactPropertyType.setProperty("oneToOne", clientParamters);
		
		return clientType;
	}

	public static IEntityType createContactEntity() throws EsfingeAOMException {
		IEntityType contactType = new GenericEntityType("Contact");
		
		IPropertyType idPropertyType = new GenericPropertyType("id", Long.class);
		contactType.addPropertyType(idPropertyType);
		IPropertyType phonePropertyType = new GenericPropertyType("phone", String.class);
		contactType.addPropertyType(phonePropertyType);
		IPropertyType typePropertyType = new GenericPropertyType("type", String.class);
		contactType.addPropertyType(typePropertyType);
		
		//IEntityType clientType = new GenericEntityType("Client");
		//IPropertyType clientPropertyType = new GenericPropertyType("client", clientType);
		//contactType.addPropertyType(clientPropertyType);
		
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
		
		//annotations da propriedade type
		//clientPropertyType.setProperty("oneToOne", true);	
		
		return contactType;		
	}	
}