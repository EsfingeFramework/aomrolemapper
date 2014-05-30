package org.esfinge.aom.persistence.mongodb;

public class EntityTypeCollectionMap {

	private String packageRegexp;
	
	private String entityTypeRegexp;
	
	private String collection;
	
	public EntityTypeCollectionMap (String entityTypeRegexp, String packageRegexp, String collection)
	{
		this(entityTypeRegexp, collection);
		this.packageRegexp = packageRegexp;
	}
	
	public EntityTypeCollectionMap (String entityTypeRegexp, String collection)
	{
		this.entityTypeRegexp = entityTypeRegexp;
		this.collection = collection;
	}

	public String getEntityTypeRegexp() {
		return entityTypeRegexp;
	}

	public String getCollection() {
		return collection;
	}

	public String getPackageRegexp() {
		return packageRegexp;
	}
}
