package org.esfinge.aom.model.rolemapper.metadata.descriptors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MetadataDescriptor {
	private FieldDescriptor metadataDescriptor;
	private List<FieldDescriptor> fixedMetadataDescriptor = new ArrayList<FieldDescriptor>();
	
	public void setMetadataDescriptor(FieldDescriptor metadataDescriptor) {
		this.metadataDescriptor = metadataDescriptor;
	}
	
	public FieldDescriptor getMetadataDescriptor() {
		return metadataDescriptor;
	}
	
	public void setFixedMetadataDescriptor(List<FieldDescriptor> fixedMetadataDescriptor){
		this.fixedMetadataDescriptor =  fixedMetadataDescriptor;
	}
	
	public List<FieldDescriptor> getFixedMetadataDescriptor(){
		return this.fixedMetadataDescriptor;
	}
}