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
public class MatriculaInvalidaException extends Exception {

    public MatriculaInvalidaException() {
    }

    public MatriculaInvalidaException(String message) {
        super(message);
    }

    public MatriculaInvalidaException(String message, Throwable cause) {
        super(message, cause);
    }

    public MatriculaInvalidaException(Throwable cause) {
        super(cause);
    }

    public MatriculaInvalidaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }    
}
