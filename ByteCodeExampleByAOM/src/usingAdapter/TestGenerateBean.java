package usingAdapter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.exceptions.EsfingeAOMException;
import org.esfinge.aom.model.rolemapper.core.AdapterEntity;


public class TestGenerateBean {
	
	public static void main(String[] args) throws Exception{
		Funcionario f = new Funcionario();
				
		IEntity entity = AdapterEntity.getAdapter(null, f);
		
		System.out.println("----------------------");
		
		entity.setProperty("age", 27);
		entity.setProperty("jobTitle", "Director");
		
		GenerateClasses gc = new GenerateClasses();
		
		// uncomment next line
	  //Object generatedAdapter = gc.generate(entity);
		
		Object handMadeAdapter = new FuncionarioBeanAdapter(entity); 
		
		System.out.println("----------------------Hand Made");
		printClass(handMadeAdapter);
		System.out.println("----------------------Generated");
	  //printClass(generatedAdapter);
		
		
//		System.out.println("\n");
//		System.out.println(BeanUtils.getProperty(obj, "age"));
//		System.out.println(BeanUtils.getProperty(obj, "jobTitle"));
		
				
	}
		

	private static void printClass(Object obj) {
		for(Method m : obj.getClass().getMethods()){
			System.out.println(m);
		}
		
		for(Field field : obj.getClass().getDeclaredFields()){
			System.out.println(field);
		}
	}

}
