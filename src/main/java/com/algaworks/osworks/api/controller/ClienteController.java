package com.algaworks.osworks.api.controller;

//import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;

import com.algaworks.osworks.domain.model.Cliente;
import com.algaworks.osworks.domain.service.CadastroClienteService;
import com.algaworks.osworks.doman.repository.ClienteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController   // para dizer que é uma classe de controle Jakarta-persistence
@RequestMapping("/clientes") // mapeamento vindo do cliente para buscar no servidor (/clientes)
public class ClienteController {
/* Teste 1 	
	@GetMapping("/clientes") // mapeamento vindo do cliente tem que achar o /clientes
	public List<Cliente> listar()  {
		var cliente1 = new Cliente();
		cliente1.setId (1L);
		cliente1.setNome("João das Couves");
		cliente1.setEmail("joaodascouves@gmail.com");
		cliente1.setTelefone("484233330908");
		
		var cliente2 = new Cliente();
		cliente2.setId (2L);
		cliente2.setNome("Maria");
		cliente2.setEmail("Mariadascouves@gmail.com");
		cliente2.setTelefone("484233330999");
		
		return Arrays.asList(cliente1,cliente2);
	}
*/

/* exemplo 2 com persistencia JPa	
	@PersistenceContext    // instancia a EntityManager do jakarta-Persistence,  para uso.
	// interface o Jakarta persistence para fazer atualizacoes nas tabelas
	private EntityManager manager;
	
	@GetMapping("/clientes")  // mapeamento vindo do cliente para buscar no servidor (/clientes)
	public List<Cliente> listar()  {
//		System.out.print(manager.createQuery("from Cliente", Cliente.class)
//				.getResultList());
		return this.manager.createQuery("from Cliente", Cliente.class)
				.getResultList();
	
	}
	
*/

	@Autowired	// instanciando o ClienteRepository via injecao Spring
	private ClienteRepository clienteRepository;

	@Autowired // Instancia (injeta) CadastroClienteService  pois esta colocadad como @Service (spring)
	private CadastroClienteService cadastroCliente;

	@GetMapping  
	public List<Cliente> listar()  {
		return clienteRepository.findAll();
//		return clienteRepository.findByNome("Marcia Nelza");
	}

	@GetMapping("/{clienteId}")  // mapeamento trazendo uma varivel
	public ResponseEntity <Cliente> buscar(@PathVariable Long clienteId)  { // vincula a variavel clienteId
		// Optional container que pode retornar nulo
		Optional <Cliente> cliente = clienteRepository.findById(clienteId); 

		if (cliente.isPresent()) {
			// cria resposta (200 ok) para regtorno.
			return ResponseEntity.ok(cliente.get());
		}
		
		return ResponseEntity.notFound().build();
	
	}
	
	@PostMapping 
	@ResponseStatus(HttpStatus.CREATED) // retorna o Status da criacao do registro 
	public Cliente Adicionar(@Valid @RequestBody Cliente cliente) { //@RequestBody - transforma o corpo da requisicao  em Cliente
																	//@Valid - Ativa a validacao do Cliente(bean validaçao no Cliente) 		
		// retorna o registro salvo
		return cadastroCliente.salvar(cliente);
	}
	
	@PutMapping("/{clienteId}")  // mapeamento para Atualizar Cliente trazendo uma varivel
	public ResponseEntity <Cliente> Atualizar(@Valid
											  @PathVariable Long clienteId, 
											  @RequestBody Cliente cliente)  { // vincula a variavel clienteId
	
		if (!clienteRepository.existsById(clienteId)) {
			return ResponseEntity.notFound().build();
		}
		
		cliente.setId(clienteId);
		cliente  = cadastroCliente.salvar(cliente);

		return ResponseEntity.ok(cliente);
	}	
	
	@DeleteMapping("/{clienteId}")  // mapeamento trazendo uma varivel
	public ResponseEntity <Void> Deletar(@PathVariable Long clienteId)  { // vincula a variavel clienteId
		if (!clienteRepository.existsById(clienteId)) {
			return ResponseEntity.notFound().build();
		}
		
		//clienteRepository.deleteById(clienteId);
		cadastroCliente.excluir(clienteId);
		return ResponseEntity.noContent().build();
	}
	
}
