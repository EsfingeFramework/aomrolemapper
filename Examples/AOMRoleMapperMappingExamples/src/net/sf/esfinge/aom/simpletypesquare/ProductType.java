package net.sf.esfinge.aom.simpletypesquare;

import java.util.ArrayList;
import java.util.List;

import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.EntityType;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.Name;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.PropertyType;

@EntityType
public class ProductType {
	
	public ProductType(String productCode) {
		super();
		this.productCode = productCode;
	}
	
	public ProductType() {
		super();
	}

	@Name
	private String productCode;
	
	@PropertyType
	private List<InformationType> list = new ArrayList<>();

	public String getProductCode() {
		return productCode;
	}

	public void setProductCode(String productCode) {
		this.productCode = productCode;
	}

	public List<InformationType> getList() {
		return list;
	}

	public void setList(List<InformationType> list) {
		this.list = list;
	}

}
