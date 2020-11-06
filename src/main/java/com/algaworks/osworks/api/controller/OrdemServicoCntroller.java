package com.algaworks.osworks.api.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.osworks.api.model.OrdemServicoInput;
import com.algaworks.osworks.api.model.OrdemServicoModel;
import com.algaworks.osworks.domain.model.OrdemServico;
import com.algaworks.osworks.domain.service.GestaoOrdemServicoService;
import com.algaworks.osworks.doman.repository.OrdemServicoRepository;

@RestController
@RequestMapping("/ordens-servico")
public class OrdemServicoCntroller {
	
	@Autowired
	private GestaoOrdemServicoService gestaoOrdemServicoService;
	
	@Autowired 
	private OrdemServicoRepository ordemServicoRepository;
	
	@Autowired // injeta a ModelMapperl para fazer atribuiçao de uma classe de um tipo para outra
	private ModelMapper modelMapper;
	
	@PostMapping
	@ResponseStatus(code = HttpStatus.CREATED) // Resposta de Sucesso qdo cria Ordemservico
	public OrdemServicoModel criar(@Valid @RequestBody OrdemServicoInput ordemServicoInput) {
		OrdemServico ordemServico = toEntity(ordemServicoInput);
		
		return toModel(gestaoOrdemServicoService.criar(ordemServico));
	}
	
	// quando for uma consulta, listagem, nao precisa criar no gestaoOrdemServicoService
	// pode criar aqui mesmo no Controller (apenas injetando o Repository).
	@GetMapping
	public List<OrdemServicoModel> listar() {
		return toCollectionModel(ordemServicoRepository.findAll());
	}
	
	@GetMapping("/{ordemServicoId}")
	public ResponseEntity<OrdemServicoModel> buscar(@PathVariable Long ordemServicoId) {
		Optional<OrdemServico> ordemServico =  ordemServicoRepository.findById(ordemServicoId);
		
		if (ordemServico.isPresent()) {
			// nao preciso instanciar(new OrdemServicoModel()), a classe sera atribuida diretamente com Modelmapper ;
			//Modelmapper instancia e atribui as propriedades de ordemServico para OrdemServicoModel
			// alterado -OrdemServicoModel ordemServicoModel = modelMapper.map(ordemServico.get(), OrdemServicoModel.class);
			OrdemServicoModel ordemServicoModel = toModel(ordemServico.get());
			return ResponseEntity.ok(ordemServicoModel);
		}
		
		return ResponseEntity.notFound().build();
	}

	@PutMapping("/{ordemServicoId}/finalizacao")
	@ResponseStatus(code = HttpStatus.NO_CONTENT) // retorno codigo 204, realizada finalizacao mas sem retorno no metodo
	public void finalizar(@PathVariable Long ordemServicoId) {
		gestaoOrdemServicoService.finalizar(ordemServicoId);
		
	}
	
	// apenas para facilitar conversao e ser chamado por outros metodos
	protected OrdemServicoModel toModel(OrdemServico ordemServico) {
		return modelMapper.map(ordemServico, OrdemServicoModel.class);
	}
	
	//Transforma uma Lista colecao) de OS em uma lista OSModel
	private List<OrdemServicoModel> toCollectionModel(List<OrdemServico> ordensServico) {
		return ordensServico.stream() // sequencia de elementos que suportam operacao de agregacao e transformacao
				.map(ordemServico -> toModel(ordemServico)) // (.map) aplica funcao stream dos elementos um por um  
				.collect(Collectors.toList()); // reduz para uma colecao 
	}

	// converte OS em uma OSInput usando ModelMapper
	protected OrdemServico toEntity(OrdemServicoInput ordemServicoInput) {
		return modelMapper.map(ordemServicoInput, OrdemServico.class);
	}
	
}
