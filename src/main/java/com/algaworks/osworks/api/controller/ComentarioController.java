package com.algaworks.osworks.api.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.algaworks.osworks.api.model.Comentario;
import com.algaworks.osworks.api.model.ComentarioInput;
import com.algaworks.osworks.api.model.ComentarioModel;
import com.algaworks.osworks.api.model.OrdemServicoModel;
import com.algaworks.osworks.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.osworks.domain.model.OrdemServico;
import com.algaworks.osworks.domain.service.GestaoOrdemServicoService;
import com.algaworks.osworks.doman.repository.OrdemServicoRepository;


@RestController
@RequestMapping("/ordens-servico/{ordemServicoId}/comentarios")
public class ComentarioController {

	@Autowired
	private GestaoOrdemServicoService gestaoOrdemServico;
	
	@Autowired
	private OrdemServicoRepository ordemServicoRepository;


	@Autowired
	private ModelMapper modelMapper;

	@GetMapping
	public List<ComentarioModel> listar(@PathVariable Long ordemServicoId) {
		OrdemServico ordemServico = ordemServicoRepository.findById(ordemServicoId)
				.orElseThrow(() -> new EntidadeNaoEncontradaException("Ordem de serviço não encontrda"));

/* falta testar qdo tem OS mas nao tem Comentario
		Optional<OrdemServico> ordemServico =  ordemServicoRepository.findById(ordemServicoId);
		
		if (ordemServico.isPresent()) {

			System.out.println("ordem ::::::::::"+ordemServico.get().getId().toString());
			System.out.println("nao tem os QUE COISA DIFICIRRRRRRRRRRR");
		
		}
		return toCollectionModel(ordemServico.get().getComentarios());

*/
		return toCollectionModel(ordemServico.getComentarios());
		
	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ComentarioModel adicionar(@PathVariable Long ordemServicoId,
		@Valid @RequestBody	ComentarioInput comentarioInput) {
		
		Comentario comentario = gestaoOrdemServico.adicionarComentario(ordemServicoId, comentarioInput.getDescricao());
		
		return toModel(comentario);
	}

	private ComentarioModel toModel(Comentario comentario) {
		return modelMapper.map(comentario, ComentarioModel.class);
	}

	//Transforma uma Lista Comentarios de OS em uma lista ComentarioModel
	private List<ComentarioModel> toCollectionModel(List<Comentario> comentarios) {
		return comentarios.stream() // sequencia de elementos que suportam operacao de agregacao e transformacao
				.map(comentario -> toModel(comentario)) // (.map) aplica funcao stream dos elementos um por um  
				.collect(Collectors.toList()); // reduz para uma colecao 
	}
	
	
}
