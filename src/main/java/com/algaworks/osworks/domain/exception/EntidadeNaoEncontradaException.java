// Classe com mensagem mais especifica de erro de excessao.
package com.algaworks.osworks.domain.exception;

public class EntidadeNaoEncontradaException extends NegocioException {

	private static final long serialVersionUID = 1L;

	public EntidadeNaoEncontradaException(String message) {
		super(message);
	}

}
