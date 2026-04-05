package br.pucminas.aluguelcarros.exception;

import lombok.Getter;

@Getter
public class RegraDeNegocioException extends RuntimeException {

    private final String mensagem;

    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
        this.mensagem = mensagem;
    }
}
