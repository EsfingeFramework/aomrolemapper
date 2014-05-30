package org.esfinge.aom.modelretriever.factories;

import java.util.ServiceLoader;

import org.esfinge.aom.api.modelretriever.IModelRetriever;


public class ModelRetrieverFactory {

	private static ModelRetrieverFactory instance;
	
	private ServiceLoader<IModelRetriever> entityManagerLoader;
	
	private ModelRetrieverFactory()
	{
		entityManagerLoader = ServiceLoader.load(IModelRetriever.class);
	}
	
	public static ModelRetrieverFactory getInstance()
	{
		if (instance == null)
		{
			instance = new ModelRetrieverFactory();
		}
		return instance;
	}
	
	public IModelRetriever getEntityManager()
	{
		return getEntityManager(null);
	}
	
	public IModelRetriever getEntityManager(String managerName)
	{
		for (IModelRetriever manager : entityManagerLoader)
		{
			if (managerName == null)
			{
				return manager;
			}
			if (manager.getClass().getName().equals(managerName))
			{
				return manager;
			}
		}
		
		return null;
	}
	
}
