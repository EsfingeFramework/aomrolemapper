package org.esfinge.aom.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.esfinge.aom.api.model.IEntity;
import org.esfinge.aom.api.model.IEntityType;

public class ObjectPrinter {
	public static void printClass(Object obj) {
		System.out.println("classe name: " + obj.getClass().getName() + " package: " + obj.getClass().getPackage());
		for (Annotation annotation : obj.getClass().getAnnotations()) {
			System.out.println("  anotacao da classe: " + annotation);
		}

		for (Method m : obj.getClass().getMethods()) {
			for (Annotation annotation : m.getAnnotations()) {
				System.out.println("  anotacao: " + annotation + " do metodo: " + m.getName());
			}
			if (m.getName().startsWith("get")) {
				System.out.print(m + "   => ");
				try {
					System.out.println(m.invoke(obj));
				} catch (Exception e) {
					System.out.println("Erro no metodo " + m.getName() + " Erro: " + m);
					// e.printStackTrace();
				}
			} else {
				System.out.println("  metodo: " + m);
			}
		}

		for (Field field : obj.getClass().getDeclaredFields()) {
			System.out.println("  campo: " + field);
			//System.out.println(field.getType().getName());
			field.setAccessible(true);
			try {
				if (field.get(obj) instanceof IEntity) {
					IEntity object = (IEntity) field.get(obj);
					if (obj != null) {
						printClass2(object);
					}
				}
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}

	public static void printClass2(Object obj) throws IllegalArgumentException, IllegalAccessException {
		for (Method m : obj.getClass().getMethods()) {
			System.out.println(m);
		}
		for (Field field : obj.getClass().getDeclaredFields()) {
			System.out.println(field);
			if (field.getName().contains("entityType")) {
				field.setAccessible(true);
				IEntityType objType = (IEntityType) field.get(obj);
				printClass2(objType);
			}
		}
	}
}
