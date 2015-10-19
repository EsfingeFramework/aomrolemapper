package br.inpe.jpateste.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.annotation.*;

public class ObjectPrinter {
	public static void printClass(Object obj) {
		for (Annotation annotation : obj.getClass().getAnnotations()) {
			System.out.println(annotation);
		}
		
		for (Method m : obj.getClass().getMethods()) {
			for (Annotation annotation : m.getAnnotations()) {
				System.out.println(annotation);
			}
			if (m.getName().startsWith("get")) {
				System.out.print(m + "   => ");
				try {
					System.out.println(m.invoke(obj));
				} catch (Exception e) {
					System.out.println("Erro no metodo "+m);
					e.printStackTrace();
				}
			} else {
				System.out.println(m);
			}
		}

		for (Field field : obj.getClass().getDeclaredFields()) {
			System.out.println(field);
		}
	}
	
	public static void printClass2(Object obj) {
		for (Method m : obj.getClass().getMethods()) {
			System.out.println(m);
		}
		for (Field field : obj.getClass().getDeclaredFields()) {
			System.out.println(field);
		}
	}
}
