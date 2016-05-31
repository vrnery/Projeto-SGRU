/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.excessao;

/**
 *
 * @author 10070133
 */
public class RelatorioInvalidoException extends Exception {
    public RelatorioInvalidoException() {
    }

    public RelatorioInvalidoException(String message) {
        super(message);
    }

    public RelatorioInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }

    public RelatorioInvalidoException(Throwable cause) {
        super(cause);
    }

    public RelatorioInvalidoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }    
}
