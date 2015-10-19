package br.inpe.dga.utils;

public enum AnnotationMapFileName {
	NAME("AnnotationMapping.json");
	
	private final String name;
	
	AnnotationMapFileName(String name){
		this.name = name;
	}
	
	public String getName(){
		return name;
	}
}