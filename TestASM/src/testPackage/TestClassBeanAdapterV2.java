package testPackage;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.exceptions.EsfingeAOMException;

public class TestClassBeanAdapterV2 {

	private IEntity entity;

	public TestClassBeanAdapterV2(IEntity entity) {
		this.entity = entity;
	}
	public TestClassBeanAdapterV2() {
		
	}
	
	public Object getExtraProperty() {
		try {
			return entity.getProperty("extraProperty").getValue();
		} catch (EsfingeAOMException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setExtraProperty(Object a) {
		try {
			entity.setProperty("extraProperty", a);
		} catch (EsfingeAOMException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Integer getIntegerProperty() {
		try {
			return (Integer)entity.getProperty("integerProperty").getValue();
		} catch (EsfingeAOMException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setIntegerProperty(Integer a) {
		try {
			entity.setProperty("integerProperty", a);
		} catch (EsfingeAOMException e) {
			throw new RuntimeException(e);
		}
	}

}


