package org.esfinge.aom.onlyPropertyList;

import java.util.ArrayList;
import java.util.List;

import org.esfinge.aom.model.rolemapper.metadata.annotations.Entity;
import org.esfinge.aom.model.rolemapper.metadata.annotations.EntityProperties;
import org.esfinge.aom.simpletypesquare.Information;

@Entity
public class Funcionario {
	
	@EntityProperties
	private List<FuncionarioInfo> informations = new ArrayList<>();

	public List<FuncionarioInfo> getInformations() {
		return informations;
	}

	public void setInformations(List<FuncionarioInfo> informations) {
		this.informations = informations;
	}

}
