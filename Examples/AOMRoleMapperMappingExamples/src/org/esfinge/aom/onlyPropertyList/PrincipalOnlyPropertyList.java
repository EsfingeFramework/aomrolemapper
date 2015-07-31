package org.esfinge.aom.onlyPropertyList;

import org.esfinge.aom.api.model.HasProperties;
import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.api.model.IPropertyType;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.rolemapper.core.AdapterEntity;

public class PrincipalOnlyPropertyList {
	
	public static void main(String[] args) throws EsfingeAOMException{
		Funcionario f = new Funcionario();
		f.getInformations().add(new FuncionarioInfo("cargo", "gerente"));
		
		HasProperties entity = AdapterEntity.getAdapter(null, f);
		for(IProperty p : entity.getProperties()){
			System.out.println(p.getName() + " = "+ p.getValue());
		}
		
		System.out.println("----------------------");
		
		entity.setProperty("idade", 23);
		entity.setProperty("cargo", "diretor");
		
		for(FuncionarioInfo fi : f.getInformations()){
			System.out.println(fi.getName() + " = " + fi.getInfo());
		}		
	}
}