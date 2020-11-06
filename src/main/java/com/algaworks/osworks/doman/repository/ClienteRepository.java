package com.algaworks.osworks.doman.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.algaworks.osworks.domain.model.Cliente;
import java.util.List;

@Repository // anotacao do Spring, defina interface como componente do Spring, fazendo injecao de depencia
public interface ClienteRepository extends JpaRepository<Cliente, Long >{

	List<Cliente> findByNome(String nome);
	Cliente findByEmail(String email);
	
}
