package usingAdapter;


import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IProperty;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.rolemapper.core.AdapterEntity;

public class PrincipalOnlyPropertyList {
	
	public static void main(String[] args) throws EsfingeAOMException{
		Funcionario f = new Funcionario();
		f.getInformations().add(new FuncionarioInfo("cargo", "gerente"));
		
		IEntity entity = AdapterEntity.getAdapter(null, f);
		for(IProperty p : entity.getProperties()){
			//p.getPropertyType().getType() retorna a Class
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
