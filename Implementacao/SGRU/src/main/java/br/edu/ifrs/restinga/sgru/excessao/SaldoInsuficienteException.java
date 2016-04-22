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
public class SaldoInsuficienteException extends Exception {
    public SaldoInsuficienteException(String msg) {
        super(msg);
    }
}
