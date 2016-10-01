package net.sf.esfinge.aom.onlyPropertyList;

import net.sf.esfinge.aom.api.model.HasProperties;
import net.sf.esfinge.aom.api.model.IProperty;
import net.sf.esfinge.aom.exceptions.EsfingeAOMException;
import net.sf.esfinge.aom.model.rolemapper.core.AdapterEntity;

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