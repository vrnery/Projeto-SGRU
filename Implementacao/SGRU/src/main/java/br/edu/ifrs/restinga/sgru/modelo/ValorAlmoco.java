/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.modelo;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 *
 * @author marcelo.lima
 */
@Entity
public class ValorAlmoco implements Serializable {
    @Id
    @GeneratedValue
    private int id;
    private double valorAlmoco;
    private Date dataValor;

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

    /**
     * @return the dataValor
     */
    public Date getDataValor() {
        return dataValor;
    }

    /**
     * @param dataValor the dataValor to set
     */
    public void setDataValor(Date dataValor) {
        this.dataValor = dataValor;
    }
}
