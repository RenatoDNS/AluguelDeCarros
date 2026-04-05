package br.pucminas.aluguelcarros.exception;

import lombok.Getter;

@Getter
public class EntidadeNaoEncontradaException extends RuntimeException {

    private final String mensagem;

    public EntidadeNaoEncontradaException(String mensagem) {
        super(mensagem);
        this.mensagem = mensagem;
    }
}
