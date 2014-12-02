package usingAdapter;


import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.exceptions.EsfingeAOMException;

public class FuncionarioBeanAdapter {
	
	private IEntity entity;
	
	public FuncionarioBeanAdapter(IEntity entity){
		this.entity = entity;
	}
	
	public Integer getAge(){
		try {
			return (Integer) entity.getProperty("age").getValue();
		} catch (EsfingeAOMException e) {
			throw new RuntimeException(e);
		}
	}	
	
	public void setAge(Integer a){
		try {
			entity.setProperty("age", a);
		} catch (EsfingeAOMException e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getJobTitle(){
		try {
			return (String) entity.getProperty("jobTitle").getValue();
		} catch (EsfingeAOMException e) {
			throw new RuntimeException(e);
		}
	}	
	
	public void setJobTitle(String j){
		try {
			entity.setProperty("jobTitle", j);
		} catch (EsfingeAOMException e) {
			throw new RuntimeException(e);
		}
	}
	
		
	//more properties
	/*
	public Double getHeight(){
		try {
			return (Double) entity.getProperty("height").getValue();
		} catch (EsfingeAOMException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setHeight(Double d){
		try {
			entity.setProperty("height", d);
		} catch (EsfingeAOMException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Double getWeight(){
		try {
			return (Double) entity.getProperty("weight").getValue();
		} catch (EsfingeAOMException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setWeight(Double d){
		try {
			entity.setProperty("weight", d);
		} catch (EsfingeAOMException e) {
			throw new RuntimeException(e);
		}
	}
	
	public Integer getIdentityDocument(){
		try {
			return (Integer) entity.getProperty("identityDocument").getValue();
		} catch (EsfingeAOMException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void setIdentityDocument(Integer i){
		try {
			entity.setProperty("identityDocument", i);
		} catch (EsfingeAOMException e) {
			throw new RuntimeException(e);
		}
	}
	
   */
}
