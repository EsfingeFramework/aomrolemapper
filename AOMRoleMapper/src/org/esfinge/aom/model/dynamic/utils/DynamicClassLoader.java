package org.esfinge.aom.model.dynamic.utils;


public class DynamicClassLoader extends ClassLoader {
	
	private static DynamicClassLoader dynamicClassLoader;
	
	private DynamicClassLoader(ClassLoader cl){
		super(cl);
	}
	
	public static DynamicClassLoader getInstance(ClassLoader cl){
		if(dynamicClassLoader == null){
			dynamicClassLoader = new DynamicClassLoader(cl);
		}		
		return dynamicClassLoader;
	}
	
	public Class defineClass(String name, byte[] b) {
		return defineClass(name, b, 0, b.length);		
	}
}
