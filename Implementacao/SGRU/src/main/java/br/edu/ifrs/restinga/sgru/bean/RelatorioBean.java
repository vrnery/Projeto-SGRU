/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.ifrs.restinga.sgru.bean;

import java.util.Date;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author marcelo.lima
 */
@ManagedBean
@RequestScoped
public class RelatorioBean {
    private Date dataInicialMin;
    private Date dataInicialMax;
    private Date dataFinalMin;
    private Date dataFinalMax;

    public RelatorioBean() {
        this.dataInicialMax = new Date();
        this.dataFinalMin = new Date();
    }

    /**
     * @return the dataInicialMin
     */
    public Date getDataInicialMin() {
        return dataInicialMin;
    }

    /**
     * @return the dataInicialMax
     */
    public Date getDataInicialMax() {
        return dataInicialMax;
    }

    /**
     * @return the dataFinalMin
     */
    public Date getDataFinalMin() {
        return dataFinalMin;
    }

    /**
     * @return the dataFinalMax
     */
    public Date getDataFinalMax() {
        return dataFinalMax;
    }

    public void tratarDataInicial(SelectEvent evento) {
        Date data = (Date) evento.getObject();
        this.dataFinalMin = data;
    }
    
    public void tratarDataFinal(SelectEvent evento) {
        Date data = (Date) evento.getObject();
        this.dataInicialMax = data;
    }
}