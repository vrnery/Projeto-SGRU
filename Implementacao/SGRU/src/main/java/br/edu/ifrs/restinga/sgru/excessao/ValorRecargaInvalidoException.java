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
public class ValorRecargaInvalidoException extends Exception {

    public ValorRecargaInvalidoException() {
    }

    public ValorRecargaInvalidoException(String message) {
        super(message);
    }

    public ValorRecargaInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }

    public ValorRecargaInvalidoException(Throwable cause) {
        super(cause);
    }

    public ValorRecargaInvalidoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
}
