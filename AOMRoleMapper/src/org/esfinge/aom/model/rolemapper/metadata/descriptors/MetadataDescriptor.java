package org.esfinge.aom.model.rolemapper.metadata.descriptors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MetadataDescriptor {
	private List<FieldDescriptor> metadataDescriptor = new ArrayList<FieldDescriptor>();
	private List<FieldDescriptor> fixedMetadataDescriptor = new ArrayList<FieldDescriptor>();
	
	public void setMetadataDescriptor(List<FieldDescriptor> metadataDescriptor) {
		this.metadataDescriptor = metadataDescriptor;
	}
	
	public List<FieldDescriptor> getMetadataDescriptor() {
		return metadataDescriptor;
	}
	
	public void setFixedMetadataDescriptor(List<FieldDescriptor> fixedMetadataDescriptor){
		this.fixedMetadataDescriptor =  fixedMetadataDescriptor;
	}
	
	public List<FieldDescriptor> getFixedMetadataDescriptor(){
		return this.fixedMetadataDescriptor;
	}
}