package org.esfinge.aom.model.rolemapper.metadata.descriptors;

import java.util.ArrayList;
import java.util.List;

public class MetadataDescriptor {
	private FieldDescriptor metadataDescriptor;
	private List<FieldDescriptor> fixedMetadataDescriptor = new ArrayList<FieldDescriptor>();
	private FieldDescriptor mapMetadataDescriptor; 
	
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

	public FieldDescriptor getMapMetadataDescriptor() {
		return mapMetadataDescriptor;
	}

	public void setMapMetadataDescriptor(FieldDescriptor mapMetadataDescriptor) {
		this.mapMetadataDescriptor = mapMetadataDescriptor;
	}
}