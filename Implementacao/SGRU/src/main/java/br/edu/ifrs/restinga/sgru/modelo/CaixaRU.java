/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import java.util.Date;

/**
 *
 * @author marcelo.lima
 */
public class CaixaRU {
    private int id;
    private Date data;
    private double valorAbertura;
    private double valorFechamento;
    private VendaAlmoco vendaAlmoco;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the data
     */
    public Date getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(Date data) {
        this.data = data;
    }

    /**
     * @return the valorAbertura
     */
    public double getValorAbertura() {
        return valorAbertura;
    }

    /**
     * @param valorAbertura the valorAbertura to set
     */
    public void setValorAbertura(double valorAbertura) {
        this.valorAbertura = valorAbertura;
    }

    /**
     * @return the valorFechamento
     */
    public double getValorFechamento() {
        return valorFechamento;
    }

    /**
     * @param valorFechamento the valorFechamento to set
     */
    public void setValorFechamento(double valorFechamento) {
        this.valorFechamento = valorFechamento;
    }

    /**
     * @return the vendaAlmoco
     */
    public VendaAlmoco getVendaAlmoco() {
        return vendaAlmoco;
    }

    /**
     * @param vendaAlmoco the vendaAlmoco to set
     */
    public void setVendaAlmoco(VendaAlmoco vendaAlmoco) {
        this.vendaAlmoco = vendaAlmoco;
    }
    
}
