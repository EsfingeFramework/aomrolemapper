package org.esfinge.aom.simpletypesquare;

import java.util.ArrayList;
import java.util.List;

import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.Entity;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperty;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.EntityType;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.FixedEntityProperty;

@Entity
public class Product {

	@EntityType
	private ProductType type;
	
	@FixedEntityProperty
	private String productName;
	
	@EntityProperty
	private List<Information> informations = new ArrayList<>();

	public ProductType getType() {
		return type;
	}

	public void setType(ProductType type) {
		this.type = type;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public List<Information> getInformations() {
		return informations;
	}

	public void setInformations(List<Information> informations) {
		this.informations = informations;
	}
	
}
