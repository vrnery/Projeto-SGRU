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
import javax.persistence.Temporal;

/**
 *
 * @author marcelo.lima
 */
@Entity
public class Ticket implements Serializable {
    @Id
    @GeneratedValue
    private int id;
    private double valor;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataCriado;
    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date dataUtilizado;

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
     * @return the valor
     */
    public double getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(double valor) {
        this.valor = valor;
    }

    /**
     * @return the dataCriado
     */
    public Date getDataCriado() {
        return dataCriado;
    }

    /**
     * @param dataCriado the dataCriado to set
     */
    public void setDataCriado(Date dataCriado) {
        this.dataCriado = dataCriado;
    }

    /**
     * @return the dataUtilizado
     */
    public Date getDataUtilizado() {
        return dataUtilizado;
    }

    /**
     * @param dataUtilizado the dataUtilizado to set
     */
    public void setDataUtilizado(Date dataUtilizado) {
        this.dataUtilizado = dataUtilizado;
    }
}
