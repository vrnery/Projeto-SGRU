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
public class ValorAlmoco {
    private int id;
    private double valorAlmoco;

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
     * @return the valorAlmoco
     */
    public double getValorAlmoco() {
        return valorAlmoco;
    }

    /**
     * @param valorAlmoco the valorAlmoco to set
     */
    public void setValorAlmoco(double valorAlmoco) {
        this.valorAlmoco = valorAlmoco;
    }

    public ValorAlmoco(Date dataCredito) {
    }        
}
