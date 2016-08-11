package org.esfinge.aom.persistence.neo4j;

public class EntityTypeNode {

	private String packageRegexp;
	
	private String entityTypeRegexp;
	
	private String node;
	
	public EntityTypeNode(String entityTypeRegexp, String packageRegexp, String node) {
		this(entityTypeRegexp, node);
		this.packageRegexp = packageRegexp;
	}
	
	public EntityTypeNode(String entityTypeRegexp, String node) {
		this.entityTypeRegexp = entityTypeRegexp;
		this.node = node;
	}

	public String getEntityTypeRegexp() {
		return entityTypeRegexp;
	}

	public String getNode() {
		return node;
	}

	public String getPackageRegexp() {
		return packageRegexp;
	}
}
