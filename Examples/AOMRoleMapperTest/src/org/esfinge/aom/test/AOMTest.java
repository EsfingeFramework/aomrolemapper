package org.esfinge.aom.test;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Scanner;

import org.esfinge.aom.api.model.HasProperties;
import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;
import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.manager.ModelManager;
import org.esfinge.aom.manager.configuration.ModelConfiguration;
import org.esfinge.aom.model.factories.EntityTypeFactory;
import org.esfinge.aom.model.factories.PropertyTypeFactory;

public class AOMTest {

	private ModelManager manager;
	
	private IEntity entity;
	
	private List<IEntityType> model;
		
	/**
	 * @param args
	 * @throws EsfingeAOMException 
	 * @throws URISyntaxException 
	 */
	public static void main(String[] args) throws EsfingeAOMException {		
		
		AOMTest program = new AOMTest();
		program.selectPersistenceFrameworkMenu();
		program.showMainMenu();
	}
	
	private void selectPersistenceFrameworkMenu() {
		int option;
		Scanner in = new Scanner(System.in);
		do {
			System.out.println("Select the persistence framework you want to use:");
			System.out.println("1- Autoselect using ServiceLocator.");
			System.out.println("2- Use MongoDB.");
			System.out.println("3- Use CouchDB.");
			System.out.println("4- Use Neo4J.");
			option = Integer.parseInt(in.nextLine());
			switch (option) {
				case 1:
					manager = ModelManager.getInstance();
					break;
				case 2:
					manager = ModelManager.getInstance("org.esfinge.aom.persistence.mongodb.MongoAOM");
					break;
				case 3:
					manager = ModelManager.getInstance("org.esfinge.aom.persistence.couchdb.CouchAOM");
					break;
				case 4:
					manager = ModelManager.getInstance("org.esfinge.aom.persistence.couchdb.Neo4jAOM");
					break;
				default:
					System.out.println("Invalid choice.");
			}
		} while(option < 1 || option > 4);
	}
	
	private void showMainMenu ()
	{
		System.out.println("Select one option: ");
		System.out.println("1- Read model");
		System.out.println("2- Load model from database");
		System.out.println("3- Save model");
		System.out.println("4- Manipulate system");
		System.out.println("5- Exit");

		Scanner in = new Scanner(System.in);
		int option = Integer.parseInt(in.nextLine());
		
		switch (option)
		{
			case 1:	
				ModelConfiguration configuration = new ModelConfiguration(modelConfigPathMenu());
				model = configuration.getModel();		
				showMainMenu();
				return;
				
			case 2:
				try {
					model = manager.loadModel();
				} catch (EsfingeAOMException e1) {
					e1.printStackTrace();
				}
				showMainMenu();
				return;
				
			case 3: 
				if (model == null)
				{
					System.out.println("Please load/read model first"); 
				}
				else
				{
					for (IEntityType entityType : model)
					{
						try {
							manager.save(entityType);
						} catch (EsfingeAOMException e1) {
							e1.printStackTrace();
						}
					}
				}
				showMainMenu();
				return;
				
			case 4:
				manipulateSystem();
				return;
				
			default:
				return;

		}
	}
	
	private String modelConfigPathMenu() {
		String configPath = null;
		int option;
		Scanner in = new Scanner(System.in);
		do {
			System.out.println("Select the configuration file you want to use:");
			System.out.println("1- src/Config/BankingModelConfiguration.xml");
			System.out.println("2- src/Config/MedicalModelConfiguration.xml");
			System.out.println("3- Enter configuration file path.");
			option = Integer.parseInt(in.nextLine());
			switch (option) {
				case 1:
					configPath = "src/Config/BankingModelConfiguration.xml"; 
					break;
				case 2:
					configPath = "src/Config/MedicalModelConfiguration.xml"; 
					break;
				case 3:
					configPath = in.nextLine();
					File f = new File(configPath);
					if (!f.exists() || f.isDirectory()) {
						System.out.println("File not found: " + f.getAbsolutePath());
						configPath = null;
					}
					break;
				default:
					System.out.println("Invalid  option.");
			}
			
		} while(configPath == null);
		return configPath;		
	}
	
	private void manipulateSystem ()
	{
		System.out.println("Select one: ");
		for (int i = 0; i < model.size(); i++)
		{
			try {
				System.out.println(i + "- " + model.get(i).getName());
			} catch (EsfingeAOMException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println(model.size() + "- Create new entity type");
		System.out.println((model.size() + 1) + "- Back");
		
		Scanner in = new Scanner(System.in);
		int option = Integer.parseInt(in.nextLine());
		
		if (option < model.size())
		{
			IEntityType entityType = model.get(option);
			action(entityType);
		}
		else
		{
			if (option == model.size())
			{
				createEntityType();
			}
			else
			{
				showMainMenu();
			}
		}		
	}
	
	private void createEntityType ()
	{ 
		int option = 0;
		String packageName = "";
		String typeName = null;
		String adaptedClass = null;
		
		while (option < 4 || typeName == null)
		{
			System.out.println("Select one action: ");
			System.out.println("1- Enter package name");
			System.out.println("2- Enter name (mandatory)");
			System.out.println("3- Enter adapted class");
			System.out.println("4- Create entity type");
			System.out.println("5- Back");
			
			Scanner in = new Scanner(System.in);
			option = Integer.parseInt(in.nextLine());
			
			switch (option)
			{
			case 1:
				packageName = in.nextLine();
				break;
				
			case 2:
				typeName = in.nextLine();
				break;
				
			case 3:
				adaptedClass = in.nextLine();
				break;
			}			
		}		
		if (option == 4)
		{
			try {
				IEntityType entityType = EntityTypeFactory.createEntityType(packageName, typeName, adaptedClass);
				manager.save(entityType);
				model = manager.loadModel();
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		manipulateSystem();
	}
	
	private void action (IEntityType entityType)
	{
		System.out.println("Select one action: ");
		System.out.println("1- Show entity type");
		System.out.println("2- Change entity type");
		System.out.println("3- Remove entity type");
		System.out.println("4- Show entities");
		System.out.println("5- Create entity");		
		System.out.println("6- Remove entity");		
		System.out.println("7- Back");
		
		Scanner in = new Scanner(System.in);
		int option = Integer.parseInt(in.nextLine());
		
		switch (option)
		{
		case 1:
			showEntityType(entityType);
			return;
			
		case 2:
			changeEntityType(entityType);
			return;
			
		case 3:
			removeEntityType(entityType);
			manipulateSystem();
			return;
		
		case 4:
			showEntitiesForType(entityType, true);
			return;
		
		case 5:
			entity = null;
			createAndManipulateEntity(entityType);
			return;
		
		case 6:
			removeEntity(entityType);
			return;
			
		default:
			manipulateSystem();			
		}
	}
	
	private void removeEntity (IEntityType entityType)
	{
		try
		{
			showEntitiesForType(entityType, false);
			System.out.println("Insert the id of the entity to remove:");
			Scanner in = new Scanner(System.in);
			String id = in.nextLine();
			manager.removeEntity(id, entityType);
			action(entityType);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void removeEntityType (IEntityType entityType)
	{
		try
		{
			manager.removeEntityType(entityType);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void changeEntityType (IEntityType entityType)
	{
		System.out.println("1- Add property type");
		System.out.println("2- Remove property type");
		
		Scanner in = new Scanner(System.in);
		int option = Integer.parseInt(in.nextLine());
		
		switch (option)
		{
		case 1:
			addPropertyType(entityType);
			return;
			
		case 2: 
			removePropertyType(entityType);
			return;
			
		default:
			action(entityType);	
		}
	}
	
	private void removePropertyType (IEntityType entityType)
	{
		try
		{
			System.out.println("Enter property type name");
			Scanner in = new Scanner(System.in);
			entityType.removePropertyType(in.nextLine());
			manager.save(entityType);
			action(entityType);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void addPropertyType (IEntityType entityType)
	{
		try
		{
			Scanner in = new Scanner(System.in);
			System.out.println("Enter property name: ");			
			String name = in.nextLine();
			System.out.println("Enter property type: ");
			Type type = Class.forName(in.nextLine());	
			
			
			System.out.println("Is property type adapted?");
			System.out.println("1- Yes");
			System.out.println("2- No");			
			int option = Integer.parseInt(in.nextLine());
			
			IPropertyType propertyType = null;
			
			if (option == 1)
			{
				System.out.println("Enter adapted class");				
				propertyType = PropertyTypeFactory.createPropertyType(name, type, in.nextLine());
			}
			else
			{
				propertyType = PropertyTypeFactory.createPropertyType(name, type);
			}
			entityType.addPropertyType(propertyType);
			manager.save(entityType);
			action(entityType);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void showEntitiesForType (IEntityType entityType, boolean returnToMenu)
	{
		try
		{
			List<IEntity> entities = manager.getEntitiesForType(entityType);
			for (HasProperties entity : entities)
			{
				showEntity(entity);
			}
			
			if (returnToMenu)
				action(entityType);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void createAndManipulateEntity (IEntityType entityType)
	{
		try {
			if (entity == null)
			{
				entity = entityType.createNewEntity();
			}
			List<IPropertyType> propertyTypes = entityType.getPropertyTypes();
			for (int i = 0; i < propertyTypes.size(); i++)
			{
				System.out.println(i + "- Set " + propertyTypes.get(i).getName());
			}
			
			int nextOption = propertyTypes.size();
			System.out.println(nextOption + "- Save entity");
			System.out.println((nextOption + 1) + "- Back");

			Scanner in = new Scanner(System.in);
			int option = Integer.parseInt(in.nextLine());

			if (option < propertyTypes.size())
			{
				IPropertyType propertyType = propertyTypes.get(option);

				if (!propertyType.isRelationshipProperty())
				{
					System.out.println("Insert value to be put in property");
					String value = in.nextLine();
					if (propertyType.getType().equals(String.class))
					{
						entity.setProperty(propertyType.getName(), value);
					}
					else if (propertyType.getType().equals(Integer.class))
					{
						entity.setProperty(propertyType.getName(), Integer.valueOf(value)); 
					}
					else if (propertyType.getType().equals(Long.class))
					{
						entity.setProperty(propertyType.getName(), Long.valueOf(value)); 
					}
					else if (propertyType.getType().equals(Double.class))
					{
						entity.setProperty(propertyType.getName(), Double.valueOf(value)); 
					}
					else if (propertyType.getType().equals(Float.class))
					{
						entity.setProperty(propertyType.getName(), Float.valueOf(value)); 
					}
					else if (propertyType.getType().equals(Short.class))
					{
						entity.setProperty(propertyType.getName(), Short.valueOf(value)); 
					}
					else if (propertyType.getType().equals(Byte.class))
					{
						entity.setProperty(propertyType.getName(), Byte.valueOf(value)); 
					}
					else if (propertyType.getType().equals(int.class))
					{
						entity.setProperty(propertyType.getName(), Integer.parseInt(value)); 
					}
					else if (propertyType.getType().equals(long.class))
					{
						entity.setProperty(propertyType.getName(), Long.parseLong(value)); 
					}
					else if (propertyType.getType().equals(double.class))
					{
						entity.setProperty(propertyType.getName(), Double.parseDouble(value)); 
					}
					else if (propertyType.getType().equals(float.class))
					{
						entity.setProperty(propertyType.getName(), Float.parseFloat(value)); 
					}
					else if (propertyType.getType().equals(short.class))
					{
						entity.setProperty(propertyType.getName(), Short.parseShort(value)); 
					}
					else if (propertyType.getType().equals(byte.class))
					{
						entity.setProperty(propertyType.getName(), Byte.parseByte(value)); 
					}
					else
					{
						entity.setProperty(propertyType.getName(), value);
					}
				}
				else
				{					
					IEntityType validType = (IEntityType)propertyType.getType();
					List<IEntity> entities = manager.getEntitiesForType(validType);
					for (int i = 0; i < entities.size(); i++)
					{
						System.out.println(i + "-");
						showEntity(entities.get(i));
					}
					System.out.println("Insert entity to refer to:");
					option = Integer.parseInt(in.nextLine());
					entity.setProperty(propertyType.getName(), entities.get(option));
				}
				createAndManipulateEntity(entityType);
			}
			else if (option == propertyTypes.size())
			{
				manager.save(entity);
				action(entityType);
			}
			else
			{
				action (entityType);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void showEntity (HasProperties entity)
	{
		try
		{
			for (IProperty property : entity.getProperties())
			{
				Object value = property.getValue();
				if (value instanceof IEntity)
				{
					IEntity propertyEntity = (IEntity)value;
					System.out.println(property.getPropertyType().getName() + ": " + propertyEntity.getEntityType().getName() + " / " + 
							propertyEntity.getProperty("id").getValue());
				}
				else
				{
					System.out.println(property.getPropertyType().getName() + ": " + value);
				}
			}
			System.out.println();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void showEntityType (IEntityType entityType)
	{
		try
		{
			System.out.println("Name: " + entityType.getName());
			Object associatedObject = entityType.getAssociatedObject();
			if (associatedObject != null)
			{
				System.out.println("AssociatedObject: " + associatedObject.getClass().getCanonicalName());
			}
			System.out.println("PropertyTypes:\n");
			for (IPropertyType propertyType : entityType.getPropertyTypes())
			{
				System.out.println("\tName: " + propertyType.getName());
				System.out.println("\tType: " + propertyType.getTypeAsString());
				Object associatedObjectForProperty = propertyType.getAssociatedObject();
				if (associatedObjectForProperty != null)
				{
					System.out.println("\tAssociatedObject: " + associatedObjectForProperty.getClass().getCanonicalName());
				}
				System.out.println();
			}
			
			System.out.println("Press any key to return");
			Scanner in = new Scanner(System.in);
			in.nextLine();
			manipulateSystem();			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
