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
public class Recarga {
    private int id;
    private double valorRecarregado;
    private Date dataCredito;
    private boolean utilizado;

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
     * @return the valorRecarregado
     */
    public double getValorRecarregado() {
        return valorRecarregado;
    }

    /**
     * @param valorRecarregado the valorRecarregado to set
     */
    public void setValorRecarregado(double valorRecarregado) {
        this.valorRecarregado = valorRecarregado;
    }

    /**
     * @return the dataCredito
     */
    public Date getDataCredito() {
        return dataCredito;
    }

    /**
     * @param dataCredito the dataCredito to set
     */
    public void setDataCredito(Date dataCredito) {
        this.dataCredito = dataCredito;
    }

    /**
     * @return the utilizado
     */
    public boolean isUtilizado() {
        return utilizado;
    }

    /**
     * @param utilizado the utilizado to set
     */
    public void setUtilizado(boolean utilizado) {
        this.utilizado = utilizado;
    }
    
}
