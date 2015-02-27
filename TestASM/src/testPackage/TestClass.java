package testPackage;

import org.esfinge.aom.model.rolemapper.metadata.annotations.Entity;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperties;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperty;

@Entity
public class TestClass {

	@EntityProperty
	private Integer number;
	@EntityProperty
	private Double height;
	
	public TestClass() {

	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}
	
	public Double getHeight() {
		return height;
	}
	
	public void setHeight(Double height) {
		this.height = height;
	}
	
	
	
}
