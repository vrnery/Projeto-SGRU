/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.excessao;

/**
 *
 * @author marcelo.lima
 */
public class CadastroInvalidoException extends Exception {

    public CadastroInvalidoException() {
    }

    public CadastroInvalidoException(String message) {
        super(message);
    }

    public CadastroInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }

    public CadastroInvalidoException(Throwable cause) {
        super(cause);
    }

    public CadastroInvalidoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
