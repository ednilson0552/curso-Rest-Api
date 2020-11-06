// classe criada para poder instanciar como componente spring 
package com.algaworks.osworks.core;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // componente spring para configurar beans e permitir acessar como componente spring
public class ModelMapperConfig {

	@Bean // indica que instancia, inicializa o tipo modelmapper e disponibiliza para injecao em outras classes 
	public ModelMapper modelMapper() {
		return new ModelMapper();// gera uma instancia de ModelMapper.
	}
	
}
