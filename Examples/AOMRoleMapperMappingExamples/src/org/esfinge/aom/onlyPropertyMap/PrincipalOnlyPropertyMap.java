package org.esfinge.aom.onlyPropertyMap;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.rolemapper.core.AdapterEntity;

public class PrincipalOnlyPropertyMap {
	
	public static void main(String[] args) throws EsfingeAOMException{
		Funcionario f = new Funcionario();
		f.getInformations().put("cargo", "gerente");
		
		IEntity entity = AdapterEntity.getAdapter(null, f);
		for(IProperty p : entity.getProperties()){
			System.out.println(p.getName() + " = "+ p.getValue());
		}
		
		System.out.println("----------------------");
		
		entity.setProperty("idade", 23);
		entity.setProperty("cargo", "diretor");
		
		for(String propName : f.getInformations().keySet()){
			System.out.println(propName + " = " + f.getInformations().get(propName));
		}
		
	}

}
