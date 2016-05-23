/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.excessao;

/**
 *
 * @author Marcelo
 */
public class DadoPessoaInvalidoException extends Exception {

    public DadoPessoaInvalidoException() {
    }

    public DadoPessoaInvalidoException(String message) {
        super(message);
    }

    public DadoPessoaInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DadoPessoaInvalidoException(Throwable cause) {
        super(cause);
    }

    public DadoPessoaInvalidoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }    
}
