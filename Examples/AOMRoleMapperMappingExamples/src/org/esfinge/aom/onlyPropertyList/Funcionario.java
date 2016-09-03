package org.esfinge.aom.onlyPropertyList;

import java.util.ArrayList;
import java.util.List;

import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.Entity;
import net.sf.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperty;

@Entity
public class Funcionario {
	
	@EntityProperty
	private List<FuncionarioInfo> informations = new ArrayList<>();

	public List<FuncionarioInfo> getInformations() {
		return informations;
	}

	public void setInformations(List<FuncionarioInfo> informations) {
		this.informations = informations;
	}

}
