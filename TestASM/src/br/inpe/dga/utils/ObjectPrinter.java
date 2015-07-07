package br.inpe.dga.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ObjectPrinter {
	public static void printClass(Object obj) {
		for (Method m : obj.getClass().getMethods()) {
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
