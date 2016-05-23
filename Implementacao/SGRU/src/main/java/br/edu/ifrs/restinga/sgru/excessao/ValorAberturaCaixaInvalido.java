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
public class ValorAberturaCaixaInvalido extends Exception {

    public ValorAberturaCaixaInvalido() {
    }

    public ValorAberturaCaixaInvalido(String message) {
        super(message);
    }

    public ValorAberturaCaixaInvalido(String message, Throwable cause) {
        super(message, cause);
    }

    public ValorAberturaCaixaInvalido(Throwable cause) {
        super(cause);
    }

    public ValorAberturaCaixaInvalido(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
