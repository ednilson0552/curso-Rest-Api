// Classe para entrada(input) dados do Id cliente (DTO), para entrada
// do DTO da OSInput
package com.algaworks.osworks.api.model;

import javax.validation.constraints.NotNull;

public class ClienteIdInput {

	@NotNull
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
